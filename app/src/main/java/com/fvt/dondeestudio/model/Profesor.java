package com.fvt.dondeestudio.model;

import java.util.List;

public class Profesor {

    public Profesor(String id, String email, String nombre, String apellido) {
        this.id = id;
        this.email = email;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    private String id;
    private String nombre;
    private String apellido;
    private String email;
    private Integer valoracion;
    private List<Clase> clases;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getValoracion() {
        return valoracion;
    }

    public void setValoracion(Integer valoracion) {
        this.valoracion = valoracion;
    }

    public List<Clase> getClases() {
        return clases;
    }

    public void setClases(List<Clase> clases) {
        this.clases = clases;
    }
}
