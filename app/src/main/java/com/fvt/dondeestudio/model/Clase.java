package com.fvt.dondeestudio.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Clase {

    private String id;
    private Profesor profesor;
    private Double tarifaHora;
    private Integer cupo;
    private Integer cupoActual;
    private Nivel nivel;
    private LatLng ubicacion;
    private String asignatura;
    private Integer valoracion;
    private Date horario;
    private String estadoUsuario;

    public Clase(){};

    public String getEstadoUsuario() {
        return estadoUsuario;
    }

    public void setEstadoUsuario(String estadoUsuario) {
        this.estadoUsuario = estadoUsuario;
    }

    public Clase(Profesor profesor, Double tarifaHora, Integer cupo, Nivel nivel, LatLng ubicacion, String asignatura, Integer valoracion, Date horario) {
        this.profesor = profesor;
        this.tarifaHora = tarifaHora;
        this.cupo = cupo;
        this.nivel = nivel;
        this.ubicacion = ubicacion;
        this.asignatura = asignatura;
        this.valoracion = valoracion;
        ArrayList<Valoracion> valoraciones;
        this.horario = horario;
        this.cupoActual = 0;
    }

    public Clase(String id, Double tarifa, String asignatura){
        this.id = id;
        this.tarifaHora =tarifa;
        this.asignatura = asignatura;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }


    public Double getTarifaHora() {
        return tarifaHora;
    }

    public void setTarifaHora(Double tarifaHora) {
        this.tarifaHora = tarifaHora;
    }

    public Integer getCupo() {
        return cupo;
    }

    public void setCupo(Integer cupo) {
        this.cupo = cupo;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public LatLng getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(LatLng ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getId() {
        return id;
    }

    public String getAsignatura() {
        return asignatura;
    }

    public Integer getValoracion() {
        return valoracion;
    }

    public Date getHorario() {
        return horario;
    }

    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }

    public void setID(String id) {
        this.id = id;
    }
}
