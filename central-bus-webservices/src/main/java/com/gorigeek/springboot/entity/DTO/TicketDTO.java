package com.gorigeek.springboot.entity.DTO;

public class TicketDTO {
    
    private Long id;
    
    private boolean isTour;
    
    private String nombrePasajero;
    
    private String origen;
    
    private String destino;
    
    private String fecha;
    
    private String hora;
    
    private int status;        

    public TicketDTO() {}

    public TicketDTO(Long id, boolean isTour, String nombrePasajero, String origen, String destino, String fecha,
            String hora, int status) {
        super();
        this.id = id;
        this.isTour = isTour;
        this.nombrePasajero = nombrePasajero;
        this.origen = origen;
        this.destino = destino;
        this.fecha = fecha;
        this.hora = hora;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isTour() {
        return isTour;
    }

    public void setTour(boolean isTour) {
        this.isTour = isTour;
    }

    public String getNombrePasajero() {
        return nombrePasajero;
    }

    public void setNombrePasajero(String nombrePasajero) {
        this.nombrePasajero = nombrePasajero;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }      
}
