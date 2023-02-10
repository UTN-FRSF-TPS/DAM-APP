package com.fvt.dondeestudio.model;

public class Valoracion {
    Long puntaje;
    String usuarioId;
    public Valoracion(){};
    public Valoracion(Long puntaje, String usuarioId) {
        this.puntaje = puntaje;
        this.usuarioId = usuarioId;
    }

    public Long getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(Long puntaje) {
        this.puntaje = puntaje;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }
}
