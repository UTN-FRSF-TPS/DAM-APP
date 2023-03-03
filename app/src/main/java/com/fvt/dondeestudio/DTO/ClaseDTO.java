package com.fvt.dondeestudio.DTO;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class ClaseDTO implements Serializable {

    private Double radioMaxMetros;
    private Double tarifaHoraMax;
    private String nivel;
    private String asignatura;
    private Integer valoracionProfesor;
    private String tipo;
    private transient LatLng ubicacion;


public ClaseDTO(){

};

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String presencial) {
        this.tipo = presencial;
    }

    public Double getRadioMaxMetros() {
        return radioMaxMetros;
    }

    public void setRadioMaxMetros(Double radioMaxMetros) {
        this.radioMaxMetros = radioMaxMetros;
    }

    public Double getTarifaHoraMax() {
        return tarifaHoraMax;
    }

    public void setTarifaHoraMax(Double tarifaHoraMax) {
        this.tarifaHoraMax = tarifaHoraMax;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }

    public Integer getValoracionProfesor() {
        return valoracionProfesor;
    }

    public void setValoracionProfesor(Integer valoracionProfesor) {
        this.valoracionProfesor = valoracionProfesor;
    }

    public LatLng getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(LatLng ubicacion) {
        this.ubicacion = ubicacion;
    }
}
