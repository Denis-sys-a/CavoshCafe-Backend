package com.cavosh.api_cafe.shared.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cavosh.api_cafe.modules.usuarios.domain.model.Usuario;

import lombok.Getter;

/**
 * Adaptador entre el modelo de dominio {@link Usuario} y el contrato
 * {@link UserDetails} que Spring Security necesita para autenticar y
 * autorizar peticiones.
 *
 * Se implementa como una clase propia (en vez de usar directamente
 * UsuarioEntity) para no acoplar la capa de seguridad a la capa de
 * persistencia, respetando la arquitectura hexagonal del proyecto.
 */
public class CustomUserDetails implements UserDetails {

    private static final String ROL_POR_DEFECTO = "CLIENTE";
    private static final String PREFIJO_ROL = "ROLE_";

    /**
     * ID interno del usuario (útil para no volver a consultar por correo en cada
     * request).
     */
    @Getter
    private final Long id;

    private final String email;

    @Getter
    private final String fullName;

    private final String password;
    private final boolean enabled;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Usuario usuario) {
        this.id = usuario.getId();
        this.email = usuario.getEmail();
        this.fullName = usuario.getFullName();
        this.password = usuario.getPassword();
        // Un usuario sin verificar no debería poder autenticarse vía Spring
        // Security (aunque el caso de uso de login ya valida esto también
        // de forma explícita para dar un mensaje de error más claro).
        this.enabled = Boolean.TRUE.equals(usuario.getIsVerified());
        this.authorities = List.of(new SimpleGrantedAuthority(construirNombreAuthority(usuario.getRol())));
    }

    private static String construirNombreAuthority(String rol) {
        String rolNormalizado = (rol == null || rol.isBlank())
                ? ROL_POR_DEFECTO
                : rol.trim().toUpperCase();
        return rolNormalizado.startsWith(PREFIJO_ROL) ? rolNormalizado : PREFIJO_ROL + rolNormalizado;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
