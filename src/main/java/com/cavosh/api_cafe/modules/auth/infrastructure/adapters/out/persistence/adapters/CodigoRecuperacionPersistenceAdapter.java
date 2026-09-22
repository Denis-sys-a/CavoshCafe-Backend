package com.cavosh.api_cafe.modules.auth.infrastructure.adapters.out.persistence.adapters;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.cavosh.api_cafe.modules.auth.domain.model.CodigoRecuperacion;
import com.cavosh.api_cafe.modules.auth.domain.ports.out.CodigoRecuperacionRepository;
import com.cavosh.api_cafe.modules.auth.infrastructure.adapters.out.persistence.entities.CodigoRecuperacionEntity;
import com.cavosh.api_cafe.modules.auth.infrastructure.adapters.out.persistence.repository.SpringDataCodigoRecuperacionRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CodigoRecuperacionPersistenceAdapter implements CodigoRecuperacionRepository {

    private final SpringDataCodigoRecuperacionRepository repository;

    @Override
    public CodigoRecuperacion guardar(CodigoRecuperacion codigo) {
        CodigoRecuperacionEntity guardado = repository.save(aEntidad(codigo));
        return aDominio(guardado);
    }

    @Override
    public Optional<CodigoRecuperacion> buscarUltimoVigentePorEmail(String email) {
        return repository.findTopByCorreoAndUsadoIsFalseOrderByFechaCreacionDesc(email)
                .map(this::aDominio);
    }

    @Override
    public Optional<CodigoRecuperacion> buscarVigentePorEmailYCodigo(String email, String codigo) {
        return repository.findTopByCorreoAndCodigoAndUsadoIsFalseOrderByFechaCreacionDesc(email, codigo)
                .map(this::aDominio);
    }

    @Override
    public Optional<CodigoRecuperacion> buscarVigentePorEmailYToken(String email, String token) {
        return repository
                .findTopByCorreoAndTokenAndVerificadoIsTrueAndUsadoIsFalseOrderByFechaCreacionDesc(email, token)
                .map(this::aDominio);
    }

    // ------------------------------------------------------------------
    // Mapeo dominio <-> entidad (dominio: email/expiracionCodigo/expiracionToken;
    // entidad: correo/fechaExpiracionCodigo/fechaExpiracionToken)
    // ------------------------------------------------------------------

    private CodigoRecuperacionEntity aEntidad(CodigoRecuperacion modelo) {
        return CodigoRecuperacionEntity.builder()
                .id(modelo.getId())
                .correo(modelo.getEmail())
                .codigo(modelo.getCodigo())
                .token(modelo.getToken())
                .fechaExpiracionCodigo(modelo.getExpiracionCodigo())
                .fechaExpiracionToken(modelo.getExpiracionToken())
                .verificado(modelo.isVerificado())
                .usado(modelo.isUsado())
                .fechaCreacion(modelo.getFechaCreacion())
                .build();
    }

    private CodigoRecuperacion aDominio(CodigoRecuperacionEntity entidad) {
        return CodigoRecuperacion.builder()
                .id(entidad.getId())
                .email(entidad.getCorreo())
                .codigo(entidad.getCodigo())
                .token(entidad.getToken())
                .expiracionCodigo(entidad.getFechaExpiracionCodigo())
                .expiracionToken(entidad.getFechaExpiracionToken())
                .verificado(entidad.isVerificado())
                .usado(entidad.isUsado())
                .fechaCreacion(entidad.getFechaCreacion())
                .build();
    }
}
