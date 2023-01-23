package com.fvt.dondeestudio.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.List;

public class Clase {

    private String id;
    private Profesor profesor;
    private List<Alumno> alumnos;
    private Double tarifaHora;
    private Integer cupo;
    private Nivel nivel;
    private LatLng ubicacion;
    private String asignatura;
    private Integer valoracion;
    private Date horario;


    public Clase(Profesor profesor, List<Alumno> alumnos, Double tarifaHora, Integer cupo, Nivel nivel, LatLng ubicacion, String asignatura, Integer valoracion, Date horario) {
        this.profesor = profesor;
        this.alumnos = alumnos;
        this.tarifaHora = tarifaHora;
        this.cupo = cupo;
        this.nivel = nivel;
        this.ubicacion = ubicacion;
        this.asignatura = asignatura;
        this.valoracion = valoracion;
        this.horario = horario;
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

    public List<Alumno> getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(List<Alumno> alumnos) {
        this.alumnos = alumnos;
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
}
