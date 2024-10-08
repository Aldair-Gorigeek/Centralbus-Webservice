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
@Table(name = "temp_asientos_normal")
public class AsientoNormalTemp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idtemp_asientos_normal;
    
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "t_terminales_origen")
    private Terminales terminalOrigen;
    
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "t_terminales_destino")
    private Terminales terminalDestino;
    
    @Column(name = "t_ruta")
    private int ruta;
    
    
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
    
    @Column(name = "tipo_planta", nullable = true)
    private Integer tipoPlanta;
    
    //private String asientoTemp;
    
    
    
    

    public AsientoNormalTemp() {
        super();
    }





    public AsientoNormalTemp(Long idtemp_asientos_normal, Terminales terminalOrigen, Terminales terminalDestino,
            int ruta, int numeroAsiento, String fechaViaje, TipoBoleto tipoBoleto, double autobus, String fechaInsert, Long usuarioFinal, Integer tipoPlanta) {
        super();
        this.idtemp_asientos_normal = idtemp_asientos_normal;
        this.terminalOrigen = terminalOrigen;
        this.terminalDestino = terminalDestino;
        this.ruta = ruta;
        this.numeroAsiento = numeroAsiento;
        this.fechaViaje = fechaViaje;
        this.tipoBoleto = tipoBoleto;
        this.autobus = autobus;
        this.fechaInsert = fechaInsert;
        this.usuarioFinal= usuarioFinal;
        this.tipoPlanta = tipoPlanta;
        //this.asientoTemp = asientoTemp;
    }





    public Long getIdtemp_asientos_normal() {
        return idtemp_asientos_normal;
    }





    public void setIdtemp_asientos_normal(Long idtemp_asientos_normal) {
        this.idtemp_asientos_normal = idtemp_asientos_normal;
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





    public int getRuta() {
        return ruta;
    }





    public void setRuta(int ruta) {
        this.ruta = ruta;
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





    public Integer getTipoPlanta() {
        return tipoPlanta;
    }





    public void setTipoPlanta(Integer tipoPlanta) {
        this.tipoPlanta = tipoPlanta;
    }





    




}
