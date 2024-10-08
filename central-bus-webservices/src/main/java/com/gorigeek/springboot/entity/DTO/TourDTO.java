package com.gorigeek.springboot.entity.DTO;

import java.util.List;

import com.gorigeek.springboot.entity.ImagenesTour;

public class TourDTO {
    private Long idTour;
    
    private String estadoOrigen;
    
    private String ciudadOrigen;
    
    private String estadoDestino;
    
    private String ciudadDestino;
    
    private String fechaSalida;
    
    private String horaSalida;
    
    private String fechaRegreso;
    
    private String horaRegreso;    
   
    private String paradas;
    
    private String nombreTour;
    
    private List<ImagenesTour> imagenesTour;
    
    
    
    public TourDTO() {
        
    }



    public TourDTO(Long idTour, String estadoOrigen, String ciudadOrigen, String estadoDestino, String ciudadDestino,
            String fechaSalida, String horaSalida, String fechaRegreso, String horaRegreso, String paradas,
            String nombreTour, List<ImagenesTour> imagenesTour) {
        super();
        this.idTour = idTour;
        this.estadoOrigen = estadoOrigen;
        this.ciudadOrigen = ciudadOrigen;
        this.estadoDestino = estadoDestino;
        this.ciudadDestino = ciudadDestino;
        this.fechaSalida = fechaSalida;
        this.horaSalida = horaSalida;
        this.fechaRegreso = fechaRegreso;
        this.horaRegreso = horaRegreso;
        this.paradas = paradas;
        this.nombreTour = nombreTour;
        this.imagenesTour = imagenesTour;
    }



    public Long getIdTour() {
        return idTour;
    }



    public void setIdTour(Long idTour) {
        this.idTour = idTour;
    }



    public String getEstadoOrigen() {
        return estadoOrigen;
    }



    public void setEstadoOrigen(String estadoOrigen) {
        this.estadoOrigen = estadoOrigen;
    }



    public String getCiudadOrigen() {
        return ciudadOrigen;
    }



    public void setCiudadOrigen(String ciudadOrigen) {
        this.ciudadOrigen = ciudadOrigen;
    }



    public String getEstadoDestino() {
        return estadoDestino;
    }



    public void setEstadoDestino(String estadoDestino) {
        this.estadoDestino = estadoDestino;
    }



    public String getCiudadDestino() {
        return ciudadDestino;
    }



    public void setCiudadDestino(String ciudadDestino) {
        this.ciudadDestino = ciudadDestino;
    }



    public String getFechaSalida() {
        return fechaSalida;
    }



    public void setFechaSalida(String fechaSalida) {
        this.fechaSalida = fechaSalida;
    }



    public String getHoraSalida() {
        return horaSalida;
    }



    public void setHoraSalida(String horaSalida) {
        this.horaSalida = horaSalida;
    }



    public String getFechaRegreso() {
        return fechaRegreso;
    }



    public void setFechaRegreso(String fechaRegreso) {
        this.fechaRegreso = fechaRegreso;
    }



    public String getHoraRegreso() {
        return horaRegreso;
    }



    public void setHoraRegreso(String horaRegreso) {
        this.horaRegreso = horaRegreso;
    }



    public String getParadas() {
        return paradas;
    }



    public void setParadas(String paradas) {
        this.paradas = paradas;
    }



    public String getNombreTour() {
        return nombreTour;
    }



    public void setNombreTour(String nombreTour) {
        this.nombreTour = nombreTour;
    }



    public List<ImagenesTour> getImagenesTour() {
        return imagenesTour;
    }



    public void setImagenesTour(List<ImagenesTour> imagenesTour) {
        this.imagenesTour = imagenesTour;
    }
    
    




    

    

}
