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
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import net.bytebuddy.dynamic.loading.ClassReloadingStrategy.Strategy;

@JsonIdentityInfo(generator=ObjectIdGenerators.UUIDGenerator.class, property="@id")
@Entity
//@IdClass(DetalleId.class)
@Table(name="detalle_venta")
public class DetalleVentaMovil {
    
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "id_detalle_venta")
    private Long idDetalleVenta;
    
    
/*    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "t_venta_viajes")
    private VentaViajesMovil ventaViajes;*/
    
    
    
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "t_venta_viajes")
    private VentaViajesMovil ventaViajes;
    
    
    @Column(name = "t_usuarios_final")
    private Long usuarioFinal;
    
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
    
    //@Column(name = "estatus_cancelado", nullable = true)
    //private Integer statusCancelado;
    
    @Column(name = "fecha_cacelacion")
    private String fechaCancelacion;
    
    @Column(name = "t_usuariosadmin_cancelado")
    private Long usuarioAdminCancelado;
    
    @Column(name = "t_venta_viajes_cambio")
    private Long ventaViajesCambio;
    
    @Column(name = "estatus_disponible", nullable = true)
    private int statusDisponible;
    
    @Column(name = "c_estatus_notificacion", nullable = true)
    private Integer statusNotificacion;
    
    @Column(name = "tipo_planta", nullable = true)
    private Integer tipoPlanta;
    
    
    
    public DetalleVentaMovil() {}


    public DetalleVentaMovil(Long id_detalle_venta, VentaViajesMovil ventaViajes, Long usuarioFinal,
            TipoBoleto tipoBoleto, double costo, int numeroAsiento, double autobus, String nombrePasajero,
            String fechaViaje, String folio, String fechaCancelacion, Long usuarioAdminCancelado,
            Long ventaViajesCambio, int statusDisponible, Integer statusNotificacion, Integer tipoPlanta) {
        super();
        this.idDetalleVenta = id_detalle_venta;
        this.ventaViajes = ventaViajes;
        this.usuarioFinal = usuarioFinal;
        this.tipoBoleto = tipoBoleto;
        this.costo = costo;
        this.numeroAsiento = numeroAsiento;
        this.autobus = autobus;
        this.nombrePasajero = nombrePasajero;
        this.fechaViaje = fechaViaje;
        this.folio = folio;
        //this.statusCancelado = statusCancelado;
        this.fechaCancelacion = fechaCancelacion;
        this.usuarioAdminCancelado = usuarioAdminCancelado;
        this.ventaViajesCambio = ventaViajesCambio;
        this.statusDisponible = statusDisponible;
        this.statusNotificacion = statusNotificacion;
        this.tipoPlanta = tipoPlanta;
    }

    
    public Long getId_detalle_venta() {
        return idDetalleVenta;
    }


    public void setId_detalle_venta(Long id_detalle_venta) {
        this.idDetalleVenta = id_detalle_venta;
    }


    public VentaViajesMovil getVentaViajes() {
        return ventaViajes;
    }

    public void setVentaViajes(VentaViajesMovil ventaViajes) {
        this.ventaViajes = ventaViajes;
    }
    
    /*
    public Long getVentaViajes() {
        return ventaViajes;
    }

    public void setVentaViajes(Long ventaViajes) {
        this.ventaViajes = ventaViajes;
    }*/

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
    /*
    public Integer getStatusCancelado() {
        return statusCancelado;
    }

    public void setStatusCancelado(Integer statusCancelado) {
        this.statusCancelado = statusCancelado;
    }*/

    public String getFechaCancelacion() {
        return fechaCancelacion;
    }

    public void setFechaCancelacion(String fechaCancelacion) {
        this.fechaCancelacion = fechaCancelacion;
    }

    public Long getUsuarioAdminCancelado() {
        return usuarioAdminCancelado;
    }

    public void setUsuarioAdminCancelado(Long usuarioAdminCancelado) {
        this.usuarioAdminCancelado = usuarioAdminCancelado;
    }

    public Long getVentaViajesCambio() {
        return ventaViajesCambio;
    }

    public void setVentaViajesCambio(Long ventaViajesCambio) {
        this.ventaViajesCambio = ventaViajesCambio;
    }

    public int getStatusDisponible() {
        return statusDisponible;
    }

    public void setStatusDisponible(int statusDisponible) {
        this.statusDisponible = statusDisponible;
    }


    public Integer getStatusNotificacion() {
        return statusNotificacion;
    }


    public void setStatusNotificacion(Integer statusNotificacion) {
        this.statusNotificacion = statusNotificacion;
    }


    public Integer getTipoPlanta() {
        return tipoPlanta;
    }


    public void setTipoPlanta(Integer tipoPlanta) {
        this.tipoPlanta = tipoPlanta;
    }
}
