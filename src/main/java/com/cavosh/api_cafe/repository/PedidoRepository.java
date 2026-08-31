package com.cavosh.api_cafe.repository;

import com.cavosh.api_cafe.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUsuarioIdOrderByCreatedAtDesc(Long usuarioId);
}
