package com.gorigeek.springboot.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_venta_viajes")
public class TVentasViajesModel implements Serializable {

	@Id
	private String idt_venta_viajes;
	private String t_afiliado;

	public String getIdt_venta_viajes() {
		return idt_venta_viajes;
	}

	public void setIdt_venta_viajes(String idt_venta_viajes) {
		this.idt_venta_viajes = idt_venta_viajes;
	}

	public String getT_afiliado() {
		return t_afiliado;
	}

	public void setT_afiliado(String t_afiliado) {
		this.t_afiliado = t_afiliado;
	}

	private static final long serialVersionUID = 1L;
}
