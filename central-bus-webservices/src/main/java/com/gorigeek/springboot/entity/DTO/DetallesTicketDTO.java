package com.gorigeek.springboot.entity.DTO;

public class DetallesTicketDTO {

    private Long idTicket;

    private boolean isTour;

    private String tipoBoleto;

    private String nombrePasajero;

    private String logo;

    private String fecha;

    private String hora;

    private String origen;

    private String destino;

    private int asiento;

    private double subtotal;

    private double iva;

    private double total;

    private String folio;

    private String afiliado;

    private String terminosCondiciones;

    private Integer mostrarDireccion;

    private String direccionTerminalOrigen;

    private String direccionTerminalDestino;
    private Boolean isDoubleDecker;
    private Integer numeroPlanta;

    public DetallesTicketDTO() {
    }

    public DetallesTicketDTO(Long idTicket, boolean isTour, String tipoBoleto, String nombrePasajero, String logo,
            String fecha, String hora, String origen, String destino, int asiento, double subtotal, double iva,
            double total, String folio, String afiliado) {
        super();
        this.idTicket = idTicket;
        this.isTour = isTour;
        this.tipoBoleto = tipoBoleto;
        this.nombrePasajero = nombrePasajero;
        this.logo = logo;
        this.fecha = fecha;
        this.hora = hora;
        this.origen = origen;
        this.destino = destino;
        this.asiento = asiento;
        this.subtotal = subtotal;
        this.iva = iva;
        this.total = total;
        this.folio = folio;
        this.afiliado = afiliado;
    }

    public String getTerminosCondiciones() {
        return terminosCondiciones;
    }

    public void setTerminosCondiciones(String terminosCondiciones) {
        this.terminosCondiciones = terminosCondiciones;
    }

    public Long getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(Long idTicket) {
        this.idTicket = idTicket;
    }

    public boolean isTour() {
        return isTour;
    }

    public void setTour(boolean isTour) {
        this.isTour = isTour;
    }

    public String getTipoBoleto() {
        return tipoBoleto;
    }

    public void setTipoBoleto(String tipoBoleto) {
        this.tipoBoleto = tipoBoleto;
    }

    public String getNombrePasajero() {
        return nombrePasajero;
    }

    public void setNombrePasajero(String nombrePasajero) {
        this.nombrePasajero = nombrePasajero;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
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

    public int getAsiento() {
        return asiento;
    }

    public void setAsiento(int asiento) {
        this.asiento = asiento;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getAfiliado() {
        return afiliado;
    }

    public void setAfiliado(String afiliado) {
        this.afiliado = afiliado;
    }

    public Integer getMostrarDireccion() {
        return mostrarDireccion;
    }

    public void setMostrarDireccion(Integer mostrarDireccion) {
        this.mostrarDireccion = mostrarDireccion;
    }

    public String getDireccionTerminalOrigen() {
        return direccionTerminalOrigen;
    }

    public void setDireccionTerminalOrigen(String direccionTerminalOrigen) {
        this.direccionTerminalOrigen = direccionTerminalOrigen;
    }

    public String getDireccionTerminalDestino() {
        return direccionTerminalDestino;
    }

    public void setDireccionTerminalDestino(String direccionTerminalDestino) {
        this.direccionTerminalDestino = direccionTerminalDestino;
    }

    public Boolean getIsDoubleDecker() {
        return isDoubleDecker;
    }

    public void setIsDoubleDecker(Boolean isDoubleDecker) {
        this.isDoubleDecker = isDoubleDecker;
    }

    public Integer getNumeroPlanta() {
        return numeroPlanta;
    }

    public void setNumeroPlanta(Integer numeroPlanta) {
        this.numeroPlanta = numeroPlanta;
    }

}
