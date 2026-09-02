package com.cavosh.api_cafe.service.impl;

import com.cavosh.api_cafe.dto.*;
import com.cavosh.api_cafe.entity.*;
import com.cavosh.api_cafe.exception.ResourceNotFoundException;
import com.cavosh.api_cafe.repository.*;
import com.cavosh.api_cafe.service.CarritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarritoServiceImpl implements CarritoService {

        private final CarritoRepository carritoRepository;
        private final CarritoItemRepository carritoItemRepository;
        private final UsuarioRepository usuarioRepository;
        private final ProductRepository productRepository;
        private final ProductoOpcionValorRepository productoOpcionValorRepository;

        @Override
        @Transactional
        public CarritoResponseDTO obtenerCarritoPorUsuario(Long usuarioId) {
                Carrito carrito = obtenerOCrearCarrito(usuarioId);
                return toResponseDTO(carrito);
        }

        @Override
        @Transactional
        public CarritoResponseDTO agregarItem(AgregarCarritoItemRequestDTO dto) {
                Carrito carrito = obtenerOCrearCarrito(dto.getUsuarioId());

                Product producto = productRepository.findById(dto.getProductoId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Producto no encontrado con id: " + dto.getProductoId()));

                List<ProductoOpcionValor> valoresElegidos = (dto.getOpcionValorIds() == null)
                                ? List.of()
                                : dto.getOpcionValorIds().stream()
                                                .map(id -> productoOpcionValorRepository.findById(id)
                                                                .orElseThrow(() -> new ResourceNotFoundException(
                                                                                "Valor de opción no encontrado con id: "
                                                                                                + id)))
                                                .collect(Collectors.toList());

                BigDecimal modificadores = valoresElegidos.stream()
                                .map(valor -> valor.getModificadorPrecio())
                                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

                BigDecimal precioUnitario = producto.getBasePrice().add(modificadores);

                CarritoItem item = CarritoItem.builder()
                                .carrito(carrito)
                                .producto(producto)
                                .cantidad(dto.getCantidad())
                                .precioUnitario(precioUnitario)
                                .build();

                valoresElegidos.forEach(valor -> item.getOpciones().add(CarritoItemOpcion.builder()
                                .carritoItem(item)
                                .productoOpcionValor(valor)
                                .build()));

                carrito.getItems().add(item);
                carritoRepository.save(carrito);

                return toResponseDTO(carrito);
        }

        @Override
        @Transactional
        public CarritoResponseDTO actualizarCantidad(Long itemId, ActualizarCarritoItemRequestDTO dto) {
                CarritoItem item = carritoItemRepository.findById(itemId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Ítem de carrito no encontrado con id: " + itemId));

                item.setCantidad(dto.getCantidad());
                carritoItemRepository.save(item);

                return toResponseDTO(item.getCarrito());
        }

        @Override
        @Transactional
        public CarritoResponseDTO eliminarItem(Long itemId) {
                CarritoItem item = carritoItemRepository.findById(itemId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Ítem de carrito no encontrado con id: " + itemId));

                Carrito carrito = item.getCarrito();
                carrito.getItems().remove(item);
                carritoRepository.save(carrito);

                return toResponseDTO(carrito);
        }

        private Carrito obtenerOCrearCarrito(Long usuarioId) {
                return carritoRepository.findByUsuarioId(usuarioId)
                                .orElseGet(() -> {
                                        Usuario usuario = usuarioRepository.findById(usuarioId)
                                                        .orElseThrow(() -> new ResourceNotFoundException(
                                                                        "Usuario no encontrado con id: " + usuarioId));
                                        Carrito nuevo = Carrito.builder().usuario(usuario).build();
                                        return carritoRepository.save(nuevo);
                                });
        }

        private CarritoResponseDTO toResponseDTO(Carrito carrito) {
                List<CarritoItemResponseDTO> itemsDTO = carrito.getItems().stream()
                                .map(this::toItemResponseDTO)
                                .collect(Collectors.toList());

                BigDecimal total = itemsDTO.stream()
                                .map(itemDto -> itemDto.getSubtotal())
                                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

                return CarritoResponseDTO.builder()
                                .id(carrito.getId())
                                .usuarioId(carrito.getUsuario().getId())
                                .items(itemsDTO)
                                .total(total)
                                .build();
        }

        private CarritoItemResponseDTO toItemResponseDTO(CarritoItem item) {
                List<CarritoItemOpcionResponseDTO> opcionesDTO = item.getOpciones().stream()
                                .map(op -> CarritoItemOpcionResponseDTO.builder()
                                                .nombreOpcion(op.getProductoOpcionValor().getProductoOpcion()
                                                                .getNombreOpcion())
                                                .nombreValor(op.getProductoOpcionValor().getNombreValor())
                                                .modificadorPrecio(op.getProductoOpcionValor().getModificadorPrecio())
                                                .build())
                                .collect(Collectors.toList());

                BigDecimal subtotal = item.getPrecioUnitario().multiply(BigDecimal.valueOf(item.getCantidad()));

                return CarritoItemResponseDTO.builder()
                                .id(item.getId())
                                .productoId(item.getProducto().getId())
                                .nombreProducto(item.getProducto().getName())
                                .cantidad(item.getCantidad())
                                .precioUnitario(item.getPrecioUnitario())
                                .subtotal(subtotal)
                                .opciones(opcionesDTO)
                                .build();
        }
}
