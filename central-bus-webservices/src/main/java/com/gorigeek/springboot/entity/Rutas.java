package com.gorigeek.springboot.entity;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import com.gorigeek.crypto.Encrypt;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.persistence.*;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name="t_ruta")
public class Rutas {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idt_ruta")
	private Long idt_ruta;
	
	@Column(name = "nombre_ruta")
	private String nombreRuta;
	
   @Column(name = "fecha_creacion_ruta")
    private String fechaCreacioRuta;
   
    @Column(name = "porcentaje_descuento")
    private String porcentajeDescuento;
	    
	@Column(name = "dias_anticipacion")
	private int diasAnticipacion;
	
	@OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
	@JoinColumn(name = "c_tiporuta")
    private TipoRuta id_tipoRuta;
	
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "t_afiliado")
    private Afiliado t_afiliado; 
   
    @Column(name = "dias_periodo")
    private String diasPeriodo;

    @Column(name = "fecha_periodo")
    private String fechaPeriodo;
    
    //Se debe agregar esta columna a la base de datos
    @Column(name = "c_status_ruta")
    private String statusRuta;
	
	 public Rutas() {
	        super();
    }

    public Rutas(Long idt_ruta, String nombreRuta, String fechaCreacioRuta, String porcentajeDescuento, int diasAnticipacion, TipoRuta id_tipoRuta, Afiliado t_afiliado, String diasPeriodo, String fechaPeriodo) {
        super();
        this.idt_ruta = idt_ruta;
        this.nombreRuta = nombreRuta;
        this.fechaCreacioRuta = fechaCreacioRuta;
        this.porcentajeDescuento = porcentajeDescuento;
        this.diasAnticipacion = diasAnticipacion;
        this.id_tipoRuta = id_tipoRuta;
        this.t_afiliado = t_afiliado;
        this.diasPeriodo = diasPeriodo;
        this.fechaPeriodo = fechaPeriodo;
    }
    

    public String getStatusRuta() {
        return statusRuta;
    }

    public void setStatusRuta(String statusRuta) {
        this.statusRuta = statusRuta;
    }

    public Long getIdt_ruta() {
        return idt_ruta;
    }

    public void setIdt_ruta(Long idt_ruta) {
        this.idt_ruta = idt_ruta;
    }

    public String getNombreRuta() {
        return nombreRuta;
    }

    public void setNombreRuta(String nombreRuta) {
        this.nombreRuta = nombreRuta;
    }

    public String getFechaCreacioRuta() {
        return fechaCreacioRuta;
    }

    public void setFechaCreacioRuta(String fechaCreacioRuta) {
        this.fechaCreacioRuta = fechaCreacioRuta;
    }

    public String getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(String porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public int getDiasAnticipacion() {
        return diasAnticipacion;
    }

    public void setDiasAnticipacion(int diasAnticipacion) {
        this.diasAnticipacion = diasAnticipacion;
    }

    public TipoRuta getId_tipoRuta() {
        return id_tipoRuta;
    }

    public void setId_tipoRuta(TipoRuta id_tipoRuta) {
        this.id_tipoRuta = id_tipoRuta;
    }

    public Afiliado getT_afiliado() {
        return t_afiliado;
    }

    public void setT_afiliado(Afiliado t_afiliado) {
        this.t_afiliado = t_afiliado;
    }

    public String getDiasPeriodo() {
        return diasPeriodo;
    }

    public void setDiasPeriodo(String diasPeriodo) {
        this.diasPeriodo = diasPeriodo;
    }

    public String getFechaPeriodo() {
        return fechaPeriodo;
    }

    public void setFechaPeriodo(String fechaPeriodo) {
        this.fechaPeriodo = fechaPeriodo;
    }

    
    
}
