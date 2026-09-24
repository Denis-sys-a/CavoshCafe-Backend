package com.cavosh.api_cafe.modules.usuarios.infrastructure.adapters.in.web.dtos;

import com.cavosh.api_cafe.modules.usuarios.domain.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {

    private Long id;
    private String fullName;
    private String email;
    private String telefono;
    private String rol;

    /** Construye el DTO seguro a partir del modelo de dominio. */
    public static UsuarioResponseDTO fromDomain(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        return UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .fullName(usuario.getFullName())
                .email(usuario.getEmail())
                .telefono(usuario.getTelefono())
                .rol(usuario.getRol())
                .build();
    }
}