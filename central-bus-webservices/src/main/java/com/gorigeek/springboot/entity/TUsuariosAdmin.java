package com.gorigeek.springboot.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_usuarios_final")
public class TUsuariosAdmin implements Serializable {

    @Id
    private String idt_usuarios_final;
    private String email;

    public String getIdt_usuarios_final() {
        return idt_usuarios_final;
    }

    public void setIdt_usuarios_final(String idt_usuarios_final) {
        this.idt_usuarios_final = idt_usuarios_final;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private static final long serialVersionUID = 1L;
}