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
@Table(name="t_venta_tour")
public class VentaTourMovil {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idt_venta_tour")
    private Long idVentaTour;
    
    @Column(name = "t_usuariosadmin_cajero")
    private Long cajero;
    
    @Column(name = "total_pagado")
    private double totalPagado;
    
    @Column(name = "fecha_compra")
    private String fechaCompra;
    
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "t_tour")
    private TourMovil tour;
    
    //@Column(name = "t_afiliado")
    //private Long afiliado;
    
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "t_afiliado")
    private Afiliado afiliado;
    
    @Column(name = "promocion")
    private Long promocion;
    
/*    @OneToOne(mappedBy="ventaTour",cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private DetalleVentaTourMovil detalleventaTourViajes;*/
    
    @OneToMany(mappedBy="ventaTour",cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<DetalleVentaTourMovil> detalleventaTourViajes;
    
    public VentaTourMovil() {}

    

    public VentaTourMovil(Long idVentaTour, Long cajero, double totalPagado, String fechaCompra, TourMovil tour,
            Afiliado afiliado, Long promocion, List<DetalleVentaTourMovil> detalleventaTourViajes) {
        super();
        this.idVentaTour = idVentaTour;
        this.cajero = cajero;
        this.totalPagado = totalPagado;
        this.fechaCompra = fechaCompra;
        this.tour = tour;
        this.afiliado = afiliado;
        this.promocion = promocion;
        this.detalleventaTourViajes = detalleventaTourViajes;
    }



    public Long getIdVentaTour() {
        return idVentaTour;
    }

    public void setIdVentaTour(Long idVentaTour) {
        this.idVentaTour = idVentaTour;
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

    public TourMovil getTour() {
        return tour;
    }

    public void setTour(TourMovil tour) {
        this.tour = tour;
    }

    public Afiliado getAfiliado() {
        return afiliado;
    }

    public void setAfiliado(Afiliado afiliado) {
        this.afiliado = afiliado;
    }

    public Long getPromocion() {
        return promocion;
    }

    public void setPromocion(Long promocion) {
        this.promocion = promocion;
    }



    public List<DetalleVentaTourMovil> getDetalleventaTourViajes() {
        return detalleventaTourViajes;
    }



    public void setDetalleventaTourViajes(List<DetalleVentaTourMovil> detalleventaTourViajes) {
        this.detalleventaTourViajes = detalleventaTourViajes;
    }

    
}
