package com.gorigeek.springboot.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator=ObjectIdGenerators.UUIDGenerator.class, property="@id")
@Entity
@Table(name="detalle_venta_tour")
public class DetalleVentaTourMovil {
    
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "id_detalle_venta_tour")
    private Long idDetalleVentaTour;
    
    
/*  @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "t_venta_tour")
    private VentaTourMovil ventaTour;*/
    
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "t_venta_tour")
    private VentaTourMovil ventaTour;
    
    
    //@Column(name = "t_usuarios_final", insertable = false, updatable = false)
    @Column(name = "t_usuarios_final")
    private Long usuarioFinal;
    
    //@Column(name = "c_tipoboleto")
    //private Long tipoBoleto;
    
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "c_tipoboleto")
    private TipoBoleto tipoBoleto;
    
    @Column(name = "costo")
    private double costo;
    
    @Column(name = "numero_asiento")
    private Integer numeroAsiento;
    
    @Column(name = "t_autobus")
    private double autobus;
    
    @Column(name = "nombre_pasajero")
    private String nombrePasajero;
    
    @Column(name = "fecha_viaje")
    private String fechaViaje;
    
    @Column(name = "folio")
    private String folio;
    
    @Column(name = "estatus_disponible", nullable = true)
    private int statusDisponible;
    
    @Column(name = "c_estatus_notificacion", nullable = true)
    private Integer statusNotificacion;
    
    public DetalleVentaTourMovil() {}



    public DetalleVentaTourMovil(Long id_detalle_venta_tour, VentaTourMovil ventaTour, Long usuarioFinal,
            TipoBoleto tipoBoleto, double costo, Integer numeroAsiento, double autobus, String nombrePasajero,
            String fechaViaje, String folio, int statusDisponible, Integer statusNotificacion) {
        super();
        this.idDetalleVentaTour = id_detalle_venta_tour;
        this.ventaTour = ventaTour;
        this.usuarioFinal = usuarioFinal;
        this.tipoBoleto = tipoBoleto;
        this.costo = costo;
        this.numeroAsiento = numeroAsiento;
        this.autobus = autobus;
        this.nombrePasajero = nombrePasajero;
        this.fechaViaje = fechaViaje;
        this.folio = folio;
        this.statusDisponible = statusDisponible;
        this.statusNotificacion = statusNotificacion;
    }



    public Long getId_detalle_venta_tour() {
        return idDetalleVentaTour;
    }


    public void setId_detalle_venta_tour(Long id_detalle_venta_tour) {
        this.idDetalleVentaTour = id_detalle_venta_tour;
    }



    public VentaTourMovil getVentaTour() {
        return ventaTour;
    }

    public void setVentaTour(VentaTourMovil ventaTour) {
        this.ventaTour = ventaTour;
    }

    public Long getUsuarioFinal() {
        return usuarioFinal;
    }

    public void setUsuarioFinal(Long usuarioFinal) {
        this.usuarioFinal = usuarioFinal;
    }

    public TipoBoleto getTipoBoleto() {
        return tipoBoleto;
    }

    public void setTipoBoleto(TipoBoleto tipoBoleto) {
        this.tipoBoleto = tipoBoleto;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public Integer getNumeroAsiento() {
        return numeroAsiento;
    }

    public void setNumeroAsiento(Integer numeroAsiento) {
        this.numeroAsiento = numeroAsiento;
    }

    public double getAutobus() {
        return autobus;
    }

    public void setAutobus(double autobus) {
        this.autobus = autobus;
    }

    public String getNombrePasajero() {
        return nombrePasajero;
    }

    public void setNombrePasajero(String nombrePasajero) {
        this.nombrePasajero = nombrePasajero;
    }

    public String getFechaViaje() {
        return fechaViaje;
    }

    public void setFechaViaje(String fechaViaje) {
        this.fechaViaje = fechaViaje;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public int getStatusDisponible() {
        return statusDisponible;
    }

    public void setStatusDisponible(int statusDisponible) {
        this.statusDisponible = statusDisponible;
    }



    public Long getIdDetalleVentaTour() {
        return idDetalleVentaTour;
    }



    public void setIdDetalleVentaTour(Long idDetalleVentaTour) {
        this.idDetalleVentaTour = idDetalleVentaTour;
    }



    public Integer getStatusNotificacion() {
        return statusNotificacion;
    }



    public void setStatusNotificacion(Integer statusNotificacion) {
        this.statusNotificacion = statusNotificacion;
    }
    
    
}
