package com.gorigeek.springboot.entity;

import javax.persistence.*;

@Entity
@Table(name = "t_terminales")
public class Terminales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idt_terminales")
    private Long idTerminales;
    
    @Column(name = "nombre_terminal")
    private String nombreterminal;
    
    @Column(name = "direccion")
    private String direccion;
    
    @Column(name = "c_estatus_terminal")
    private String statusTerminal;
    
    @Column(name = "codigo_postal")
    private String codigoPostal;
    
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "t_afiliado")
    private Afiliado afiliado; 
    
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH,CascadeType.DETACH})
    @JoinColumn(name = "c_estados")
    private Estado estados;
    
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "c_ciudades")
    private Ciudad ciudad;
    
    public Terminales() {}

    public Terminales(Long idt_terminales, String nombreterminal, String direccion, Afiliado afiliado,
            Estado estados, Ciudad ciudad) {
        super();
        this.idTerminales = idt_terminales;
        this.nombreterminal = nombreterminal;
        this.direccion = direccion;
        this.afiliado = afiliado;
        this.estados = estados;
        this.ciudad = ciudad;
    }

    
    public String getStatusTerminal() {
        return statusTerminal;
    }

    public void setStatusTerminal(String statusTerminal) {
        this.statusTerminal = statusTerminal;
    }

    public Long getIdt_terminales() {
        return idTerminales;
    }


    public void setIdt_terminales(Long idt_terminales) {
        this.idTerminales = idt_terminales;
    }


    public String getNombreterminal() {
        return nombreterminal;
    }


    public void setNombreterminal(String nombreterminal) {
        this.nombreterminal = nombreterminal;
    }


    public String getDireccion() {
        return direccion;
    }


    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }


    public Estado getEstados() {
        return estados;
    }


    public void setEstados(Estado estados) {
        this.estados = estados;
    }


    public Ciudad getCiudad() {
        return ciudad;
    }


    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    public Afiliado getAfiliado() {
        return afiliado;
    }

    public void setAfiliado(Afiliado afiliado) {
        this.afiliado = afiliado;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }
    
    
}
