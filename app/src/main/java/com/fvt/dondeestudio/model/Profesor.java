package com.fvt.dondeestudio.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Profesor extends Usuario implements Serializable {

    public Profesor() {
        super();
    }

    public Profesor(String id, String nombre, String apellido, String email, String telefono) {
        super(id, nombre, apellido, email, telefono);
        this.valoracion = 3.0;
    }

    private double valoracion;

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

    public double getValoracion() {
        return valoracion;
    }

    public void setValoracion(double valoracion) {
        this.valoracion = valoracion;
    }


}
