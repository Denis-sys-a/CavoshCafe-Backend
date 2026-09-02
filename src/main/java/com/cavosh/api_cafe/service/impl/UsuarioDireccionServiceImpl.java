package com.cavosh.api_cafe.service.impl;

import com.cavosh.api_cafe.dto.UsuarioDireccionRequestDTO;
import com.cavosh.api_cafe.dto.UsuarioDireccionResponseDTO;
import com.cavosh.api_cafe.entity.Usuario;
import com.cavosh.api_cafe.entity.UsuarioDireccion;
import com.cavosh.api_cafe.exception.ResourceNotFoundException;
import com.cavosh.api_cafe.repository.UsuarioDireccionRepository;
import com.cavosh.api_cafe.repository.UsuarioRepository;
import com.cavosh.api_cafe.service.UsuarioDireccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioDireccionServiceImpl implements UsuarioDireccionService {

    private final UsuarioDireccionRepository usuarioDireccionRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDireccionResponseDTO> obtenerDireccionesPorUsuario(String correo) {
        Usuario usuario = obtenerUsuarioOrThrow(correo);

        return usuarioDireccionRepository.findByUsuarioId(usuario.getId()).stream()
                .map(direccion -> toResponseDTO(direccion))
                .collect(Collectors.toList());
    }

    @Override
    public UsuarioDireccionResponseDTO crearDireccion(String correo, UsuarioDireccionRequestDTO dto) {
        Usuario usuario = obtenerUsuarioOrThrow(correo);

        if (dto.isEsPredeterminada()) {
            usuarioDireccionRepository.findByUsuarioIdAndEsPredeterminadaTrue(usuario.getId())
                    .ifPresent(direccionAnterior -> {
                        direccionAnterior.setEsPredeterminada(false);
                        usuarioDireccionRepository.save(direccionAnterior);
                    });
        }

        UsuarioDireccion nuevaDireccion = UsuarioDireccion.builder()
                .usuario(usuario)
                .etiqueta(dto.getEtiqueta())
                .direccion(dto.getDireccion())
                .latitud(dto.getLatitud())
                .longitud(dto.getLongitud())
                .esPredeterminada(dto.isEsPredeterminada())
                .build();

        nuevaDireccion = usuarioDireccionRepository.save(nuevaDireccion);

        return toResponseDTO(nuevaDireccion);
    }

    @Override
    public void eliminarDireccion(String correo, Long direccionId) {
        Usuario usuario = obtenerUsuarioOrThrow(correo);

        UsuarioDireccion direccion = usuarioDireccionRepository.findById(direccionId)
                .filter(d -> d.getUsuario().getId().equals(usuario.getId()))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Dirección no encontrada con id: " + direccionId));

        usuarioDireccionRepository.delete(direccion);
    }

    private Usuario obtenerUsuarioOrThrow(String correo) {
        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con correo: " + correo));
    }

    private UsuarioDireccionResponseDTO toResponseDTO(UsuarioDireccion direccion) {
        return UsuarioDireccionResponseDTO.builder()
                .id(direccion.getId())
                .etiqueta(direccion.getEtiqueta())
                .direccion(direccion.getDireccion())
                .latitud(direccion.getLatitud())
                .longitud(direccion.getLongitud())
                .esPredeterminada(direccion.isEsPredeterminada())
                .build();
    }
}