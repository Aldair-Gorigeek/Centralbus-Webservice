package com.gorigeek.springboot.entity;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.gorigeek.crypto.Encrypt;
@Entity
@Table(name = "t_detalle_ruta")
public class RutasDetalles {
	@Id
	@Column(name ="idt_detalle_ruta")
	private Long idt_detalle_ruta;
	
	@OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "t_ruta")
    private Rutas t_ruta;
	   
	@OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
	@JoinColumn(name = "t_terminales_origen")
	private Terminales idTerminalOrigen;
	
	@OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
	@JoinColumn(name = "t_terminales_destino")
	private Terminales idTerminalDestino;

	@Column(name ="secuencia")
    private String secuencia;
	   
	@Column(name ="tiempo_ruta")
	private String tiempoRuta;

	public RutasDetalles() {
		
	}
	
    public RutasDetalles(RutasDetalles c) {
        this.idt_detalle_ruta = c.idt_detalle_ruta;
        this.t_ruta = c.t_ruta;
        this.idTerminalOrigen = c.idTerminalOrigen;
        this.idTerminalDestino = c.idTerminalDestino;
        this.secuencia = c.secuencia;
        this.tiempoRuta = c.tiempoRuta;
      }
	
	public RutasDetalles(Long idt_detalle_ruta, Rutas t_ruta, Terminales idTerminalOrigen, Terminales idTerminalDestino,String secuencia, String tiempoRuta, 
	        String adulto, 
	        String nino, 
	        String inapam,
	        String hora_salida, 
	        String t_horarios_salida,
	        String tiempoRutas
	        ) {
		super();
		this.idt_detalle_ruta = idt_detalle_ruta;
		this.t_ruta = t_ruta;
		this.idTerminalOrigen = idTerminalOrigen;
		this.idTerminalDestino = idTerminalDestino;
		this.tiempoRuta = tiempoRuta;
	}


    public Long getIdt_detalle_ruta() {
        return idt_detalle_ruta;
    }


    public void setIdt_detalle_ruta(Long idt_detalle_ruta) {
        this.idt_detalle_ruta = idt_detalle_ruta;
    }


    public Rutas getT_ruta() {
        return t_ruta;
    }


    public void setT_ruta(Rutas t_ruta) {
        this.t_ruta = t_ruta;
    }


    public Terminales getIdTerminalOrigen() {
        return idTerminalOrigen;
    }


    public void setIdTerminalOrigen(Terminales idTerminalOrigen) {
        this.idTerminalOrigen = idTerminalOrigen;
    }


    public Terminales getIdTerminalDestino() {
        return idTerminalDestino;
    }


    public void setIdTerminalDestino(Terminales idTerminalDestino) {
        this.idTerminalDestino = idTerminalDestino;
    }


    public String getSecuencia() {
        return secuencia;
    }


    public void setSecuencia(String secuencia) {
        this.secuencia = secuencia;
    }


    public String getTiempoRuta() {
        return tiempoRuta;
    }


    public void setTiempoRuta(String tiempoRuta) {
        this.tiempoRuta = tiempoRuta;
    }
    
    /*
    public String getPorcentaje_descuento() {
        return porcentaje_descuento;
    }


    public void setPorcentaje_descuento(String porcentaje_descuento) {
        this.porcentaje_descuento = porcentaje_descuento;
    }


    public int getDias_anticipacion() {
        return dias_anticipacion;
    }


    public void setDias_anticipacion(int dias_anticipacion) {
        this.dias_anticipacion = dias_anticipacion;
    }


    public int getT_afiliado() {
        return t_afiliado;
    }


    public void setT_afiliado(int t_afiliado) {
        this.t_afiliado = t_afiliado;
    }


    public int getC_tiporuta() {
        return c_tiporuta;
    }


    public void setC_tiporuta(int c_tiporuta) {
        this.c_tiporuta = c_tiporuta;
    }

    public String getDias_periodo() {
        return dias_periodo;
    }


    public void setDias_periodo(String dias_periodo) {
        this.dias_periodo = dias_periodo;
    }


    public String getFecha_periodo() {
        return fecha_periodo;
    }


    public void setFecha_periodo(String fecha_periodo) {
        this.fecha_periodo = fecha_periodo;
    }
     */
    /*
    public String getAdulto() {
        return adulto;
    }


    public void setAdulto(String adulto) {
        this.adulto = adulto;
    }


    public String getNino() {
        return nino;
    }


    public void setNino(String nino) {
        this.nino = nino;
    }


    public String getInapam() {
        return inapam;
    }


    public void setInapam(String inapam) {
        this.inapam = inapam;
    }

    
    public String getT_horarios_salida() {
        return t_horarios_salida;
    }
     */

    /*
    public void setT_horarios_salida(String t_horarios_salida) {
        this.t_horarios_salida = t_horarios_salida;
    }
    
    public String getHora_salida() {
    return hora_salida;
    }*/

	
	
}
