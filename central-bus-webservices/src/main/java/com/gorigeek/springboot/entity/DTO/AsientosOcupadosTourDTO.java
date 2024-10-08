package com.gorigeek.springboot.entity.DTO;

public class AsientosOcupadosTourDTO {
    
private Long idTerminalOrigen;
    
    private Long idTerminalDestino;
    
    private Long idTour;
    
    private Long idAutobus;
    
    private int numeroAsiento;
    
    private String fechaHoraViaje;
    
   // private int statusCancelado;
    
    private Long tipoAsiento;
    
    private String asientoTemp;
    
    

    public AsientosOcupadosTourDTO() {
        super();
    }



   



    public AsientosOcupadosTourDTO(Long idTerminalOrigen, Long idTerminalDestino, Long idRuta, Long idAutobus,
            int numeroAsiento, String fechaHoraViaje,  Long tipoAsiento, String asientoTemp) {
        super();
        this.idTerminalOrigen = idTerminalOrigen;
        this.idTerminalDestino = idTerminalDestino;
        this.idTour = idRuta;
        this.idAutobus = idAutobus;
        this.numeroAsiento = numeroAsiento;
        this.fechaHoraViaje = fechaHoraViaje;
       // this.statusCancelado = statusCancelado;
        this.tipoAsiento = tipoAsiento;
        this.asientoTemp = asientoTemp;
    }



    public Long getIdTerminalOrigen() {
        return idTerminalOrigen;
    }



    public void setIdTerminalOrigen(Long idTerminalOrigen) {
        this.idTerminalOrigen = idTerminalOrigen;
    }



    public Long getIdTerminalDestino() {
        return idTerminalDestino;
    }



    public void setIdTerminalDestino(Long idTerminalDestino) {
        this.idTerminalDestino = idTerminalDestino;
    }



    public Long getIdTour() {
        return idTour;
    }



    public void setIdTour(Long idRuta) {
        this.idTour = idRuta;
    }



    public Long getIdAutobus() {
        return idAutobus;
    }



    public void setIdAutobus(Long idAutobus) {
        this.idAutobus = idAutobus;
    }



    public int getNumeroAsiento() {
        return numeroAsiento;
    }



    public void setNumeroAsiento(int numeroAsiento) {
        this.numeroAsiento = numeroAsiento;
    }



    public String getFechaHoraViaje() {
        return fechaHoraViaje;
    }



    public void setFechaHoraViaje(String fechaHoraViaje) {
        this.fechaHoraViaje = fechaHoraViaje;
    }


    public Long getTipoAsiento() {
        return tipoAsiento;
    }







    public void setTipoAsiento(Long tipoAsiento) {
        this.tipoAsiento = tipoAsiento;
    }







    public String getAsientoTemp() {
        return asientoTemp;
    }







    public void setAsientoTemp(String asientoTemp) {
        this.asientoTemp = asientoTemp;
    }
    
    
    
    

}
