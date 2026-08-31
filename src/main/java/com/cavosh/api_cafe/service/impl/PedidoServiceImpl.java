package com.cavosh.api_cafe.service.impl;

import com.cavosh.api_cafe.dto.*;
import com.cavosh.api_cafe.entity.*;
import com.cavosh.api_cafe.exception.EmptyCartException;
import com.cavosh.api_cafe.exception.InvalidPromoCodeException;
import com.cavosh.api_cafe.exception.ResourceNotFoundException;
import com.cavosh.api_cafe.repository.*;
import com.cavosh.api_cafe.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final CarritoRepository carritoRepository;
    private final PedidoRepository pedidoRepository;
    private final SucursalRepository sucursalRepository;
    private final UsuarioDireccionRepository usuarioDireccionRepository;
    private final CodigoPromocionalRepository codigoPromocionalRepository;
    private final PagoRepository pagoRepository;
    private final HistorialEstadoPedidoRepository historialRepository;

    @Override
    @Transactional
    public PedidoResponseDTO crearPedidoDesdeCarrito(CheckoutRequestDTO dto) {
        Carrito carrito = carritoRepository.findByUsuarioId(dto.getUsuarioId())
                .orElseThrow(() -> new EmptyCartException("El usuario no tiene un carrito activo"));

        if (carrito.getItems().isEmpty()) {
            throw new EmptyCartException("El carrito está vacío, agrega productos antes de hacer checkout");
        }

        Sucursal sucursal = sucursalRepository.findById(dto.getSucursalId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sucursal no encontrada con id: " + dto.getSucursalId()));

        UsuarioDireccion direccion = null;
        if (dto.getMetodoEntrega() == MetodoEntrega.DELIVERY) {
            if (dto.getDireccionId() == null) {
                throw new IllegalArgumentException(
                        "La direccionId es obligatoria cuando el método de entrega es DELIVERY");
            }
            direccion = usuarioDireccionRepository.findById(dto.getDireccionId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Dirección no encontrada con id: " + dto.getDireccionId()));
        }

        // 1. Construir los items del pedido
        Pedido pedido = Pedido.builder()
                .numeroPedido(generarNumeroPedido())
                .usuario(carrito.getUsuario())
                .sucursal(sucursal)
                .direccion(direccion)
                .metodoEntrega(dto.getMetodoEntrega())
                .estado(EstadoPedido.CREADO)
                .subtotal(BigDecimal.ZERO)
                .descuento(BigDecimal.ZERO)
                .total(BigDecimal.ZERO)
                .build();

        for (CarritoItem carritoItem : carrito.getItems()) {
            BigDecimal subtotalItem = carritoItem.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(carritoItem.getCantidad()));

            PedidoItem pedidoItem = PedidoItem.builder()
                    .pedido(pedido)
                    .producto(carritoItem.getProducto())
                    .nombreProducto(carritoItem.getProducto().getName())
                    .cantidad(carritoItem.getCantidad())
                    .precioUnitario(carritoItem.getPrecioUnitario())
                    .subtotal(subtotalItem)
                    .build();

            carritoItem.getOpciones().forEach(opcion -> pedidoItem.getOpciones().add(PedidoItemOpcion.builder()
                    .pedidoItem(pedidoItem)
                    .nombreOpcion(opcion.getProductoOpcionValor().getProductoOpcion().getNombreOpcion())
                    .nombreValor(opcion.getProductoOpcionValor().getNombreValor())
                    .modificadorPrecio(opcion.getProductoOpcionValor().getModificadorPrecio())
                    .build()));

            pedido.getItems().add(pedidoItem);
        }

        // 2. Calcular subtotal, descuento y total
        BigDecimal subtotal = pedido.getItems().stream()
                .map(PedidoItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal descuento = BigDecimal.ZERO;
        if (dto.getCodigoPromocional() != null && !dto.getCodigoPromocional().isBlank()) {
            CodigoPromocional promo = codigoPromocionalRepository
                    .findByCodigoAndActivoTrue(dto.getCodigoPromocional())
                    .orElseThrow(() -> new InvalidPromoCodeException("Código promocional inválido o inactivo"));

            LocalDateTime ahora = LocalDateTime.now();
            if (ahora.isBefore(promo.getValidoDesde()) || ahora.isAfter(promo.getValidoHasta())) {
                throw new InvalidPromoCodeException("El código promocional no está vigente");
            }

            descuento = (promo.getTipoDescuento() == TipoDescuento.PORCENTAJE)
                    ? subtotal.multiply(promo.getValorDescuento()).divide(BigDecimal.valueOf(100), 2,
                            RoundingMode.HALF_UP)
                    : promo.getValorDescuento();

            if (descuento.compareTo(subtotal) > 0) {
                descuento = subtotal; // el descuento nunca supera el subtotal
            }
            pedido.setCodigoPromocional(promo);
        }

        BigDecimal total = subtotal.subtract(descuento);

        pedido.setSubtotal(subtotal);
        pedido.setDescuento(descuento);
        pedido.setTotal(total);

        pedido = pedidoRepository.save(pedido);

        // 3. Registrar pago inicial (pendiente)
        Pago pago = Pago.builder()
                .pedido(pedido)
                .metodoPago(dto.getMetodoPago())
                .estado(EstadoPago.PENDIENTE)
                .build();
        pagoRepository.save(pago);

        // 4. Registrar historial de estado inicial
        HistorialEstadoPedido historial = HistorialEstadoPedido.builder()
                .pedido(pedido)
                .estado(EstadoPedido.CREADO)
                .build();
        historialRepository.save(historial);

        // 5. Vaciar el carrito (cascade elimina cart_items y cart_item_options)
        carrito.getItems().clear();
        carritoRepository.save(carrito);

        return toResponseDTO(pedido);
    }

    @Override
    public List<PedidoResponseDTO> obtenerHistorialPorUsuario(Long usuarioId) {
        return pedidoRepository.findByUsuarioIdOrderByCreatedAtDesc(usuarioId).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private String generarNumeroPedido() {
        return "CAV-" + System.currentTimeMillis();
    }

    private PedidoResponseDTO toResponseDTO(Pedido pedido) {
        List<PedidoItemResponseDTO> itemsDTO = pedido.getItems().stream()
                .map(item -> PedidoItemResponseDTO.builder()
                        .nombreProducto(item.getNombreProducto())
                        .cantidad(item.getCantidad())
                        .precioUnitario(item.getPrecioUnitario())
                        .subtotal(item.getSubtotal())
                        .opciones(item.getOpciones().stream()
                                .map(op -> PedidoItemOpcionResponseDTO.builder()
                                        .nombreOpcion(op.getNombreOpcion())
                                        .nombreValor(op.getNombreValor())
                                        .modificadorPrecio(op.getModificadorPrecio())
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());

        return PedidoResponseDTO.builder()
                .id(pedido.getId())
                .numeroPedido(pedido.getNumeroPedido())
                .usuarioId(pedido.getUsuario().getId())
                .sucursalId(pedido.getSucursal().getId())
                .estado(pedido.getEstado())
                .metodoEntrega(pedido.getMetodoEntrega())
                .subtotal(pedido.getSubtotal())
                .descuento(pedido.getDescuento())
                .total(pedido.getTotal())
                .items(itemsDTO)
                .createdAt(pedido.getCreatedAt())
                .build();
    }
}
