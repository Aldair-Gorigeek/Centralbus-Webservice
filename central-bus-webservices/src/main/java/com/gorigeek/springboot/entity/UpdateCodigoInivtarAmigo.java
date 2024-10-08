package com.gorigeek.springboot.entity;


import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_usuarios_final")
public class UpdateCodigoInivtarAmigo implements Serializable{
    
    @Id
    private String idt_usuarios_final;
    private String codigo_amigo;

    public String getIdt_usuarios_final() {
        return idt_usuarios_final;
    }

    public void setIdt_usuarios_final(String idt_usuarios_final) {
        this.idt_usuarios_final = idt_usuarios_final;
    }

    public String getCodigo_amigo() {
        return codigo_amigo;
    }

    public void setCodigo_amigo(String codigo_amigo) {
        this.codigo_amigo = codigo_amigo;
    }
    
    private static final long serialVersionUID = 1L;

}
