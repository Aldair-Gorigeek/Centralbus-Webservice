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
@Table(name = "t_ventas_distribusion")
public class T_ventas_distribusion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idt_ventas_distribusion;

    @Column(name = "t_usuarios_final")
    private String tUsuariosFinal;

    public T_ventas_distribusion() {}

    public T_ventas_distribusion(int idt_ventas_distribusion, String tUsuariosFinal) {
        super();
        this.idt_ventas_distribusion = idt_ventas_distribusion;
        this.tUsuariosFinal = tUsuariosFinal;
    }

    public int getIdt_ventas_distribusion() {
        return idt_ventas_distribusion;
    }

    public void setIdt_ventas_distribusion(int idt_ventas_distribusion) {
        this.idt_ventas_distribusion = idt_ventas_distribusion;
    }

    public String gettUsuariosFinal() {
        return tUsuariosFinal;
    }

    public void settUsuariosFinal(String tUsuariosFinal) {
        this.tUsuariosFinal = tUsuariosFinal;
    }

}
