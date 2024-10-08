package com.gorigeek.springboot.entity;

import javax.persistence.*;


@Entity
@Table(name="t_tarjetas_usuarios")
public class TarjetaUsuarios {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name ="idt_tarjetas_usuarios")
    private Long idtTarjetasUsuarios;
    
   // @Id
   // @GeneratedValue(strategy = GenerationType.IDENTITY)
    //private Long idt_tarjetas_usuarios;
    
    //@OneToOne(cascade={CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @Column(name="t_usuarios_final")
    private Long usuarioFinal;
    
    @Column(name="numero_tarjeta")
    private String numeroTarjeta;
    
    @Column(name="fecha_vigencia")
    private String fechaVigencia;
    
    @Column(name="titular")
    private String titular;
    
    @Column(name="correo")
    private String correo;
    
    //@Column(name="c_tipo_tarjeta")
    //private String tipoTarjeta;
    
 //  @OneToOne(cascade={CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
   // @JoinColumn(name="c_tipo_tarjeta")
    @OneToOne(cascade={CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "c_tipo_tarjeta")
    private TipoTarjeta tipoTarjeta;
    
   // @Column(name = "c_tipo_tarjeta")
    //private Long tipoTarjeta;
    
    @Column(name="tel")
    private String tel;

    public TarjetaUsuarios() {
        
    }

    public TarjetaUsuarios(Long idt_tarjetas_usuarios, Long usuarioFinal, String numeroTarjeta,
            String fechaVigencia, String titular, String correo, TipoTarjeta tipoTarjeta, String tel) {
        super();
        this.idtTarjetasUsuarios = idt_tarjetas_usuarios;
        this.usuarioFinal = usuarioFinal;
        this.numeroTarjeta = numeroTarjeta;
        this.fechaVigencia = fechaVigencia;
        this.titular = titular;
        this.correo = correo;
        this.tipoTarjeta = tipoTarjeta;
        this.tel = tel;
    }

    public Long getIdt_tarjetas_usuarios() {
        return idtTarjetasUsuarios;
    }

    public void setIdt_tarjetas_usuarios(Long idt_tarjetas_usuarios) {
        this.idtTarjetasUsuarios = idt_tarjetas_usuarios;
    }

    public Long getUsuarioFinal() {
        return usuarioFinal;
    }

    public void setUsuarioFinal(Long usuarioFinal) {
        this.usuarioFinal = usuarioFinal;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public String getFechaVigencia() {
        return fechaVigencia;
    }

    public void setFechaVigencia(String fechaVigencia) {
        this.fechaVigencia = fechaVigencia;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public TipoTarjeta getTipoTarjeta() {
        return tipoTarjeta;
    }

    public void setTipoTarjeta(TipoTarjeta tipoTarjeta) {
        this.tipoTarjeta = tipoTarjeta;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
    
    
}
