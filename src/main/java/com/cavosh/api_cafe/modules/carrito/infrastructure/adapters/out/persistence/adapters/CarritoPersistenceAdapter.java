package com.cavosh.api_cafe.modules.carrito.infrastructure.adapters.out.persistence.adapters;

import com.cavosh.api_cafe.modules.carrito.domain.model.Carrito;
import com.cavosh.api_cafe.modules.carrito.domain.model.CarritoItem;
import com.cavosh.api_cafe.modules.carrito.domain.ports.out.CarritoRepositorioPuerto;
import com.cavosh.api_cafe.modules.carrito.infrastructure.adapters.in.web.dtos.AgregarItemDTO;
import com.cavosh.api_cafe.modules.carrito.infrastructure.adapters.out.persistence.entities.CarritoEntity;
import com.cavosh.api_cafe.modules.carrito.infrastructure.adapters.out.persistence.entities.CarritoItemEntity;
import com.cavosh.api_cafe.modules.carrito.infrastructure.adapters.out.persistence.entities.CarritoItemOpcionEntity;
import com.cavosh.api_cafe.modules.carrito.infrastructure.adapters.out.persistence.repository.SpringDataCarritoRepository;
import com.cavosh.api_cafe.modules.productos.domain.model.Producto;
import com.cavosh.api_cafe.modules.productos.domain.ports.out.ProductoRepositorioPuerto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CarritoPersistenceAdapter implements CarritoRepositorioPuerto {

    private final SpringDataCarritoRepository springDataCarritoRepository;
    private final ProductoRepositorioPuerto productoRepositorioPuerto;

    @Override
    public Carrito obtenerPorUsuarioId(Long usuarioId) {
        return toDomain(obtenerOCrearEntity(usuarioId));
    }

    /**
     * Agrega un producto al carrito.
     * <p>
     * Regla de consolidación: si el carrito ya tiene un ítem para el MISMO
     * producto con EXACTAMENTE la misma combinación de opciones/personalizaciones
     * (mismo conjunto de producto_opcion_valores, sin importar el orden), no se
     * crea un nuevo registro en carrito_items: simplemente se suma la cantidad al
     * ítem existente. Si el producto no existe todavía o existe con una
     * combinación de opciones distinta, se crea un nuevo ítem.
     */
    @Override
    @Transactional
    public Carrito guardar(Long usuarioId, AgregarItemDTO dto) {
        CarritoEntity carrito = obtenerOCrearEntity(usuarioId);

        Set<Long> opcionesSolicitadas = dto.getOpcionesValoresIds() == null
                ? Set.of()
                : new HashSet<>(dto.getOpcionesValoresIds());

        Optional<CarritoItemEntity> itemExistente = carrito.getItems().stream()
                .filter(item -> item.getProductoId().equals(dto.getProductoId()))
                .filter(item -> item.opcionesValoresIds().equals(opcionesSolicitadas))
                .findFirst();

        if (itemExistente.isPresent()) {
            CarritoItemEntity item = itemExistente.get();
            item.setCantidad(item.getCantidad() + dto.getCantidad());
        } else {
            carrito.getItems().add(construirNuevoItem(carrito, dto, opcionesSolicitadas));
        }

        return toDomain(springDataCarritoRepository.save(carrito));
    }

    @Override
    @Transactional
    public Carrito eliminarItem(Long usuarioId, Long itemId) {
        CarritoEntity carrito = obtenerOCrearEntity(usuarioId);
        carrito.getItems().removeIf(item -> item.getId().equals(itemId));
        return toDomain(springDataCarritoRepository.save(carrito));
    }

    @Override
    @Transactional
    public void vaciar(Long usuarioId) {
        CarritoEntity carrito = obtenerOCrearEntity(usuarioId);
        carrito.getItems().clear();
        springDataCarritoRepository.save(carrito);
    }

    // ------------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------------

    private CarritoEntity obtenerOCrearEntity(Long usuarioId) {
        return springDataCarritoRepository.findByUsuarioId(usuarioId)
                .orElseGet(() -> springDataCarritoRepository.save(
                        CarritoEntity.builder().usuarioId(usuarioId).build()));
    }

    private CarritoItemEntity construirNuevoItem(CarritoEntity carrito, AgregarItemDTO dto, Set<Long> opciones) {
        // El precio unitario se toma del precio base del producto. Los
        // modificadores de precio por opción (producto_opcion_valores.modificador_precio)
        // no se suman aquí porque el repositorio de esos valores todavía no está
        // implementado en el proyecto; es el siguiente paso natural al construir
        // la persistencia completa de productos.
        BigDecimal precioUnitario = productoRepositorioPuerto.obtenerPorId(dto.getProductoId())
                .map(Producto::getPrecio)
                .orElse(BigDecimal.ZERO);

        CarritoItemEntity nuevoItem = CarritoItemEntity.builder()
                .carrito(carrito)
                .productoId(dto.getProductoId())
                .cantidad(dto.getCantidad())
                .precioUnitario(precioUnitario)
                .build();

        opciones.forEach(valorId -> nuevoItem.getOpciones().add(
                CarritoItemOpcionEntity.builder()
                        .carritoItem(nuevoItem)
                        .productoOpcionValorId(valorId)
                        .build()));

        return nuevoItem;
    }

    private Carrito toDomain(CarritoEntity entity) {
        List<CarritoItem> items = entity.getItems().stream()
                .map(this::toDomainItem)
                .toList();

        BigDecimal total = items.stream()
                .map(CarritoItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return Carrito.builder()
                .id(entity.getId())
                .usuarioId(entity.getUsuarioId())
                .items(items)
                .total(total)
                .build();
    }

    private CarritoItem toDomainItem(CarritoItemEntity entity) {
        Producto producto = productoRepositorioPuerto.obtenerPorId(entity.getProductoId())
                .orElse(Producto.builder().id(entity.getProductoId()).build());

        BigDecimal subtotal = entity.getPrecioUnitario().multiply(BigDecimal.valueOf(entity.getCantidad()));

        return CarritoItem.builder()
                .id(entity.getId())
                .producto(producto)
                .cantidad(entity.getCantidad())
                .precioUnitario(entity.getPrecioUnitario())
                .subtotal(subtotal)
                .opcionesValoresIds(entity.opcionesValoresIds())
                .build();
    }
}
