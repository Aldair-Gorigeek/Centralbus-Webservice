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
@Table(name = "t_notificaciones_push")
public class T_notificaciones_push {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idt_notificaciones_push;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "mensaje")
    private String mensaje;

    @Column(name = "img")
    private String img;

    @Column(name = "c_comportamiento")
    private int comportamiento;

    @Column(name = "url")
    private String url;

    @Column(name = "c_dirigido")
    private int dirigido;

    @Column(name = "t_usuarios_final")
    private String usuariosFinal;

    @Column(name = "fecha_hora")
    private String fechaHora;

    @Column(name = "c_status_notificacion")
    private int statusNotificacion;

    public T_notificaciones_push() {

    }

    public T_notificaciones_push(int idt_notificaciones_push, String titulo, String mensaje, String img,
            int comportamiento, String url, int dirigido, String usuariosFinal, String fechaHora,
            int statusNotificacion) {
        super();
        this.idt_notificaciones_push = idt_notificaciones_push;
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.img = img;
        this.comportamiento = comportamiento;
        this.url = url;
        this.dirigido = dirigido;
        this.usuariosFinal = usuariosFinal;
        this.fechaHora = fechaHora;
        this.statusNotificacion = statusNotificacion;
    }

    public int getIdt_notificaciones_push() {
        return idt_notificaciones_push;
    }

    public void setIdt_notificaciones_push(int idt_notificaciones_push) {
        this.idt_notificaciones_push = idt_notificaciones_push;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getComportamiento() {
        return comportamiento;
    }

    public void setComportamiento(int comportamiento) {
        this.comportamiento = comportamiento;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getDirigido() {
        return dirigido;
    }

    public void setDirigido(int dirigido) {
        this.dirigido = dirigido;
    }

    public String getUsuariosFinal() {
        return usuariosFinal;
    }

    public void setUsuariosFinal(String usuariosFinal) {
        this.usuariosFinal = usuariosFinal;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public int getStatusNotificacion() {
        return statusNotificacion;
    }

    public void setStatusNotificacion(int statusNotificacion) {
        this.statusNotificacion = statusNotificacion;
    }

}
