package com.cavosh.api_cafe.modules.usuarios.application.usecases;

import com.cavosh.api_cafe.modules.usuarios.domain.model.Direccion;
import com.cavosh.api_cafe.modules.usuarios.domain.ports.in.RegistrarDireccionCasoUso;
import com.cavosh.api_cafe.modules.usuarios.domain.ports.out.DireccionRepositorioPuerto;
import com.cavosh.api_cafe.modules.usuarios.domain.ports.out.UsuarioRepository;
import com.cavosh.api_cafe.modules.usuarios.infrastructure.adapters.in.web.dtos.DireccionRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrarDireccionCasoUsoImpl implements RegistrarDireccionCasoUso {

    private static final String ETIQUETA_POR_DEFECTO = "Casa";

    private final DireccionRepositorioPuerto direccionRepositorioPuerto;
    private final UsuarioRepository usuarioRepositorioPuerto;

    @Override
    public Direccion agregarDireccion(Long usuarioId, DireccionRequestDTO dto) {
        usuarioRepositorioPuerto.buscarPorId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

        Direccion direccion = Direccion.builder()
                .etiqueta(etiquetaOPorDefecto(dto.getEtiqueta()))
                .calle(dto.getCalle())
                .ciudad(dto.getCiudad())
                .distrito(dto.getDistrito())
                .referencia(dto.getReferencia())
                .esPrincipal(dto.getEsPrincipal() != null ? dto.getEsPrincipal() : false)
                .build();

        return direccionRepositorioPuerto.guardar(usuarioId, direccion);
    }

    @Override
    public List<Direccion> obtenerDireccionesPorUsuario(Long usuarioId) {
        return direccionRepositorioPuerto.buscarPorUsuarioId(usuarioId);
    }

    private String etiquetaOPorDefecto(String etiqueta) {
        return (etiqueta == null || etiqueta.isBlank()) ? ETIQUETA_POR_DEFECTO : etiqueta.trim();
    }
}
