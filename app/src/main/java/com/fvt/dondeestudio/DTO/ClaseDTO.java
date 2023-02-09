package com.fvt.dondeestudio.DTO;

public class ClaseDTO {

    private Double radioMaxMetros;
    private Double tarifaHoraMax;
    private String nivel;
    private String asignatura;
    private Integer valoracionProfesor;


public ClaseDTO(){

};

    public Double getRadioMaxMetros() {
        return radioMaxMetros;
    }

    public void setRadioMaxMetros(Double radioMaxMetros) {
        this.radioMaxMetros = radioMaxMetros;
    }

    public Double getTarifaHoraMax() {
        return tarifaHoraMax;
    }

    public void setTarifaHoraMax(Double tarifaHoraMax) {
        this.tarifaHoraMax = tarifaHoraMax;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }

    public Integer getValoracionProfesor() {
        return valoracionProfesor;
    }

    public void setValoracionProfesor(Integer valoracionProfesor) {
        this.valoracionProfesor = valoracionProfesor;
    }
}
