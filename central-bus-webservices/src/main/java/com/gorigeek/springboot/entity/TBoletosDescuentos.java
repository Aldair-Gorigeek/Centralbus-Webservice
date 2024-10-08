package com.gorigeek.springboot.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "t_boletos_descuentos")
public class TBoletosDescuentos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idt_boletos_descuentos")
    private Long id;

    @Column(name = "linea", length = 100)
    private String linea;

    @Column(name = "origen", length = 45)
    private String origen;

    @Column(name = "destino", length = 45)
    private String destino;

    @Column(name = "fecha_salida")
    private String fechaSalida;

    @Column(name = "fecha_creacion")
    private Date fechaCreacion;

    @Column(name = "estatus")
    private Integer estatus;

    @Transient
    private Integer quantityToRegister;

    @Transient
    private Integer quantityToDelete;
    
    @Transient
    private Integer quantityToUpdate;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
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

    public String getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(String fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public Integer getEstatus() {
        return estatus;
    }

    public void setEstatus(Integer estatus) {
        this.estatus = estatus;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Integer getQuantityToRegister() {
        return quantityToRegister;
    }

    public void setQuantityToRegister(Integer quantityToRegister) {
        this.quantityToRegister = quantityToRegister;
    }

    public Integer getQuantityToDelete() {
        return quantityToDelete;
    }

    public void setQuantityToDelete(Integer quantityToDelete) {
        this.quantityToDelete = quantityToDelete;
    }

    public Integer getQuantityToUpdate() {
        return quantityToUpdate;
    }

    public void setQuantityToUpdate(Integer quantityToUpdate) {
        this.quantityToUpdate = quantityToUpdate;
    }       

}
