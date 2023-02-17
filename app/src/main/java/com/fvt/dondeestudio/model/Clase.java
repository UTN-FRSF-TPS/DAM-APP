package com.fvt.dondeestudio.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.GeoPoint;

import java.time.LocalDateTime;
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
    private GeoPoint ubicacion;
    private String asignatura;
    private Integer valoracion;
    private String horario;
    private String estadoUsuario;
    private String tipo;

    public Clase(){};

    public String getEstadoUsuario() {
        return estadoUsuario;
    }

    public void setEstadoUsuario(String estadoUsuario) {
        this.estadoUsuario = estadoUsuario;
    }

    public Clase(Profesor profesor, Double tarifaHora, Integer cupo, Nivel nivel, GeoPoint ubicacion, String asignatura, Integer valoracion, String horario) {
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


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Integer getCupoActual() {
        return cupoActual;
    }

    public void setCupoActual(Integer cupoActual) {
        this.cupoActual = cupoActual;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public GeoPoint getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(GeoPoint ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }

    public Integer getValoracion() {
        return valoracion;
    }

    public void setValoracion(Integer valoracion) {
        this.valoracion = valoracion;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
