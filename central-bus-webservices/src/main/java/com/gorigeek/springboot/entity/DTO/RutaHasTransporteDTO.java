package com.gorigeek.springboot.entity.DTO;

public class RutaHasTransporteDTO {

    private Long idRutaTransporte;
    
    private String terminalOrigen;
    
    private String terminalDestino;
    
    private Long idTerminalOrigen;
    
    private Long idTerminalDestino;  
    
    private String horaSalida;
    
    private String logo;
    
    private Long idAutobus;
    
    private int cantAdultos;
    
    private int cantNinos;
    
    private int cantInapam;
    
    private int sanitario;
    
    private Long tipoTrasporte;
    
    private double precioAdultos;
    
    private double precioNinos;
    
    private double precioInapam;
    
    private Long disponibilidadAutobusLong;
    
    private Long idRuta;
    
    

    public RutaHasTransporteDTO() {
        super();
    }



    public RutaHasTransporteDTO(Long idRutaTransporte, String terminalOrigen, String terminalDestino, Long idTerminalOrigen,
            Long idTerminalDestino, String horaSalida, String logo, Long idAutobus, int cantAdultos, int cantNinos,
            int cantInapam, int sanitario, Long tipoTrasporte, double precioAdultos, double precioNinos,
            double precioInapam, Long disponibilidadAutobusLong, Long idRuta) {
        super();
        this.idRutaTransporte = idRutaTransporte;
        this.terminalOrigen = terminalOrigen;
        this.terminalDestino = terminalDestino;
        this.idTerminalOrigen = idTerminalOrigen;
        this.idTerminalDestino = idTerminalDestino;
        this.horaSalida = horaSalida;
        this.logo = logo;
        this.idAutobus = idAutobus;
        this.cantAdultos = cantAdultos;
        this.cantNinos = cantNinos;
        this.cantInapam = cantInapam;
        this.sanitario = sanitario;
        this.tipoTrasporte = tipoTrasporte;
        this.precioAdultos = precioAdultos;
        this.precioNinos = precioNinos;
        this.precioInapam = precioInapam;
        this.disponibilidadAutobusLong = disponibilidadAutobusLong;
        this.idRuta = idRuta;
    }



    public Long getIdRutaTransporte() {
        return idRutaTransporte;
    }



    public void setIdRutaTransporte(Long idRutaTransporte) {
        this.idRutaTransporte = idRutaTransporte;
    }



    public String getTerminalOrigen() {
        return terminalOrigen;
    }



    public void setTerminalOrigen(String terminalOrigen) {
        this.terminalOrigen = terminalOrigen;
    }



    public String getTerminalDestino() {
        return terminalDestino;
    }



    public void setTerminalDestino(String terminalDestino) {
        this.terminalDestino = terminalDestino;
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



    public String getHoraSalida() {
        return horaSalida;
    }



    public void setHoraSalida(String horaSalida) {
        this.horaSalida = horaSalida;
    }



    public String getLogo() {
        return logo;
    }



    public void setLogo(String logo) {
        this.logo = logo;
    }



    public Long getIdAutobus() {
        return idAutobus;
    }



    public void setIdAutobus(Long idAutobus) {
        this.idAutobus = idAutobus;
    }



    public int getCantAdultos() {
        return cantAdultos;
    }



    public void setCantAdultos(int cantAdultos) {
        this.cantAdultos = cantAdultos;
    }



    public int getCantNinos() {
        return cantNinos;
    }



    public void setCantNinos(int cantNinos) {
        this.cantNinos = cantNinos;
    }



    public int getCantInapam() {
        return cantInapam;
    }



    public void setCantInapam(int cantInapam) {
        this.cantInapam = cantInapam;
    }



    public int getSanitario() {
        return sanitario;
    }



    public void setSanitario(int sanitario) {
        this.sanitario = sanitario;
    }



    public Long getTipoTrasporte() {
        return tipoTrasporte;
    }



    public void setTipoTrasporte(Long tipoTrasporte) {
        this.tipoTrasporte = tipoTrasporte;
    }



    public double getPrecioAdultos() {
        return precioAdultos;
    }



    public void setPrecioAdultos(double precioAdultos) {
        this.precioAdultos = precioAdultos;
    }



    public double getPrecioNinos() {
        return precioNinos;
    }



    public void setPrecioNinos(double precioNinos) {
        this.precioNinos = precioNinos;
    }



    public double getPrecioInapam() {
        return precioInapam;
    }



    public void setPrecioInapam(double precioInapam) {
        this.precioInapam = precioInapam;
    }



    public Long getDisponibilidadAutobusLong() {
        return disponibilidadAutobusLong;
    }



    public void setDisponibilidadAutobusLong(Long disponibilidadAutobusLong) {
        this.disponibilidadAutobusLong = disponibilidadAutobusLong;
    }



    public Long getIdRuta() {
        return idRuta;
    }



    public void setIdRuta(Long idRuta) {
        this.idRuta = idRuta;
    }
    
    
    
    
    

    
}
