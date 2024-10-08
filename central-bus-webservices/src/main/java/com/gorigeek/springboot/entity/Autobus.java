package com.gorigeek.springboot.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity 
@Table(name = "t_autobus")
public class Autobus {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idt_autobus;
	
	@Column(name = "modelo")
    private String modelo;
	
    //@Column(name = "numeroAutobus")
    @Column(name = "numero_autobus")
    private String numeroAutobus;
	
	@Column(name = "marca")
	private String marca;
	
	@Column(name = "placas")
	private String placas;
	
	@OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
	@JoinColumn(name = "c_tipotransporte")
    private TipoTransporte tipoTransporte;

	@Column(name = "sanitario")
	private int sanitario;
	
	//@Column(name = "asientosAdultos")
	@Column(name = "asientos_adultos")
	private int asientosAdultos;
	
	//@Column(name = "AsientosNinios")
	@Column(name = "Asientos_ninios")
	private int AsientosNinios;
	
	//@Column(name = "asientosInapam")
	@Column(name = "asientos_inapam")
	private int asientosInapam;
	
	@OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
	//@JoinColumn(name = "t_afiliado_idt_afiliado")
	@JoinColumn(name = "t_afiliado")
	private Afiliado afiliado;
	
	@OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
	//@JoinColumn(name = "c_statusdisponibilidad_idc_statusdisponibilidad")
	@JoinColumn(name = "c_disponibilidad")
	private Disponibilidad disponibilidad;
	
	//@Column(name = "asientosInapam")
    @Column(name = "estatus_seleccionAsientos")
    private int seleccionAsientos;
	
	
	public Autobus() {
		
	}


    public Autobus(Long idt_autobus, String modelo, String numeroAutobus, String marca, String placas,
            TipoTransporte tipoTransporte, int sanitario, int asientosAdultos, int asientosNinios, int asientosInapam,
            Afiliado afiliado, Disponibilidad disponibilidad, int seleccionAsientos ) {
        super();
        this.idt_autobus = idt_autobus;
        this.modelo = modelo;
        this.numeroAutobus = numeroAutobus;
        this.marca = marca;
        this.placas = placas;
        this.tipoTransporte = tipoTransporte;
        this.sanitario = sanitario;
        this.asientosAdultos = asientosAdultos;
        AsientosNinios = asientosNinios;
        this.asientosInapam = asientosInapam;
        this.afiliado = afiliado;
        this.disponibilidad = disponibilidad;
        this.seleccionAsientos = seleccionAsientos;
    }


    public Long getIdt_autobus() {
        return idt_autobus;
    }


    public void setIdt_autobus(Long idt_autobus) {
        this.idt_autobus = idt_autobus;
    }


    public String getModelo() {
        return modelo;
    }


    public void setModelo(String modelo) {
        this.modelo = modelo;
    }


    public String getNumeroAutobus() {
        return numeroAutobus;
    }


    public void setNumeroAutobus(String numeroAutobus) {
        this.numeroAutobus = numeroAutobus;
    }


    public String getMarca() {
        return marca;
    }


    public void setMarca(String marca) {
        this.marca = marca;
    }


    public String getPlacas() {
        return placas;
    }


    public void setPlacas(String placas) {
        this.placas = placas;
    }


    public TipoTransporte getTipoTransporte() {
        return tipoTransporte;
    }


    public void setTipoTransporte(TipoTransporte tipoTransporte) {
        this.tipoTransporte = tipoTransporte;
    }


    public int getSanitario() {
        return sanitario;
    }


    public void setSanitario(int sanitario) {
        this.sanitario = sanitario;
    }


    public int getAsientosAdultos() {
        return asientosAdultos;
    }


    public void setAsientosAdultos(int asientosAdultos) {
        this.asientosAdultos = asientosAdultos;
    }


    public int getAsientosNinios() {
        return AsientosNinios;
    }


    public void setAsientosNinios(int asientosNinios) {
        AsientosNinios = asientosNinios;
    }


    public int getAsientosInapam() {
        return asientosInapam;
    }


    public void setAsientosInapam(int asientosInapam) {
        this.asientosInapam = asientosInapam;
    }


    public Afiliado getAfiliado() {
        return afiliado;
    }


    public void setAfiliado(Afiliado afiliado) {
        this.afiliado = afiliado;
    }


    public Disponibilidad getDisponibilidad() {
        return disponibilidad;
    }


    public void setDisponibilidad(Disponibilidad disponibilidad) {
        this.disponibilidad = disponibilidad;
    }
    
    public int getSeleccionAsientos() {
        return seleccionAsientos;
    }

    public void setSeleccionAsientos(int seleccionAsientos) {
        this.seleccionAsientos = seleccionAsientos;
    }

}
