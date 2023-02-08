package com.fvt.dondeestudio.model;

public class Reserva {

    private Alumno alumno; //es el user_id del alumno
    private Clase clase; //es la clase_id de la clase
    private Boolean leido; //Cuando se hace click en la notificacion deberia marcarse leido=true
    private Profesor profesor; //deberia ser el profesor de la clase, lo pongo aca para q sea mas facil la consulta
    public Reserva(String p1, String p2){

    }

    public Reserva(String idAlumno, Clase clase, Boolean leido, Profesor profesor) {
        this.alumno = alumno;
        this.clase = clase;
        this.leido = leido;
        this.profesor = profesor;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public Clase getClase() {
        return clase;
    }

    public void setClase(Clase clase) {
        this.clase = clase;
    }

    public Boolean getLeido() {
        return leido;
    }

    public void setLeido(Boolean leido) {
        this.leido = leido;
    }
}

