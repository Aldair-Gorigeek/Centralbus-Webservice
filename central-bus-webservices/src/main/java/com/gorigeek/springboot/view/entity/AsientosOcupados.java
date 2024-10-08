package com.gorigeek.springboot.view.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "asientos_ocupados")
public class AsientosOcupados {
    @Id    
    private String id;
    @Column(name = "autobus")
    private Long autobus;
    @Column(name = "fecha_viaje")
    private String fechaViaje;
    @Column(name = "hora_viaje")
    private String horaViaje;
    @Column(name = "origen")
    private Long origen;
    @Column(name = "destino")
    private Long destino;
    @Column(name = "secuencia")
    private Integer secuencia;
    @Column(name = "id_ruta")
    private Long idRuta;
    @Column(name = "total_comprados")
    private Integer totalComprados;
    @Column(name = "total_reservados")
    private Integer totalReservados;
    @Column(name = "total_asientos")
    private Integer totalAsientos;

    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getAutobus() {
        return autobus;
    }

    public void setAutobus(Long autobus) {
        this.autobus = autobus;
    }

    public String getFechaViaje() {
        return fechaViaje;
    }

    public void setFechaViaje(String fechaViaje) {
        this.fechaViaje = fechaViaje;
    }

    public String getHoraViaje() {
        return horaViaje;
    }

    public void setHoraViaje(String horaViaje) {
        this.horaViaje = horaViaje;
    }

    public Long getOrigen() {
        return origen;
    }

    public void setOrigen(Long origen) {
        this.origen = origen;
    }

    public Long getDestino() {
        return destino;
    }

    public void setDestino(Long destino) {
        this.destino = destino;
    }

    public Integer getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(Integer secuencia) {
        this.secuencia = secuencia;
    }

    public Long getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(Long idRuta) {
        this.idRuta = idRuta;
    }

    public Integer getTotalComprados() {
        return totalComprados;
    }

    public void setTotalComprados(Integer totalComprados) {
        this.totalComprados = totalComprados;
    }

    public Integer getTotalReservados() {
        return totalReservados;
    }

    public void setTotalReservados(Integer totalReservados) {
        this.totalReservados = totalReservados;
    }

    public Integer getTotalAsientos() {
        return totalAsientos;
    }

    public void setTotalAsientos(Integer totalAsientos) {
        this.totalAsientos = totalAsientos;
    }

}
