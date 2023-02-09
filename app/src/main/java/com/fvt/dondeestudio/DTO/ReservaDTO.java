package com.fvt.dondeestudio.DTO;

public class ReservaDTO {

    private String idAlumno;
    private String idProfesor;
    private String idClase;
    private String estado;

    public String getIdAlumno() {
        return idAlumno;
    }

    public String getEstado() {
        return estado;
    }

    public  void setEstado(String estado) {
       this.estado = estado;
    }

    public void setIdAlumno(String idAlumno) {
        this.idAlumno = idAlumno;
    }

    public String getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(String idProfesor) {
        this.idProfesor = idProfesor;
    }

    public String getIdClase() {
        return idClase;
    }

    public void setIdClase(String idClase) {
        this.idClase = idClase;
    }
}
