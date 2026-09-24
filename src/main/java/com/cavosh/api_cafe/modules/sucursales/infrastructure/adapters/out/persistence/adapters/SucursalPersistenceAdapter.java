package com.cavosh.api_cafe.modules.sucursales.infrastructure.adapters.out.persistence.adapters;

import com.cavosh.api_cafe.modules.sucursales.domain.model.Sucursal;
import com.cavosh.api_cafe.modules.sucursales.domain.ports.out.SucursalRepositorioPuerto;
import com.cavosh.api_cafe.modules.sucursales.infrastructure.adapters.out.persistence.entities.SucursalEntity;
import com.cavosh.api_cafe.modules.sucursales.infrastructure.adapters.out.persistence.repository.SpringDataSucursalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Adaptador de salida (persistencia) del módulo de sucursales.
 */
@Component
@RequiredArgsConstructor
public class SucursalPersistenceAdapter implements SucursalRepositorioPuerto {

    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm");
    private static final Pattern HORARIO = Pattern.compile("^\\s*(\\d{2}:\\d{2})\\s*-\\s*(\\d{2}:\\d{2})\\s*$");

    private final SpringDataSucursalRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<Sucursal> obtenerTodas() {
        return repository.findAll(Sort.by("id")).stream()
                .map(this::aDominio)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Sucursal> obtenerPorId(Long id) {
        return repository.findById(id).map(this::aDominio);
    }

    /**
     * Inserta (id nulo) o actualiza (id existente). Al actualizar se parte de la
     * entidad persistida para conservar latitud/longitud, que el dominio no modela.
     */
    @Override
    @Transactional
    public Sucursal guardar(Sucursal sucursal) {
        SucursalEntity entity = sucursal.getId() == null
                ? new SucursalEntity()
                : repository.findById(sucursal.getId()).orElseGet(SucursalEntity::new);
        aplicarDominio(entity, sucursal);
        return aDominio(repository.save(entity));
    }

    /**
     * Borrado físico. Falla si la sucursal tiene pedidos asociados (FK
     * fk_pedido_sucursal).
     */
    @Override
    @Transactional
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    // ------------------------------------------------------------------
    // Mapeo dominio <-> entidad
    // ------------------------------------------------------------------

    private void aplicarDominio(SucursalEntity entity, Sucursal modelo) {
        entity.setNombre(modelo.getNombre());
        entity.setDireccion(modelo.getDireccion());
        entity.setTelefono(modelo.getTelefono());
        aplicarHorario(entity, modelo.getHorarioAtencion());
        // Nulo = conservar el valor actual (una entidad nueva arranca en activo =
        // true).
        if (modelo.getActiva() != null) {
            entity.setActivo(modelo.getActiva());
        }
    }

    private Sucursal aDominio(SucursalEntity entity) {
        return Sucursal.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .direccion(entity.getDireccion())
                .telefono(entity.getTelefono())
                .horarioAtencion(formatearHorario(entity.getHoraApertura(), entity.getHoraCierre()))
                .activa(entity.isActivo())
                .build();
    }

    /**
     * "08:00 - 22:00" -> hora_apertura / hora_cierre. Vacío o nulo limpia ambas
     * columnas.
     */
    private void aplicarHorario(SucursalEntity entity, String horario) {
        if (horario == null || horario.isBlank()) {
            entity.setHoraApertura(null);
            entity.setHoraCierre(null);
            return;
        }
        Matcher matcher = HORARIO.matcher(horario);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(
                    "Formato de horario inválido, se esperaba 'HH:mm - HH:mm': " + horario);
        }
        try {
            entity.setHoraApertura(LocalTime.parse(matcher.group(1), HORA));
            entity.setHoraCierre(LocalTime.parse(matcher.group(2), HORA));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Horario con horas inválidas: " + horario, e);
        }
    }

    /**
     * hora_apertura / hora_cierre -> "08:00 - 22:00". Si falta alguna, no hay
     * horario que mostrar.
     */
    private String formatearHorario(LocalTime apertura, LocalTime cierre) {
        if (apertura == null || cierre == null) {
            return null;
        }
        return HORA.format(apertura) + " - " + HORA.format(cierre);
    }
}
