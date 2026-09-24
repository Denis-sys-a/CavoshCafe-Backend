package com.cavosh.api_cafe.modules.usuarios.infrastructure.adapters.out.persistence.adapters;

import com.cavosh.api_cafe.modules.usuarios.domain.model.Direccion;
import com.cavosh.api_cafe.modules.usuarios.domain.ports.out.DireccionRepositorioPuerto;
import com.cavosh.api_cafe.modules.usuarios.infrastructure.adapters.out.persistence.entities.DireccionEntity;
import com.cavosh.api_cafe.modules.usuarios.infrastructure.adapters.out.persistence.repository.SpringDataDireccionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Adaptador de salida (persistencia) de las direcciones de un usuario.
 */
@Component
@RequiredArgsConstructor
public class DireccionPersistenceAdapter implements DireccionRepositorioPuerto {

    private final SpringDataDireccionRepository repository;

    /**
     * Inserta (id nulo) o actualiza una dirección del usuario. Si la nueva es la
     * principal, se desmarcan antes las demás para que el usuario tenga una sola
     * predeterminada; todo ocurre en la misma transacción.
     */
    @Override
    @Transactional
    public Direccion guardar(Long usuarioId, Direccion direccion) {
        if (Boolean.TRUE.equals(direccion.getEsPrincipal())) {
            repository.desmarcarPredeterminadas(usuarioId);
        }
        DireccionEntity entity = direccion.getId() == null
                ? new DireccionEntity()
                : repository.findByIdAndUsuarioId(direccion.getId(), usuarioId)
                        .orElseThrow(() -> new IllegalArgumentException(
                                "La dirección " + direccion.getId()
                                        + " no existe o no pertenece al usuario " + usuarioId));
        entity.setUsuarioId(usuarioId);
        aplicarDominio(entity, direccion);
        return aDominio(repository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Direccion> buscarPorUsuarioId(Long usuarioId) {
        return repository.findByUsuarioIdOrderByPredeterminadaDescIdAsc(usuarioId).stream()
                .map(this::aDominio)
                .toList();
    }

    // ------------------------------------------------------------------
    // Mapeo dominio <-> entidad
    // ------------------------------------------------------------------

    private void aplicarDominio(DireccionEntity entity, Direccion modelo) {
        entity.setEtiqueta(modelo.getEtiqueta());
        entity.setCalle(modelo.getCalle());
        entity.setCiudad(modelo.getCiudad());
        entity.setDistrito(modelo.getDistrito());
        entity.setReferencia(modelo.getReferencia());
        entity.setDireccion(componerDireccion(modelo));
        entity.setPredeterminada(Boolean.TRUE.equals(modelo.getEsPrincipal()));
    }

    private Direccion aDominio(DireccionEntity entity) {
        return Direccion.builder()
                .id(entity.getId())
                .etiqueta(entity.getEtiqueta())
                // Filas anteriores a la migración no tienen calle: se usa el texto completo.
                .calle(entity.getCalle() != null ? entity.getCalle() : entity.getDireccion())
                .ciudad(entity.getCiudad())
                .distrito(entity.getDistrito())
                .referencia(entity.getReferencia())
                .esPrincipal(entity.isPredeterminada())
                .build();
    }

    /**
     * "calle, distrito, ciudad" omitiendo las partes vacías (columna direccion NOT
     * NULL).
     */
    private String componerDireccion(Direccion direccion) {
        return Stream.of(direccion.getCalle(), direccion.getDistrito(), direccion.getCiudad())
                .filter(parte -> parte != null && !parte.isBlank())
                .map(String::trim)
                .collect(Collectors.joining(", "));
    }
}
