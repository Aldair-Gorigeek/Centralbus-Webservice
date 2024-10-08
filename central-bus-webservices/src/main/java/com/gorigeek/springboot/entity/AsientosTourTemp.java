package com.gorigeek.springboot.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity 
@Table(name = "temp_asientos_tour")
public class AsientosTourTemp {
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idtemp_asientos_tour;
    
    
    
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "t_tour_idt_tour")
    private TourMovil tour;
    
    
    
    @Column(name = "numAsiento")
    private int numeroAsiento;
    
    @Column(name = "fecha_viaje")
    private String fechaViaje;
    
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "c_tipoboleto")
    private TipoBoleto tipoBoleto;
    
    @Column(name = "t_autobus")
    private double autobus;
    
    @Column(name = "fecha_hora_insert")
    private String fechaInsert;
    
    @Column(name = "t_usuarios_final")
    private Long usuarioFinal;
    
    

    public AsientosTourTemp() {
        super();
    }



    public AsientosTourTemp(Long idtemp_asientos_tour, TourMovil tour,
            int numeroAsiento, String fechaViaje, TipoBoleto tipoBoleto, double autobus, String fechaInsert, Long usuarioFinal) {
        super();
        this.idtemp_asientos_tour = idtemp_asientos_tour;
       
        this.tour = tour;
        this.numeroAsiento = numeroAsiento;
        this.fechaViaje = fechaViaje;
        this.tipoBoleto = tipoBoleto;
        this.autobus = autobus;
        this.fechaInsert = fechaInsert;
        this.usuarioFinal = usuarioFinal;
    }



    public Long getIdtemp_asientos_tour() {
        return idtemp_asientos_tour;
    }



    public void setIdtemp_asientos_tour(Long idtemp_asientos_tour) {
        this.idtemp_asientos_tour = idtemp_asientos_tour;
    }



    public TourMovil getTour() {
        return tour;
    }

    public void setTour(TourMovil tour) {
        this.tour = tour;
    }



    public int getNumeroAsiento() {
        return numeroAsiento;
    }



    public void setNumeroAsiento(int numeroAsiento) {
        this.numeroAsiento = numeroAsiento;
    }



    public String getFechaViaje() {
        return fechaViaje;
    }



    public void setFechaViaje(String fechaViaje) {
        this.fechaViaje = fechaViaje;
    }



    public TipoBoleto getTipoBoleto() {
        return tipoBoleto;
    }



    public void setTipoBoleto(TipoBoleto tipoBoleto) {
        this.tipoBoleto = tipoBoleto;
    }



    public double getAutobus() {
        return autobus;
    }



    public void setAutobus(double autobus) {
        this.autobus = autobus;
    }



    public String getFechaInsert() {
        return fechaInsert;
    }



    public void setFechaInsert(String fechaInsert) {
        this.fechaInsert = fechaInsert;
    }
    
    public Long getUsuarioFinal() {
        return usuarioFinal;
    }

    public void setUsuarioFinal(Long usuarioFinal) {
        this.usuarioFinal = usuarioFinal;
    }
    
    
    
    

}
