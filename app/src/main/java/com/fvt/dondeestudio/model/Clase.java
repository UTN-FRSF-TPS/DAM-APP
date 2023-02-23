package com.fvt.dondeestudio.model;

import android.os.Build;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Clase implements Serializable, Comparable<Clase>
 {

    private String id;
    private Profesor profesor;
    private Double tarifaHora;
    private Nivel nivel;
    private transient GeoPoint ubicacion;
    private String asignatura;
    private Long cupo;
    private Integer valoracion;
    private String horario;
    private String estadoUsuario;
    private String tipo;
    private Boolean tieneRetroalimentacion; //Solo validos para card view
    private Boolean reservada; //Solo validos para card view
     @Exclude
     private ArrayList<HashMap<String, Object>> valoraciones;

    public Clase(){};

    public String getEstadoUsuario() {
        return estadoUsuario;
    }

    public void setEstadoUsuario(String estadoUsuario) {
        this.estadoUsuario = estadoUsuario;
    }

    public Clase(Profesor profesor, Double tarifaHora, Long cupo, Nivel nivel, GeoPoint ubicacion, String asignatura, Integer valoracion, String horario) {
        this.profesor = profesor;
        this.tarifaHora = tarifaHora;
        this.cupo = cupo;
        this.nivel = nivel;
        this.ubicacion = ubicacion;
        this.asignatura = asignatura;
        this.valoracion = valoracion;
        ArrayList<Valoracion> valoraciones;
        this.horario = horario;
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

    public Long getCupo() {
        return cupo;
    }

    public void setCupo(Long cupo) {
        this.cupo = cupo;
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

    public Boolean getTieneRetroalimentacion() {
        return tieneRetroalimentacion;
    }

    public void setTieneRetroalimentacion(Boolean tieneRetroalimentacion) {
        this.tieneRetroalimentacion = tieneRetroalimentacion;
    }

    public Boolean getReservada() {
        return reservada;
    }

    public void setReservada(Boolean reservada) {
        this.reservada = reservada;
    }

     @Override
     public int compareTo(Clase otra) {
         DateTimeFormatter formatter = null;
         LocalDateTime fechaHora = null;
         LocalDateTime otraFechaHora = null;
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
             fechaHora = LocalDateTime.parse(this.horario, formatter);
             otraFechaHora = LocalDateTime.parse(otra.horario, formatter);
         }
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
             return otraFechaHora.compareTo(fechaHora);
         return 0;
     }
 }
