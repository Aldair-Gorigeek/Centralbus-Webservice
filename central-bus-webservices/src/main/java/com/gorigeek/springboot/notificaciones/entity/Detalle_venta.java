package com.gorigeek.springboot.notificaciones.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property = "@id")
@Entity
@Table(name = "detalle_venta")
public class Detalle_venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_detalle_venta;

    @Column(name = "t_usuarios_final")
    private String tUsuariosFinal;

    public Detalle_venta() {
    }

    public Detalle_venta(int id_detalle_venta, String tUsuariosFinal) {
        super();
        this.id_detalle_venta = id_detalle_venta;
        this.tUsuariosFinal = tUsuariosFinal;
    }

    public int getId_detalle_venta() {
        return id_detalle_venta;
    }

    public void setId_detalle_venta(int id_detalle_venta) {
        this.id_detalle_venta = id_detalle_venta;
    }

    public String gettUsuariosFinal() {
        return tUsuariosFinal;
    }

    public void settUsuariosFinal(String tUsuariosFinal) {
        this.tUsuariosFinal = tUsuariosFinal;
    }

}
