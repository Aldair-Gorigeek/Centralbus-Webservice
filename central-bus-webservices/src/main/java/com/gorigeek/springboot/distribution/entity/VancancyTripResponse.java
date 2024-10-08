package com.gorigeek.springboot.distribution.entity;

public class VancancyTripResponse {
    private boolean vacant;
    private boolean supportsSeats;
    private Integer numberOfTypesOfPassenger;
    private Double pint; // Infant
    private Double pcil; // Child
    private Double pypo; // Youth
    private Double pnos; // Adult
    private Double psoe; // Senior

    
    public boolean isSupportsSeats() {
        return supportsSeats;
    }

    public void setSupportsSeats(boolean supportsSeats) {
        this.supportsSeats = supportsSeats;
    }

    public Integer getNumberOfTypesOfPassenger() {
        return numberOfTypesOfPassenger;
    }

    public void setNumberOfTypesOfPassenger(Integer numberOfTypesOfPassenger) {
        this.numberOfTypesOfPassenger = numberOfTypesOfPassenger;
    }

    public Double getPint() {
        return pint;
    }

    public void setPint(Double pint) {
        this.pint = pint;
    }

    public Double getPcil() {
        return pcil;
    }

    public void setPcil(Double pcil) {
        this.pcil = pcil;
    }

    public Double getPypo() {
        return pypo;
    }

    public void setPypo(Double pypo) {
        this.pypo = pypo;
    }

    public Double getPnos() {
        return pnos;
    }

    public void setPnos(Double pnos) {
        this.pnos = pnos;
    }

    public Double getPsoe() {
        return psoe;
    }

    public void setPsoe(Double psoe) {
        this.psoe = psoe;
    }

    public boolean isVacant() {
        return vacant;
    }

    public void setVacant(boolean vacant) {
        this.vacant = vacant;
    }

}
