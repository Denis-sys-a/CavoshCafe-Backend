package com.cavosh.api_cafe.modules.pedidos.application.usecases;

import com.cavosh.api_cafe.modules.pedidos.domain.enums.MetodoEntrega;
import com.cavosh.api_cafe.modules.pedidos.domain.exception.DireccionRequeridaException;
import com.cavosh.api_cafe.modules.pedidos.domain.model.Pedido;
import com.cavosh.api_cafe.modules.pedidos.domain.ports.in.CrearPedidoCasoUso;
import com.cavosh.api_cafe.modules.pedidos.domain.ports.out.PedidoRepositorioPuerto;
import com.cavosh.api_cafe.modules.pedidos.infrastructure.adapters.in.web.dtos.CrearPedidoRequestDTO;
import com.cavosh.api_cafe.modules.promociones.application.usecase.ValidarCuponUseCase;
import com.cavosh.api_cafe.modules.promociones.domain.model.Promocion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CrearPedidoCasoUsoImpl implements CrearPedidoCasoUso {

    private final PedidoRepositorioPuerto pedidoRepositorioPuerto;
    private final ValidarCuponUseCase validarCuponUseCase;

    @Override
    public Pedido crearPedido(CrearPedidoRequestDTO dto) {
        validarMetodoEntrega(dto);

        Promocion cuponAplicado = null;
        if (dto.getCodigoPromocional() != null && !dto.getCodigoPromocional().isBlank()) {
            int usosPreviosDelUsuario = contarUsosPreviosDelUsuario(dto.getUsuarioId(), dto.getCodigoPromocional());
            cuponAplicado = validarCuponUseCase.validarCupon(dto.getCodigoPromocional(), usosPreviosDelUsuario);
        }

        // TODO: resolver usuario/sucursal/dirección/productos y calcular
        // subtotal, descuento y total. Esto requiere que la capa de persistencia
        // de pedidos/productos/sucursales (hoy son clases vacías en el proyecto)
        // esté implementada; queda fuera del alcance de esta mejora puntual.
        Pedido pedido = Pedido.builder()
                .metodoEntrega(dto.getMetodoEntrega())
                .codigoPromocional(cuponAplicado)
                .build();

        Pedido pedidoGuardado = pedidoRepositorioPuerto.guardar(pedido);

        // El cupón solo se "consume" cuando la compra se concreta, es decir,
        // una vez que el pedido ya fue creado/guardado exitosamente.
        if (cuponAplicado != null) {
            validarCuponUseCase.registrarUso(cuponAplicado);
        }

        return pedidoGuardado;
    }

    private void validarMetodoEntrega(CrearPedidoRequestDTO dto) {
        if (dto.getMetodoEntrega() == MetodoEntrega.DELIVERY && dto.getDireccionId() == null) {
            throw new DireccionRequeridaException(
                    "Debe indicar una dirección de entrega (direccionId) cuando el método de entrega es DELIVERY");
        }
    }

    private int contarUsosPreviosDelUsuario(Long usuarioId, String codigo) {
        return (int) pedidoRepositorioPuerto.buscarPorUsuarioId(usuarioId).stream()
                .filter(p -> p.getCodigoPromocional() != null
                        && codigo.equalsIgnoreCase(p.getCodigoPromocional().getCodigo()))
                .count();
    }
}
