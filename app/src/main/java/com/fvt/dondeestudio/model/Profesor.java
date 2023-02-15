package com.fvt.dondeestudio.model;

import java.util.ArrayList;
import java.util.List;

public class Profesor extends Usuario {

    public Profesor() {
        super();
    }

    public Profesor(String id, String nombre, String apellido, String email) {
        super(id, nombre, apellido, email);
    }

    private Integer valoracion;

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


}
