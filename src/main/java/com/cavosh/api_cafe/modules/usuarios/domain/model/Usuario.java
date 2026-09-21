package com.cavosh.api_cafe.modules.usuarios.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    private Long id;
    private String fullName;
    private String email;
    private String password;
    private String telefono;
    private String rol;

    @Builder.Default
    private AuthProvider authProvider = AuthProvider.LOCAL;
    private Boolean isVerified;

    @Builder.Default
    private List<Direccion> direcciones = new ArrayList<>();
}