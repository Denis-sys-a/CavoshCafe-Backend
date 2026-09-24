package com.cavosh.api_cafe.modules.productos.infrastructure.adapters.out.persistence.adapters;

import com.cavosh.api_cafe.modules.productos.domain.model.ProductoSucursalStockId;
import com.cavosh.api_cafe.modules.productos.domain.ports.out.ProductoSucursalStockRepositorioPuerto;
import com.cavosh.api_cafe.modules.productos.infrastructure.adapters.out.persistence.entities.ProductoSucursalStockEntity;
import com.cavosh.api_cafe.modules.productos.infrastructure.adapters.out.persistence.repository.SpringDataProductoSucursalStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductoSucursalStockPersistenceAdapter implements ProductoSucursalStockRepositorioPuerto {

    private final SpringDataProductoSucursalStockRepository springDataRepository;

    @Override
    public List<Long> obtenerIdsDisponiblesPorSucursal(Long sucursalId) {
        return springDataRepository.findByIdSucursalIdAndDisponibleTrue(sucursalId).stream()
                .map(e -> e.getId().getProductoId())
                .toList();
    }

    @Override
    public List<Long> obtenerIdsNoDisponiblesPorSucursal(Long sucursalId) {
        return springDataRepository.findByIdSucursalId(sucursalId).stream()
                .filter(e -> !e.isDisponible())
                .map(e -> e.getId().getProductoId())
                .toList();
    }

    @Override
    public boolean estaDisponible(Long productoId, Long sucursalId) {
        ProductoSucursalStockId id = new ProductoSucursalStockId(productoId, sucursalId);
        // Si no hay fila registrada para ese par producto/sucursal, se asume disponible.
        return springDataRepository.findById(id)
                .map(ProductoSucursalStockEntity::isDisponible)
                .orElse(true);
    }

    @Override
    public void actualizarDisponibilidad(Long productoId, Long sucursalId, boolean disponible) {
        ProductoSucursalStockId id = new ProductoSucursalStockId(productoId, sucursalId);
        ProductoSucursalStockEntity entity = springDataRepository.findById(id)
                .orElse(ProductoSucursalStockEntity.builder().id(id).build());
        entity.setDisponible(disponible);
        springDataRepository.save(entity);
    }
}
