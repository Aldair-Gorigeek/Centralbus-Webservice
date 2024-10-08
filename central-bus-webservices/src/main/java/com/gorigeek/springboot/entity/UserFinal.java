package com.gorigeek.springboot.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_usuarios_final")
public class UserFinal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idt_usuarios_final")
    private Long idtUsuariosfinal;

    @Column(name = "email")
    private String email;

    @Column(name = "pass")
    private String pass;

    // @Column(name = "seed")//este no va
    // private String seed;

    @Column(name = "fechaRegistro")
    private String fechaRegistro;

    @Column(name = "codigo_verificacion")
    private String codigoVerificacion;

    @Column(name = "estatus_activo")
    private int estatusActivo;

    @Column(name = "token")
    private String token;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "telefono")
    private String telefono;

    public UserFinal() {

    }

    public UserFinal(Long idtUsuariosfinal, String email, String pass, String fechaRegistro,
            String codigoVerificacion, int estatusActivo, String token, String nombre, String telefono) {
        super();
        this.idtUsuariosfinal = idtUsuariosfinal;
        this.email = email;
        this.pass = pass;
        // this.seed = seed;
        this.fechaRegistro = fechaRegistro;
        this.codigoVerificacion = codigoVerificacion;
        this.estatusActivo = estatusActivo;
        this.token = token;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public Long getIdtUsuariosfinal() {
        return idtUsuariosfinal;
    }

    public void setIdtUsuariosfinal(Long idtUsuariosfinal) {
        this.idtUsuariosfinal = idtUsuariosfinal;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    // public String getSeed() {
    // return seed;
    // }

    // public void setSeed(String seed) {
    // this.seed = seed;
    // }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getCodigoVerificacion() {
        return codigoVerificacion;
    }

    public void setCodigoVerificacion(String codigoVerificacion) {
        this.codigoVerificacion = codigoVerificacion;
    }

    public int getEstatusActivo() {
        return estatusActivo;
    }

    public void setEstatusActivo(int estatusActivo) {
        this.estatusActivo = estatusActivo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

}
