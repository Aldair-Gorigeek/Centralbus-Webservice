package com.gorigeek.springboot.distribution.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "t_ventas_distribusion")
public class VentasDistribution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idt_ventas_distribusion")
    private Long id;

    @Column(name = "fecha_hora_compra")
    private String fechaHoraCompra;

    @Column(name = "t_usuarios_final")
    private Long idUsuarioFinal;

    @Column(name = "origen")
    private String origen;

    @Column(name = "destino")
    private String destino;

    @Column(name = "fecha_hora_viaje")
    private String fechaHoraViaje;

    @Column(name = "nombre_pasajero")
    private String nombrePasajero;

    @Column(name = "folio_boleto")
    private String folioBoleto;

    @Column(name = "email_usuario")
    private String emailUsuario;

    @Column(name = "precio_boleto")
    private Double precioBoleto;

    @Column(name = "linea_transporte")
    private String lineaTransporte;

    @Column(name = "id_reservacion")
    private String idReservacion;

    @Column(name = "tipo_boleto")
    private String tipoBoleto;

    @Lob
    @Column(name = "logo")
    private byte[] logo;

    @Column(name = "num_asiento")
    private String numAsiento;

    @Column(name = "eliminado")
    private Integer eliminado;
    
    @Column(name = "comision_porcentaje")
    private String comisionPorcentaje;
    
    @Column(name = "comision_monto")
    private String comisionMonto;
    
    @Column(name = "descuento")
    private String descuento;

    @Transient
    private String horaViaje;
    
    @Transient
    private String fechaViaje;
    
    @Transient
    private Double precioBoletoSinDescuento ;

    public String getHoraViaje() {
        return horaViaje;
    }

    public void setHoraViaje(String horaViaje) {
        this.horaViaje = horaViaje;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getEliminado() {
        return eliminado;
    }

    public void setEliminado(Integer eliminado) {
        this.eliminado = eliminado;
    }

    public String getFechaHoraCompra() {
        return fechaHoraCompra;
    }

    public void setFechaHoraCompra(String fechaHoraCompra) {
        this.fechaHoraCompra = fechaHoraCompra;
    }

    public Long getIdUsuarioFinal() {
        return idUsuarioFinal;
    }

    public void setIdUsuarioFinal(Long idUsuarioFinal) {
        this.idUsuarioFinal = idUsuarioFinal;
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

    public String getFechaHoraViaje() {
        return fechaHoraViaje;
    }

    public void setFechaHoraViaje(String fechaHoraViaje) {
        this.fechaHoraViaje = fechaHoraViaje;
    }

    public String getNombrePasajero() {
        return nombrePasajero;
    }

    public void setNombrePasajero(String nombrePasajero) {
        this.nombrePasajero = nombrePasajero;
    }

    public String getFolioBoleto() {
        return folioBoleto;
    }

    public void setFolioBoleto(String folioBoleto) {
        this.folioBoleto = folioBoleto;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public Double getPrecioBoleto() {
        return precioBoleto;
    }

    public void setPrecioBoleto(Double precioBoleto) {
        this.precioBoleto = precioBoleto;
    }

    public String getLineaTransporte() {
        return lineaTransporte;
    }

    public void setLineaTransporte(String lineaTransporte) {
        this.lineaTransporte = lineaTransporte;
    }

    public String getIdReservacion() {
        return idReservacion;
    }

    public void setIdReservacion(String idReservacion) {
        this.idReservacion = idReservacion;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getNumAsiento() {
        return numAsiento;
    }

    public void setNumAsiento(String numAsiento) {
        this.numAsiento = numAsiento;
    }

    public String getTipoBoleto() {
        return tipoBoleto;
    }

    public void setTipoBoleto(String tipoBoleto) {
        this.tipoBoleto = tipoBoleto;
    }

    public String getComisionPorcentaje() {
        return comisionPorcentaje;
    }

    public void setComisionPorcentaje(String comisionPorcentaje) {
        this.comisionPorcentaje = comisionPorcentaje;
    }

    public String getComisionMonto() {
        return comisionMonto;
    }

    public void setComisionMonto(String comisionMonto) {
        this.comisionMonto = comisionMonto;
    }

    public String getDescuento() {
        return descuento;
    }

    public void setDescuento(String descuento) {
        this.descuento = descuento;
    }

    public Double getPrecioBoletoSinDescuento() {
        return precioBoletoSinDescuento;
    }

    public void setPrecioBoletoSinDescuento(Double precioBoletoSinDescuento) {
        this.precioBoletoSinDescuento = precioBoletoSinDescuento;
    }

    public String getFechaViaje() {
        return fechaViaje;
    }

    public void setFechaViaje(String fechaViaje) {
        this.fechaViaje = fechaViaje;
    }         
}
