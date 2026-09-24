package com.cavosh.api_cafe.modules.pedidos.infrastructure.adapters.out.persistence.repository;

import com.cavosh.api_cafe.modules.pedidos.infrastructure.adapters.out.persistence.entities.PedidoEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SpringDataPedidoRepository extends JpaRepository<PedidoEntity, Long> {

    /**
     * Trae en una sola consulta las relaciones "a uno" que el dominio necesita. Los
     * detalles
     * (colección) se cargan por lotes gracias a {@code @BatchSize}, evitando el
     * producto
     * cartesiano de un fetch join sobre una colección.
     */
    @EntityGraph(attributePaths = { "usuario", "sucursal", "direccion", "codigoPromocional" })
    @Query("select p from PedidoEntity p where p.id = :id")
    Optional<PedidoEntity> buscarConRelacionesPorId(@Param("id") Long id);

    /** Pedidos del usuario, del más reciente al más antiguo. */
    @EntityGraph(attributePaths = { "usuario", "sucursal", "direccion", "codigoPromocional" })
    @Query("select p from PedidoEntity p where p.usuario.id = :usuarioId "
            + "order by p.createdAt desc, p.id desc")
    List<PedidoEntity> buscarPorUsuarioId(@Param("usuarioId") Long usuarioId);
}
