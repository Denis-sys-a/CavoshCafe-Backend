package com.cavosh.api_cafe.modules.notificaciones.domain.model;

import java.time.LocalDateTime;

public class Notificacion {
    private Long id;
    private Long usuarioId;
    private String titulo;
    private String mensaje;
    private TipoNotificacion tipo;
    private EstadoNotificacion estado;
    private LocalDateTime fechaEnvio;

    public Notificacion() {}

    public Notificacion(Long id, Long usuarioId, String titulo, String mensaje, 
                        TipoNotificacion tipo, EstadoNotificacion estado, LocalDateTime fechaEnvio) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.tipo = tipo;
        this.estado = estado;
        this.fechaEnvio = fechaEnvio;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public TipoNotificacion getTipo() { return tipo; }
    public void setTipo(TipoNotificacion tipo) { this.tipo = tipo; }

    public EstadoNotificacion getEstado() { return estado; }
    public void setEstado(EstadoNotificacion estado) { this.estado = estado; }

    public LocalDateTime getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(LocalDateTime fechaEnvio) { this.fechaEnvio = fechaEnvio; }
}