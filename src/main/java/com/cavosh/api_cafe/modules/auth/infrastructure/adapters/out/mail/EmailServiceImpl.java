package com.cavosh.api_cafe.modules.auth.infrastructure.adapters.out.mail;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.cavosh.api_cafe.modules.auth.domain.ports.out.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String remitente;

    @Value("${app.otp.expiration-minutes:10}")
    private int minutosExpiracion;

    @Value("${app.password-reset.otp-expiration-minutes:15}")
    private int minutosExpiracionRecuperacion;

    @Override
    public void enviarCodigoVerificacion(String toEmail, String codigo) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, StandardCharsets.UTF_8.name());

            helper.setFrom(remitente, "Cavosh Café");
            helper.setTo(toEmail);
            helper.setSubject("Tu código de verificación - Cavosh Café");
            helper.setText(construirCuerpoHtml(codigo), true);

            mailSender.send(mensaje);
            log.info("Código de verificación enviado a {}", enmascarar(toEmail));

        } catch (MessagingException | MailException | java.io.UnsupportedEncodingException e) {
            log.error("Error al enviar el código de verificación a {}", enmascarar(toEmail), e);
            throw new IllegalStateException("No se pudo enviar el correo de verificación", e);
        }
    }

    @Override
    public void enviarCodigoRecuperacion(String toEmail, String codigo) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, StandardCharsets.UTF_8.name());

            helper.setFrom(remitente, "Cavosh Café");
            helper.setTo(toEmail);
            helper.setSubject("Recupera tu contraseña - Cavosh Café");
            helper.setText(construirCuerpoHtml(codigo, "Recuperaci&oacute;n de contrase&ntilde;a",
                    "Usa el siguiente c&oacute;digo para restablecer tu contrase&ntilde;a:",
                    minutosExpiracionRecuperacion), true);

            mailSender.send(mensaje);
            log.info("Código de recuperación de contraseña enviado a {}", enmascarar(toEmail));

        } catch (MessagingException | MailException | java.io.UnsupportedEncodingException e) {
            log.error("Error al enviar el código de recuperación de contraseña a {}", enmascarar(toEmail), e);
            throw new IllegalStateException("No se pudo enviar el correo de recuperación", e);
        }
    }

    private String construirCuerpoHtml(String codigo) {
        return construirCuerpoHtml(codigo, "Verificaci&oacute;n de correo electr&oacute;nico",
                "Usa el siguiente c&oacute;digo para confirmar tu direcci&oacute;n de correo:",
                minutosExpiracion);
    }

    private String construirCuerpoHtml(String codigo, String subtitulo, String instruccion, int minutos) {
        return """
                <!DOCTYPE html>
                <html lang="es">
                <body style="margin:0;padding:24px;background-color:#f4f1ec;font-family:Arial,Helvetica,sans-serif;">
                  <table role="presentation" width="100%%" cellpadding="0" cellspacing="0">
                    <tr>
                      <td align="center">
                        <table role="presentation" width="480" cellpadding="0" cellspacing="0"
                               style="background-color:#ffffff;border-radius:12px;padding:32px;">
                          <tr>
                            <td align="center" style="font-size:22px;font-weight:bold;color:#4b2e1e;padding-bottom:8px;">
                              Cavosh Caf&eacute;
                            </td>
                          </tr>
                          <tr>
                            <td align="center" style="font-size:15px;color:#555555;padding-bottom:24px;">
                              %s
                            </td>
                          </tr>
                          <tr>
                            <td align="center" style="font-size:15px;color:#333333;padding-bottom:16px;">
                              %s
                            </td>
                          </tr>
                          <tr>
                            <td align="center" style="padding-bottom:24px;">
                              <div style="display:inline-block;background-color:#f4f1ec;border-radius:8px;
                                          padding:16px 28px;font-size:32px;font-weight:bold;
                                          letter-spacing:8px;color:#4b2e1e;">%s</div>
                            </td>
                          </tr>
                          <tr>
                            <td align="center" style="font-size:14px;color:#777777;padding-bottom:8px;">
                              Este c&oacute;digo expira en <strong>%d minutos</strong>.
                            </td>
                          </tr>
                          <tr>
                            <td align="center" style="font-size:13px;color:#999999;">
                              Si no solicitaste este c&oacute;digo, puedes ignorar este mensaje.
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </table>
                </body>
                </html>
                """.formatted(subtitulo, instruccion, codigo, minutos);
    }

    /** Evita escribir el correo completo en los logs. */
    private String enmascarar(String email) {
        int arroba = email.indexOf('@');
        if (arroba <= 1) {
            return "***";
        }
        return email.charAt(0) + "***" + email.substring(arroba);
    }
}
