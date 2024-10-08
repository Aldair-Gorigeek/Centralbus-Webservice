package com.gorigeek.springboot.distribution.entity;

public class CiudadDistribution {

    private String idc_ciudades;
    private String descripcion;
    private String idEstado;

    public String getIdc_ciudades() {
        return idc_ciudades;
    }

    public void setIdc_ciudades(String idc_ciudades) {
        this.idc_ciudades = idc_ciudades;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(String idEstado) {
        this.idEstado = idEstado;
    }
}
