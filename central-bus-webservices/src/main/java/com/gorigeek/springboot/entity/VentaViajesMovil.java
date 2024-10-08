package com.gorigeek.springboot.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="t_venta_viajes")
public class VentaViajesMovil {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idt_venta_viajes")
    private Long idVentaViaje;
    
    @Column(name = "t_usuariosadmin_cajero")
    private Long cajero;
    
    @Column(name = "total_pagado")
    private double totalPagado;
    
    @Column(name = "fecha_compra")
    private String fechaCompra;
    
    //@Column(name = "t_afiliado")
    //private Long afiliado;
    
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "t_afiliado")
    private Afiliado afiliado;
    
    @Column(name = "t_ruta")
    private Long ruta;
    
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "t_terminales_origen")
    private Terminales terminalOrigen;
    
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "t_terminales_destino")
    private Terminales terminalDestino;
    
    @Column(name = "promocion")
    private Long promocion;
    
    /*@OneToOne(mappedBy="ventaViajes",cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private DetalleVentaMovil detalleventaViajes;*/
    
    @OneToMany(mappedBy="ventaViajes",cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<DetalleVentaMovil> detalleventaViajes;
    
    public VentaViajesMovil() {     
    }

    

    public VentaViajesMovil(Long idVentaViaje, Long cajero, double totalPagado, String fechaCompra, Afiliado afiliado,
            Long ruta, Terminales terminalOrigen, Terminales terminalDestino, Long promocion,
            List<DetalleVentaMovil> detalleventaViajes) {
        super();
        this.idVentaViaje = idVentaViaje;
        this.cajero = cajero;
        this.totalPagado = totalPagado;
        this.fechaCompra = fechaCompra;
        this.afiliado = afiliado;
        this.ruta = ruta;
        this.terminalOrigen = terminalOrigen;
        this.terminalDestino = terminalDestino;
        this.promocion = promocion;
        this.detalleventaViajes = detalleventaViajes;
    }



    public Long getIdVentaViaje() {
        return idVentaViaje;
    }



    public void setIdVentaViaje(Long idVentaViaje) {
        this.idVentaViaje = idVentaViaje;
    }



    public Long getCajero() {
        return cajero;
    }



    public void setCajero(Long cajero) {
        this.cajero = cajero;
    }



    public double getTotalPagado() {
        return totalPagado;
    }



    public void setTotalPagado(double totalPagado) {
        this.totalPagado = totalPagado;
    }



    public String getFechaCompra() {
        return fechaCompra;
    }



    public void setFechaCompra(String fechaCompra) {
        this.fechaCompra = fechaCompra;
    }



    public Afiliado getAfiliado() {
        return afiliado;
    }



    public void setAfiliado(Afiliado afiliado) {
        this.afiliado = afiliado;
    }



    public Long getRuta() {
        return ruta;
    }



    public void setRuta(Long ruta) {
        this.ruta = ruta;
    }



    public Terminales getTerminalOrigen() {
        return terminalOrigen;
    }



    public void setTerminalOrigen(Terminales terminalOrigen) {
        this.terminalOrigen = terminalOrigen;
    }



    public Terminales getTerminalDestino() {
        return terminalDestino;
    }



    public void setTerminalDestino(Terminales terminalDestino) {
        this.terminalDestino = terminalDestino;
    }



    public Long getPromocion() {
        return promocion;
    }



    public void setPromocion(Long promocion) {
        this.promocion = promocion;
    }



    public List<DetalleVentaMovil> getDetalleventaViajes() {
        return detalleventaViajes;
    }



    public void setDetalleventaViajes(List<DetalleVentaMovil> detalleventaViajes) {
        this.detalleventaViajes = detalleventaViajes;
    }


}
