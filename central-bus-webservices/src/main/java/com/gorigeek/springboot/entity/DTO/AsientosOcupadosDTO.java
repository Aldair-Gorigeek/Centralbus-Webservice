package com.gorigeek.springboot.entity.DTO;

public class AsientosOcupadosDTO {
    
    private Long idTerminalOrigen;
    
    private Long idTerminalDestino;
    
    private Long idRuta;
    
    private Long idAutobus;
    
    private int numeroAsiento;
    
    private String fechaHoraViaje;
    
    private int statusCancelado;
    
    private Long tipoAsiento;
    
    private String asientoTemp;
    
    private Integer tipoPlanta;
    

    public AsientosOcupadosDTO() {
        super();
    }



   



    public AsientosOcupadosDTO(Long idTerminalOrigen, Long idTerminalDestino, Long idRuta, Long idAutobus,
            int numeroAsiento, String fechaHoraViaje, int statusCancelado, Long tipoAsiento, String asientoTemp, Integer tipoPlanta) {
        super();
        this.idTerminalOrigen = idTerminalOrigen;
        this.idTerminalDestino = idTerminalDestino;
        this.idRuta = idRuta;
        this.idAutobus = idAutobus;
        this.numeroAsiento = numeroAsiento;
        this.fechaHoraViaje = fechaHoraViaje;
        this.statusCancelado = statusCancelado;
        this.tipoAsiento = tipoAsiento;
        this.asientoTemp = asientoTemp;
        this.tipoPlanta = tipoPlanta;
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







    public Long getIdRuta() {
        return idRuta;
    }







    public void setIdRuta(Long idRuta) {
        this.idRuta = idRuta;
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







    public int getStatusCancelado() {
        return statusCancelado;
    }







    public void setStatusCancelado(int statusCancelado) {
        this.statusCancelado = statusCancelado;
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







    public Integer getTipoPlanta() {
        return tipoPlanta;
    }







    public void setTipoPlanta(Integer tipoPlanta) {
        this.tipoPlanta = tipoPlanta;
    }


}
