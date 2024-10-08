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
@Table(name = "t_usuariosadmin")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idt_usuariosadmin")
	private Long idtUsuariosadmin;
	
	@Column(name = "nombre")
	private String nombre;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "pass")
	private String pass;
	
	@Column(name = "seed")
	private String seed;
	
	@Column(name = "fechaRegistro")
	private String fechaRegistro;
	
	@Column(name = "telefono")
	private Long telefono;
	
	@Column(name = "direccion")
	private String direccion;
	
	@OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
	@JoinColumn(name = "idc_tipousuario")
	private TipoUsuario idc_tipousuario;
	
	@OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
	@JoinColumn(name = "idt_afiliado")
	private Afiliado idt_afiliado;
	
	/*@OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
	@JoinColumn(name = "idc_statusrecordar")
	private StatusRecordar idc_statusrecordar;*/
	
	/*@Column(name = "c_statusCuenta_idc_statusCuenta")
	private Integer c_statusCuenta_idc_statusCuenta;*/
	
	@OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
	@JoinColumn(name = "c_statusCuenta_idc_statusCuenta")
	private StatusCuenta c_status_cuenta_idc_status_cuenta;
	
	//@OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
	//@JoinColumn(name = "c_tiporegistro_idc_tiporegistro")
	//private TipoRegistro c_tiporegistro_idc_tiporegistro;
	
	@Column(name = "codigoverificacion")
	private Integer codigoVerificacion;
	
	public User(){}
	

	public User(String nombre, String email, String pass, String seed, String fechaRegistro,
			Long telefono, String direccion, TipoUsuario idc_tipousuario, Afiliado idt_afiliado, 
			StatusCuenta c_status_cuenta_idc_status_cuenta, 
			Integer codigoVerificacion) {
		super();
		this.nombre = nombre;
		this.email = email;
		this.pass = pass;
		this.seed = seed;
		this.fechaRegistro = fechaRegistro;
		this.telefono = telefono;
		this.direccion = direccion;
		this.idc_tipousuario = idc_tipousuario;
		this.idt_afiliado = idt_afiliado;
		//this.idc_statusrecordar = idc_statusrecordar;
		this.c_status_cuenta_idc_status_cuenta = c_status_cuenta_idc_status_cuenta;
		
		this.codigoVerificacion = codigoVerificacion;
	}

	public Long getIdtUsuariosadmin() {
		return idtUsuariosadmin;
	}

	public void setIdtUsuariosadmin(Long idtUsuariosadmin) {
		this.idtUsuariosadmin = idtUsuariosadmin;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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

	public String getSeed() {
		return seed;
	}

	public void setSeed(String seed) {
		this.seed = seed;
	}

	public String getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(String fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public Long getTelefono() {
		return telefono;
	}

	public void setTelefono(Long telefono) {
		this.telefono = telefono;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public TipoUsuario getIdc_tipousuario() {
		return idc_tipousuario;
	}

	public void setIdc_tipousuario(TipoUsuario idc_tipousuario) {
		this.idc_tipousuario = idc_tipousuario;
	}

	public Afiliado getIdt_afiliado() {
		return idt_afiliado;
	}

	public void setIdt_afiliado(Afiliado idt_afiliado) {
		this.idt_afiliado = idt_afiliado;
	}

	/*public StatusRecordar getIdc_statusrecordar() {
		return idc_statusrecordar;
	}

	public void setIdc_statusrecordar(StatusRecordar idc_statusrecordar) {
		this.idc_statusrecordar = idc_statusrecordar;
	}
*/
	public StatusCuenta getC_status_cuenta_idc_status_cuenta() {
		return c_status_cuenta_idc_status_cuenta;
	}

	public void setC_status_cuenta_idc_status_cuenta(StatusCuenta c_status_cuenta_idc_status_cuenta) {
		this.c_status_cuenta_idc_status_cuenta = c_status_cuenta_idc_status_cuenta;
	}

	//public TipoRegistro getC_tiporegistro_idc_tiporegistro() {
		//return c_tiporegistro_idc_tiporegistro;
	//}

	//public void setC_tiporegistro_idc_tiporegistro(TipoRegistro c_tiporegistro_idc_tiporegistro) {
		//this.c_tiporegistro_idc_tiporegistro = c_tiporegistro_idc_tiporegistro;
	//}

	public Integer getCodigoVerificacion() {
		return codigoVerificacion;
	}

	public void setCodigoVerificacion(Integer codigoVerificacion) {
		this.codigoVerificacion = codigoVerificacion;
	}
	
	
	

}
