package com.cavosh.api_cafe.modules.pedidos.infrastructure.adapters.out.persistence.adapters;

import com.cavosh.api_cafe.modules.pedidos.domain.model.Pedido;
import com.cavosh.api_cafe.modules.pedidos.domain.ports.out.PedidoRepositorioPuerto;
import com.cavosh.api_cafe.modules.pedidos.infrastructure.adapters.out.persistence.entities.PedidoEntity;
import com.cavosh.api_cafe.modules.pedidos.infrastructure.adapters.out.persistence.mappers.PedidoPersistenceMapper;
import com.cavosh.api_cafe.modules.pedidos.infrastructure.adapters.out.persistence.repository.SpringDataPedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador de salida (persistencia) del módulo de pedidos.
 *
 * <p>
 * Los métodos son transaccionales porque el mapeo a dominio recorre relaciones
 * lazy
 * (usuario, sucursal, detalles...) que deben resolverse dentro de la sesión.
 */
@Component
@RequiredArgsConstructor
public class PedidoPersistenceAdapter implements PedidoRepositorioPuerto {

    private final SpringDataPedidoRepository repository;
    private final PedidoPersistenceMapper mapper;

    /**
     * Inserta (id nulo) o actualiza (id existente). Al actualizar se parte de la
     * entidad
     * persistida para conservar {@code numero_pedido} y los datos que el dominio no
     * modela.
     */
    @Override
    @Transactional
    public Pedido guardar(Pedido pedido) {
        PedidoEntity entity = pedido.getId() == null
                ? new PedidoEntity()
                : repository.findById(pedido.getId())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "El pedido " + pedido.getId() + " no existe"));
        mapper.aplicarDominio(entity, pedido);
        return mapper.aDominio(repository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pedido> buscarPorId(Long id) {
        return repository.buscarConRelacionesPorId(id).map(mapper::aDominio);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> buscarPorUsuarioId(Long usuarioId) {
        return repository.buscarPorUsuarioId(usuarioId).stream()
                .map(mapper::aDominio)
                .toList();
    }
}
