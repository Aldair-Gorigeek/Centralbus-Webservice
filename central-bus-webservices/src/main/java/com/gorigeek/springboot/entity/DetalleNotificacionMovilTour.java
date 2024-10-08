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

@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property = "@id")
@Entity
@Table(name = "detalle_venta_tour")
public class DetalleNotificacionMovilTour {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id_detalle_venta_tour;
    
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "t_venta_tour")
    private VentaTourMovil ventaViajes;
    
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "t_usuarios_final")
    private UserFinal usuarioFinal;

    @Column(name = "fecha_viaje")
    private String fechaViaje;

    @Column(name = "estatus_disponible", nullable = true)
    private String statusDisponible;

    @Column(name = "c_estatus_notificacion", nullable = true)
    private String statusNotificacion;
    
    public DetalleNotificacionMovilTour() {}


    public DetalleNotificacionMovilTour(
            String id_detalle_venta_tour,    
            VentaTourMovil ventaViajes,
            UserFinal usuarioFinal,
            String fechaViaje,         
            String statusDisponible, 
            String statusNotificacion) {
        super();
        this.id_detalle_venta_tour = id_detalle_venta_tour;
        this.ventaViajes = ventaViajes;
        this.usuarioFinal = usuarioFinal;
        this.fechaViaje = fechaViaje;
        this.statusDisponible = statusDisponible;
        this.statusNotificacion = statusNotificacion;
    }

    public String getId_detalle_venta_tour() {
        return id_detalle_venta_tour;
    }

    public void setId_detalle_venta_tour(String id_detalle_venta_tour) {
        this.id_detalle_venta_tour = id_detalle_venta_tour;
    }        

    public VentaTourMovil getVentaViajes() {
        return ventaViajes;
    }

    public void setVentaViajes(VentaTourMovil ventaViajes) {
        this.ventaViajes = ventaViajes;
    }    

    public UserFinal getUsuarioFinal() {
        return usuarioFinal;
    }

    public void setUsuarioFinal(UserFinal usuarioFinal) {
        this.usuarioFinal = usuarioFinal;
    }

    public String getFechaViaje() {
        return fechaViaje;
    }

    public void setFechaViaje(String fechaViaje) {
        this.fechaViaje = fechaViaje;
    }

    public String getStatusDisponible() {
        return statusDisponible;
    }

    public void setStatusDisponible(String statusDisponible) {
        this.statusDisponible = statusDisponible;
    }

    public String getStatusNotificacion() {
        return statusNotificacion;
    }

    public void setStatusNotificacion(String statusNotificacion) {
        this.statusNotificacion = statusNotificacion;
    }

}
