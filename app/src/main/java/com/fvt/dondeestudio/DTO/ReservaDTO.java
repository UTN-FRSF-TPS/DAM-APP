package com.fvt.dondeestudio.DTO;

import com.google.firebase.firestore.DocumentReference;

public class ReservaDTO {

    private DocumentReference idAlumno;
    private DocumentReference idProfesor;
    private DocumentReference idClase;
    private String estado;

    public DocumentReference getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(DocumentReference idAlumno) {
        this.idAlumno = idAlumno;
    }

    public DocumentReference getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(DocumentReference idProfesor) {
        this.idProfesor = idProfesor;
    }

    public DocumentReference getIdClase() {
        return idClase;
    }

    public void setIdClase(DocumentReference idClase) {
        this.idClase = idClase;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
