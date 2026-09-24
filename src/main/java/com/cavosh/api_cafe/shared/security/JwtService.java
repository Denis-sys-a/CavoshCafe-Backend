package com.cavosh.api_cafe.shared.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.cavosh.api_cafe.modules.usuarios.domain.model.Usuario;
import com.cavosh.api_cafe.shared.security.exception.JwtAuthenticationException;
import com.cavosh.api_cafe.shared.security.exception.JwtTokenExpiradoException;
import com.cavosh.api_cafe.shared.security.exception.JwtTokenInvalidoException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio utilitario de JWT: se encarga de generar Access Tokens y de
 * validar/leer los tokens recibidos en las peticiones.
 *
 * La clave de firma se deriva de {@code app.jwt.secret} (ver
 * {@link JwtProperties}) usando el algoritmo HMAC-SHA256.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtService {

    private static final String CLAIM_USER_ID = "userId";
    private static final String CLAIM_ROL = "rol";

    private final JwtProperties jwtProperties;

    // ------------------------------------------------------------------
    // Generación de tokens
    // ------------------------------------------------------------------

    /**
     * Genera un Access Token a partir de un {@link Usuario} del dominio.
     * El subject del token es el correo del usuario; se agregan además
     * los claims "userId" y "rol" para poder reconstruir el contexto de
     * seguridad sin volver a consultar la base de datos en cada petición.
     */
    public String generarAccessToken(Usuario usuario) {
        return generarAccessToken(usuario.getEmail(), usuario.getId(), usuario.getRol());
    }

    /**
     * Igual que {@link #generarAccessToken(Usuario)} pero recibiendo los
     * datos sueltos, útil cuando aún no se tiene una instancia completa de
     * {@link Usuario} (p. ej. pruebas unitarias) o el subject no es un
     * correo.
     */
    public String generarAccessToken(String subject, Long userId, String rol) {
        Date fechaEmision = new Date();
        Date fechaExpiracion = new Date(fechaEmision.getTime() + jwtProperties.getExpirationMs());

        return Jwts.builder()
                .subject(subject)
                .claim(CLAIM_USER_ID, userId)
                .claim(CLAIM_ROL, rol)
                .issuedAt(fechaEmision)
                .expiration(fechaExpiracion)
                .signWith(getSigningKey())
                .compact();
    }

    // ------------------------------------------------------------------
    // Lectura de claims
    // ------------------------------------------------------------------

    /** Extrae el subject (correo/ID) del token. */
    public String extraerUsername(String token) {
        return extraerClaim(token, Claims::getSubject);
    }

    /** Extrae el ID del usuario embebido en el claim "userId". */
    public Long extraerUserId(String token) {
        Number userId = extraerClaim(token, claims -> claims.get(CLAIM_USER_ID, Number.class));
        return userId != null ? userId.longValue() : null;
    }

    /** Extrae el rol embebido en el claim "rol". */
    public String extraerRol(String token) {
        return extraerClaim(token, claims -> claims.get(CLAIM_ROL, String.class));
    }

    /** Extrae la fecha de expiración del token. */
    public Date extraerExpiracion(String token) {
        return extraerClaim(token, Claims::getExpiration);
    }

    /** Extrae un claim arbitrario usando un resolver funcional. */
    public <T> T extraerClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extraerTodosLosClaims(token);
        return resolver.apply(claims);
    }

    // ------------------------------------------------------------------
    // Validación
    // ------------------------------------------------------------------

    /**
     * Valida que el token tenga una firma correcta y no haya expirado.
     * No lanza excepciones: retorna {@code false} ante cualquier problema
     */
    public boolean esTokenValido(String token) {
        try {
            extraerTodosLosClaims(token);
            return true;
        } catch (JwtAuthenticationException e) {
            return false;
        }
    }

    /**
     * Valida el token y además verifica que el subject coincida con el
     * username esperado (p. ej. el cargado por UserDetailsService).
     */
    public boolean esTokenValido(String token, String usernameEsperado) {
        if (usernameEsperado == null) {
            return false;
        }
        try {
            String username = extraerUsername(token);
            return usernameEsperado.equalsIgnoreCase(username);
        } catch (JwtAuthenticationException e) {
            return false;
        }
    }

    // ------------------------------------------------------------------
    // Internos
    // ------------------------------------------------------------------

    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Parsea y valida la firma del token, mapeando cada excepción de la
     * librería jjwt a una excepción propia y entendible. JJWT valida la
     * expiración durante el parseo, por lo que un token vencido cae en el
     * catch de {@link ExpiredJwtException} aunque su firma sea correcta.
     */
    private Claims extraerTodosLosClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            log.warn("Token JWT expirado");
            throw new JwtTokenExpiradoException("El token ha expirado, por favor inicia sesión nuevamente", e);
        } catch (SignatureException e) {
            log.warn("Firma de token JWT inválida (posible manipulación)");
            throw new JwtTokenInvalidoException("La firma del token no es válida", e);
        } catch (MalformedJwtException e) {
            log.warn("Token JWT malformado");
            throw new JwtTokenInvalidoException("El token JWT está malformado", e);
        } catch (UnsupportedJwtException e) {
            log.warn("Token JWT no soportado");
            throw new JwtTokenInvalidoException("El formato del token JWT no es soportado", e);
        } catch (IllegalArgumentException e) {
            log.warn("Token JWT nulo o vacío");
            throw new JwtTokenInvalidoException("El token JWT no puede estar vacío", e);
        }
    }
}
