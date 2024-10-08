package com.gorigeek.springboot.entity;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "t_afiliado")
public class TAfiliado implements Serializable {

    @Id
    private String idt_afiliado;
    private String logotipo;
    private String c_statuscuenta_activo;

    public String getIdt_afiliado() {
        return idt_afiliado;
    }

    public void setIdt_afiliado(String idt_afiliado) {
        this.idt_afiliado = idt_afiliado;
    }

    public String getLogotipo() {
        return logotipo;
    }

    public void setLogotipo(String logotipo) {
        this.logotipo = logotipo;
    }

    public String getC_statuscuenta_activo() {
        return c_statuscuenta_activo;
    }

    public void setC_statuscuenta_activo(String c_statuscuenta_activo) {
        this.c_statuscuenta_activo = c_statuscuenta_activo;
    }

    private static final long serialVersionUID = 1L;

}