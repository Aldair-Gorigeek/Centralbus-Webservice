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
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "tour_has_transporte")
public class TourHasTransporte implements Cloneable{
    
    @Column(name ="adulto")
    private String adulto;
    
    @Column(name ="nino")
    private String nino;
    
    @Column(name ="inpam")
    private String inapam;
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name ="id_tour_has_transporte")
    private Long idRutaHasTransporteTour;
    
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "t_tour")
    private TourMovil idtour;
    
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name ="t_autobus")
    private Autobus idAutobus;
    
        
    
    
        
    @Transient
    private boolean discountActivated = false;

    

    public TourHasTransporte() {
        super();
    }


    public TourHasTransporte(Long idRutaHasTransporteTour, TourMovil idtour, Autobus idAutobus, String adulto,
            String nino, String inapam, boolean discountActivated) {
        super();
        this.idRutaHasTransporteTour = idRutaHasTransporteTour;
        this.idtour = idtour;
        this.idAutobus = idAutobus;
        this.adulto = adulto;
        this.nino = nino;
        this.inapam = inapam;
        this.discountActivated = discountActivated;
    }


    public Long getIdRutaHasTransporteTour() {
        return idRutaHasTransporteTour;
    }


    public void setIdRutaHasTransporteTour(Long idRutaHasTransporteTour) {
        this.idRutaHasTransporteTour = idRutaHasTransporteTour;
    }


    public TourMovil getIdtour() {
        return idtour;
    }


    public void setIdtour(TourMovil idtour) {
        this.idtour = idtour;
    }


    public Autobus getIdAutobus() {
        return idAutobus;
    }


    public void setIdAutobus(Autobus idAutobus) {
        this.idAutobus = idAutobus;
    }


    public String getAdulto() {
        return adulto;
    }


    public void setAdulto(String adulto) {
        this.adulto = adulto;
    }


    public String getNino() {
        return nino;
    }


    public void setNino(String nino) {
        this.nino = nino;
    }


    public String getInapam() {
        return inapam;
    }


    public void setInapam(String inapam) {
        this.inapam = inapam;
    }


    public boolean isDiscountActivated() {
        return discountActivated;
    }


    public void setDiscountActivated(boolean discountActivated) {
        this.discountActivated = discountActivated;
    }
    
    
    
  
    
}
