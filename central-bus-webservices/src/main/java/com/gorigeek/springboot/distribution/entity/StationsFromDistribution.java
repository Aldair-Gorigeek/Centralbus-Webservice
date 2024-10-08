package com.gorigeek.springboot.distribution.entity;

public class StationsFromDistribution {
    private String idTerminal;
    private String nombreterminal;
    private String direccion;
    private CiudadDistribution ciudad;
    private Carrier carrier;

    public String getIdTerminal() {
        return idTerminal;
    }

    public void setIdTerminal(String idTerminal) {
        this.idTerminal = idTerminal;
    }

    public String getNombreterminal() {
        return nombreterminal;
    }

    public void setNombreterminal(String nombreterminal) {
        this.nombreterminal = nombreterminal;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public CiudadDistribution getCiudad() {
        return ciudad;
    }

    public void setCiudad(CiudadDistribution ciudad) {
        this.ciudad = ciudad;
    }

    public Carrier getCarrier() {
        return carrier;
    }

    public void setCarrier(Carrier carrier) {
        this.carrier = carrier;
    }

}
