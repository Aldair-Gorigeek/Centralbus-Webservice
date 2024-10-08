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

@Entity
@Table(name = "ruta_has_transporte")
public class RutaHasTransporte implements Cloneable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name ="id_ruta_has_transporte")
    private Long idRutaHasTransporte;
    
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name ="t_autobus")
    private Autobus idAutobus;
    
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "t_detalle_ruta")
    private RutasDetalles detalleRuta;
    
    @Column(name ="adulto")
    private String adulto;
    
    @Column(name ="nino")
    private String nino;
    
    @Column(name ="inapam")
    private String inapam;
    
    @Column(name ="adultoPB")
    private String adultoPB;
    
    @Column(name ="ninoPB")
    private String ninoPB;
    
    @Column(name ="inapamPB")
    private String inapamPB;
    
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "t_horarios_salida")
    private HorariosSalida horariosSalida;
    
    @Transient
    private boolean discountActivated = false;
    
    //Se agregan esta propiedades para usarlas en la app
    @Transient
    private String origen;
    @Transient
    private String destino;
    
    @Transient
    private String fecha;
    
    //Propiedades para PROMO99 
    //Descuentos       
    @Transient
    private Double alternativePrice;
    @Transient
    private Integer availableSeatsDiscount;
    
    public RutaHasTransporte() {
        
    }
    
    public RutaHasTransporte(RutaHasTransporte c) {
        this.idRutaHasTransporte = c.idRutaHasTransporte;
        this.idAutobus = c.idAutobus;
        this.detalleRuta = c.detalleRuta;
        this.adulto = c.adulto;
        this.nino = c.nino;
        this.inapam = c.inapam;
        this.adultoPB =c.adultoPB;
        this.ninoPB = c.ninoPB;
        this.inapamPB = c.inapamPB;
        this.horariosSalida = c.horariosSalida;
        
      }
    
    public RutaHasTransporte(Long idRutaHasTransporte,
            Autobus idAutobus,
            RutasDetalles detalleRuta,
            String adulto,
            String nino,
            String inapam,
            String adultoPB,
            String ninoPB,
            String inapamPB,
            HorariosSalida horariosSalida) {
        this.idRutaHasTransporte=idRutaHasTransporte;
        this.idAutobus=idAutobus;
        this.detalleRuta=detalleRuta;
        this.adulto=adulto;
        this.nino=nino;
        this.inapam=inapam;
        this.adultoPB=adultoPB;
        this.ninoPB=ninoPB;
        this.inapamPB=inapamPB;
        this.horariosSalida=horariosSalida;
    }

    
    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public boolean isDiscountActivated() {
        return discountActivated;
    }

    public void setDiscountActivated(boolean discountActivated) {
        this.discountActivated = discountActivated;
    }

    public Long getIdRutaHasTransporte() {
        return idRutaHasTransporte;
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

    public void setIdRutaHasTransporte(Long idRutaHasTransporte) {
        this.idRutaHasTransporte = idRutaHasTransporte;
    }

    public Autobus getIdAutobus() {
        return idAutobus;
    }

    public void setIdAutobus(Autobus idAutobus) {
        this.idAutobus = idAutobus;
    }

    public RutasDetalles getDetalleRuta() {
        return detalleRuta;
    }

    public void setDetalleRuta(RutasDetalles detalleRuta) {
        this.detalleRuta = detalleRuta;
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
    
    public String getAdultoPB() {
        if(adultoPB==null) adultoPB="0";
        return adultoPB;
    }

    public void setAdultoPB(String adultoPB) {
        this.adultoPB = adultoPB;
    }

    public String getNinoPB() {
        if(ninoPB==null) ninoPB="0";
        return ninoPB;
    }

    public void setNinoPB(String ninoPB) {
        this.ninoPB = ninoPB;
    }

    public String getInapamPB() {
        if(inapamPB==null) inapamPB="0";
        return inapamPB;
    }

    public void setInapamPB(String inapamPB) {
        this.inapamPB = inapamPB;
    }

    public HorariosSalida getHorariosSalida() {
        return horariosSalida;
    }

    public void setHorariosSalida(HorariosSalida horariosSalida) {
        this.horariosSalida = horariosSalida;
    }

    public Double getAlternativePrice() {
        return alternativePrice;
    }

    public void setAlternativePrice(Double alternativePrice) {
        this.alternativePrice = alternativePrice;
    }

    public Integer getAvailableSeatsDiscount() {
        return availableSeatsDiscount;
    }

    public void setAvailableSeatsDiscount(Integer availableSeatsDiscount) {
        this.availableSeatsDiscount = availableSeatsDiscount;
    }
        
}
