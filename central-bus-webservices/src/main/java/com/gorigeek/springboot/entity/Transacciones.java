package com.gorigeek.springboot.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_transacciones")
public class Transacciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idt_transacciones")
    private Long idTransacciones;

    @Column(name = "monto_pago")
    private double montoPago;

    @Column(name = "t_usuarios_final", nullable = true)
    private Integer usuarioFinal;

    @Column(name = "fecha_hora")
    private String fechaHora;

    @Column(name = "num_autorizacion")
    private String numAutorizacion;

    @Column(name = "estatus_aprobado")
    private int statusAprobado;

    @Column(name = "num_transaccion")
    private String numTransaccion;

    @Column(name = "t_venta_viajes")
    private Long idventaViaje;

    @Column(name = "t_venta_tour")
    private Long idVentaTour;

    @Column(name = "t_ventas_distribusion")
    private Long idVentasDistribusion;

    @Column(name = "correo")
    private String correo;

    public Transacciones() {
    }

    public Transacciones(Long idTransacciones, double montoPago, int usuarioFinal, String fechaHora,
            String numAutorizacion, int statusAprobado, String numTransaccion, Long idventaViaje,
            Long idVentaTour, String correo) {
        super();
        this.idTransacciones = idTransacciones;
        this.montoPago = montoPago;
        this.usuarioFinal = usuarioFinal;
        this.fechaHora = fechaHora;
        this.numAutorizacion = numAutorizacion;
        this.statusAprobado = statusAprobado;
        this.numTransaccion = numTransaccion;
        this.idventaViaje = idventaViaje;
        this.idVentaTour = idVentaTour;
        this.correo = correo;
    }

    public Long getIdVentasDistribusion() {
        return idVentasDistribusion;
    }

    public void setIdVentasDistribusion(Long idVentasDistribusion) {
        this.idVentasDistribusion = idVentasDistribusion;
    }

    public Long getIdTransacciones() {
        return idTransacciones;
    }

    public void setIdTransacciones(Long idTransacciones) {
        this.idTransacciones = idTransacciones;
    }

    public double getMontoPago() {
        return montoPago;
    }

    public void setMontoPago(double montoPago) {
        this.montoPago = montoPago;
    }

    public Integer getUsuarioFinal() {
        return usuarioFinal;
    }

    public void setUsuarioFinal(Integer usuarioFinal) {
        this.usuarioFinal = usuarioFinal;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getNumAutorizacion() {
        return numAutorizacion;
    }

    public void setNumAutorizacion(String numAutorizacion) {
        this.numAutorizacion = numAutorizacion;
    }

    public int getStatusAprobado() {
        return statusAprobado;
    }

    public void setStatusAprobado(int statusAprobado) {
        this.statusAprobado = statusAprobado;
    }

    public String getNumTransaccion() {
        return numTransaccion;
    }

    public void setNumTransaccion(String numTransaccion) {
        this.numTransaccion = numTransaccion;
    }

    public Long getIdventaViaje() {
        return idventaViaje;
    }

    public void setIdventaViaje(Long idventaViaje) {
        this.idventaViaje = idventaViaje;
    }

    public Long getIdVentaTour() {
        return idVentaTour;
    }

    public void setIdVentaTour(Long idVentaTour) {
        this.idVentaTour = idVentaTour;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

}
