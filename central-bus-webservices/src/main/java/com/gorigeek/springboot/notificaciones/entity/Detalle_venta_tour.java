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
@Table(name = "detalle_venta_tour")
public class Detalle_venta_tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_detalle_venta_tour;

    @Column(name = "t_usuarios_final")
    private String tUsuariosFinal;

    public Detalle_venta_tour() {
    }

    public Detalle_venta_tour(int id_detalle_venta_tour, String tUsuariosFinal) {
        super();
        this.id_detalle_venta_tour = id_detalle_venta_tour;
        this.tUsuariosFinal = tUsuariosFinal;
    }

    public int getId_detalle_venta_tour() {
        return id_detalle_venta_tour;
    }

    public void setId_detalle_venta_tour(int id_detalle_venta_tour) {
        this.id_detalle_venta_tour = id_detalle_venta_tour;
    }

    public String gettUsuariosFinal() {
        return tUsuariosFinal;
    }

    public void settUsuariosFinal(String tUsuariosFinal) {
        this.tUsuariosFinal = tUsuariosFinal;
    }

}
