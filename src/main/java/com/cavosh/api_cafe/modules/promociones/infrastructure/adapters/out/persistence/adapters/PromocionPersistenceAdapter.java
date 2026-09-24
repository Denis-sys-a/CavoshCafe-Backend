package com.cavosh.api_cafe.modules.promociones.infrastructure.adapters.out.persistence.adapters;

import com.cavosh.api_cafe.modules.promociones.domain.model.Promocion;
import com.cavosh.api_cafe.modules.promociones.domain.repository.PromocionRepository;
import com.cavosh.api_cafe.modules.promociones.infrastructure.adapters.out.persistence.entities.CodigoPromocionalEntity;
import com.cavosh.api_cafe.modules.promociones.infrastructure.adapters.out.persistence.repository.SpringDataPromocionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PromocionPersistenceAdapter implements PromocionRepository {

    private final SpringDataPromocionRepository springDataPromocionRepository;

    @Override
    public Optional<Promocion> buscarPorCodigo(String codigo) {
        return springDataPromocionRepository.findByCodigoIgnoreCase(codigo)
                .map(this::toDomain);
    }

    @Override
    public Promocion guardar(Promocion promocion) {
        CodigoPromocionalEntity entity = toEntity(promocion);
        CodigoPromocionalEntity guardada = springDataPromocionRepository.save(entity);
        return toDomain(guardada);
    }

    private Promocion toDomain(CodigoPromocionalEntity entity) {
        return Promocion.builder()
                .id(entity.getId())
                .codigo(entity.getCodigo())
                .descuento(entity.getValorDescuento())
                .tipoDescuento(entity.getTipoDescuento())
                .fechaInicio(entity.getValidoDesde())
                .fechaFin(entity.getValidoHasta())
                .activo(entity.isActivo())
                .usoMaximo(entity.getUsoMaximo())
                .usosActuales(entity.getUsosActuales())
                .usoMaximoPorUsuario(entity.getUsoMaximoPorUsuario())
                .build();
    }

    private CodigoPromocionalEntity toEntity(Promocion promocion) {
        return CodigoPromocionalEntity.builder()
                .id(promocion.getId())
                .codigo(promocion.getCodigo())
                .tipoDescuento(promocion.getTipoDescuento())
                .valorDescuento(promocion.getDescuento())
                .validoDesde(promocion.getFechaInicio())
                .validoHasta(promocion.getFechaFin())
                .activo(promocion.isActivo())
                .usoMaximo(promocion.getUsoMaximo())
                .usosActuales(promocion.getUsosActuales())
                .usoMaximoPorUsuario(promocion.getUsoMaximoPorUsuario())
                .build();
    }
}
