package com.gorigeek.springboot.entity;

import javax.persistence.*;

@Entity
@Table(name = "t_afiliado")
public class Afiliado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idt_afiliado")
    private Long idt_afiliado;

    @Column(name = "nombre_titular")
    private String nombreTitular;

    @Column(name = "email")
    private String email;

    @Column(name = "telefono_titular")
    private String telefonoTitular;

    @Column(name = "direccion_empresa")
    private String direccion_empresa;

    @Column(name = "nombre_linea")
    private String nombreLinea;

    @Column(name = "rfc")
    private String rfc;

    @Column(name = "logotipo")
    private String logotipo;

    @Column(name = "c_banco")
    private int c_banco;

    @Column(name = "numerocuenta")
    private String numeroCuenta;

    @Column(name = "titular_cuenta")
    private String titularCuenta;

    @Column(name = "terminos_condiciones")
    private int terminosCondiciones;

    @Column(name = "fecha_solicitud")
    private String fechaSolicitud;

    @Column(name = "fecha_aprobacion")
    private String fechaAprobacion;

    @Column(name = "c_statuscuenta_activo")
    private int cStatuscuentaActivo;

    @Column(name = "cancelaciones_devoluciones")
    private String cancelaciones_devoluciones;

    @Column(name = "min_horas_cancelacion")
    private String min_horas_cancelacion;

    @Column(name = "max_devoluciones_mes")
    private String max_devoluciones_mes;

    @Column(name = "cancelaciones_devoluciones_promo")
    private String cancelaciones_devoluciones_promo;

    @Column(name = "mostrar_direccion")
    private Integer mostrarDireccion;

    public Afiliado() {
    }

    public Afiliado(Long idt_afiliado, String nombreTitular, String email, String telefonoTitular,
            String direccion_empresa, String nombreLinea, String rfc, String logotipo, int c_banco, String numeroCuenta,
            String titularCuenta,
            int terminosCondiciones, String fechaSolicitud, String fechaAprobacion, int c_statuscuenta_activo,
            String cancelaciones_devoluciones, String min_horas_cancelacion, String max_devoluciones_mes,
            String cancelaciones_devoluciones_promo) {
        super();
        this.idt_afiliado = idt_afiliado;
        this.nombreTitular = nombreTitular;
        this.email = email;
        this.telefonoTitular = telefonoTitular;
        this.direccion_empresa = direccion_empresa;
        this.rfc = rfc;
        this.logotipo = logotipo;
        this.c_banco = c_banco;
        this.numeroCuenta = numeroCuenta;
        this.titularCuenta = titularCuenta;
        this.terminosCondiciones = terminosCondiciones;
        this.fechaSolicitud = fechaSolicitud;
        this.fechaAprobacion = fechaAprobacion;
        this.cStatuscuentaActivo = c_statuscuenta_activo;
        this.cancelaciones_devoluciones = cancelaciones_devoluciones;
        this.min_horas_cancelacion = min_horas_cancelacion;
        this.max_devoluciones_mes = max_devoluciones_mes;
        this.cancelaciones_devoluciones_promo = cancelaciones_devoluciones_promo;
    }

    public Integer getMostrarDireccion() {
        return mostrarDireccion;
    }

    public void setMostrarDireccion(Integer mostrarDireccion) {
        this.mostrarDireccion = mostrarDireccion;
    }

    public Long getIdt_afiliado() {
        return idt_afiliado;
    }

    public void setIdt_afiliado(Long idt_afiliado) {
        this.idt_afiliado = idt_afiliado;
    }

    public String getNombreTitular() {
        return nombreTitular;
    }

    public void setNombreTitular(String nombreTitular) {
        this.nombreTitular = nombreTitular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefonoTitular() {
        return telefonoTitular;
    }

    public void setTelefonoTitular(String telefonoTitular) {
        this.telefonoTitular = telefonoTitular;
    }

    public String getDireccion_empresa() {
        return direccion_empresa;
    }

    public void setDireccion_empresa(String direccion_empresa) {
        this.direccion_empresa = direccion_empresa;
    }

    public String getNombreLinea() {
        return nombreLinea;
    }

    public void setNombreLinea(String nombreLinea) {
        this.nombreLinea = nombreLinea;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getLogotipo() {
        return logotipo;
    }

    public void setLogotipo(String logotipo) {
        this.logotipo = logotipo;
    }

    public int getC_banco() {
        return c_banco;
    }

    public void setC_banco(int c_banco) {
        this.c_banco = c_banco;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getTitularCuenta() {
        return titularCuenta;
    }

    public void setTitularCuenta(String titularCuenta) {
        this.titularCuenta = titularCuenta;
    }

    public int getTerminosCondiciones() {
        return terminosCondiciones;
    }

    public void setTerminosCondiciones(int terminosCondiciones) {
        this.terminosCondiciones = terminosCondiciones;
    }

    public String getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(String fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public String getFechaAprobacion() {
        return fechaAprobacion;
    }

    public void setFechaAprobacion(String fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }

    public int getCStatuscuentaActivo() {
        return cStatuscuentaActivo;
    }

    public void setCStatuscuentaActivo(int c_statuscuenta_activo) {
        this.cStatuscuentaActivo = c_statuscuenta_activo;
    }

    public String getCancelaciones_devoluciones() {
        return cancelaciones_devoluciones;
    }

    public void setCancelaciones_devoluciones(String cancelaciones_devoluciones) {
        this.cancelaciones_devoluciones = cancelaciones_devoluciones;
    }

    public String getMin_horas_cancelacion() {
        return min_horas_cancelacion;
    }

    public void setMin_horas_cancelacion(String min_horas_cancelacion) {
        this.min_horas_cancelacion = min_horas_cancelacion;
    }

    public String getMax_devoluciones_mes() {
        return max_devoluciones_mes;
    }

    public void setMax_devoluciones_mes(String max_devoluciones_mes) {
        this.max_devoluciones_mes = max_devoluciones_mes;
    }

    public String getCancelaciones_devoluciones_promo() {
        return cancelaciones_devoluciones_promo;
    }

    public void setCancelaciones_devoluciones_promo(String cancelaciones_devoluciones_promo) {
        this.cancelaciones_devoluciones_promo = cancelaciones_devoluciones_promo;
    }

}
