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
@Table(name = "t_usuarios_final")
public class T_usuarios_final {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idt_usuarios_final;

    @Column(name = "token")
    private String token;

    @Column(name = "estatus_activo")
    private int estatusActivo;

    @Column(name = "fechaRegistro")
    private String fechaRegistro;

    public T_usuarios_final() {
    }

    public T_usuarios_final(int idt_usuarios_final, String token, int estatusActivo, String fechaRegistro) {
        super();
        this.idt_usuarios_final = idt_usuarios_final;
        this.token = token;
        this.estatusActivo = estatusActivo;
        this.fechaRegistro = fechaRegistro;
    }

    public int getIdt_usuarios_final() {
        return idt_usuarios_final;
    }

    public void setIdt_usuarios_final(int idt_usuarios_final) {
        this.idt_usuarios_final = idt_usuarios_final;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getEstatusActivo() {
        return estatusActivo;
    }

    public void setEstatusActivo(int estatusActivo) {
        this.estatusActivo = estatusActivo;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

}
