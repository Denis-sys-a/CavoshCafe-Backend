package com.cavosh.api_cafe.modules.auth.infrastructure.adapters.out.persistence.adapters;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.cavosh.api_cafe.modules.auth.domain.model.CodigoVerificacion;
import com.cavosh.api_cafe.modules.auth.domain.ports.out.CodigoVerificacionRepository;
import com.cavosh.api_cafe.modules.auth.infrastructure.adapters.out.persistence.entities.CodigoVerificacionEntity;
import com.cavosh.api_cafe.modules.auth.infrastructure.adapters.out.persistence.repository.SpringDataCodigoVerificacionRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CodigoVerificacionPersistenceAdapter implements CodigoVerificacionRepository {

    private final SpringDataCodigoVerificacionRepository repository;

    @Override
    public CodigoVerificacion guardar(CodigoVerificacion codigo) {
        CodigoVerificacionEntity guardada = repository.save(aEntidad(codigo));
        return aDominio(guardada);
    }

    @Override
    public Optional<CodigoVerificacion> buscarUltimoVigentePorEmail(String email) {
        return repository.findTopByCorreoAndUsadoIsFalseOrderByFechaCreacionDesc(email)
                .map(this::aDominio);
    }

    @Override
    public Optional<CodigoVerificacion> buscarVigentePorEmailYCodigo(String email, String codigo) {
        return repository.findTopByCorreoAndCodigoAndUsadoIsFalseOrderByFechaCreacionDesc(email, codigo)
                .map(this::aDominio);
    }

    // ------------------------------------------------------------------
    // Mapeo dominio <-> entidad (dominio: email/expiracion; entidad: correo/fechaExpiracion)
    // ------------------------------------------------------------------

    private CodigoVerificacionEntity aEntidad(CodigoVerificacion modelo) {
        return CodigoVerificacionEntity.builder()
                .id(modelo.getId())
                .correo(modelo.getEmail())
                .codigo(modelo.getCodigo())
                .fechaExpiracion(modelo.getExpiracion())
                .usado(modelo.isUsado())
                .fechaCreacion(modelo.getFechaCreacion())
                .build();
    }

    private CodigoVerificacion aDominio(CodigoVerificacionEntity entidad) {
        return CodigoVerificacion.builder()
                .id(entidad.getId())
                .email(entidad.getCorreo())
                .codigo(entidad.getCodigo())
                .expiracion(entidad.getFechaExpiracion())
                .usado(entidad.isUsado())
                .fechaCreacion(entidad.getFechaCreacion())
                .build();
    }
}