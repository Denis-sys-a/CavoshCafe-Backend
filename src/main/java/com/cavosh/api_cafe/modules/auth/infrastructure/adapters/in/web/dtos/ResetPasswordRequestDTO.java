package com.cavosh.api_cafe.modules.auth.infrastructure.adapters.in.web.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequestDTO {

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El formato de correo no es válido")
    private String email;

    @NotBlank(message = "El token de reseteo es obligatorio")
    private String resetToken;

    @NotBlank(message = "La nueva contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String newPassword;
}
