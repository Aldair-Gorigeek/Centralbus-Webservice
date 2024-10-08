package com.gorigeek.springboot.entity;

import javax.persistence.*;


@Entity
@Table(name="t_tour")
public class TourMovil {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idt_tour;
    
    @Column(name="nombre_tour")
    private String nombreTour;
    
    //@Column(name="c_estados_origen")
    //private String estadoOrigen;
    
    @OneToOne(cascade={CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name="c_estados_origen")
    private Estado estadoOrigen;
    
    @OneToOne(cascade={CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name="c_ciudades_origen")
    private Ciudad ciudadesOrigen;
    
    @OneToOne(cascade={CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name="c_estados_destino")
    private Estado estadoDestino;
    
    @OneToOne(cascade={CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name="c_ciudades_destino")
    private Ciudad ciudadesDestino;
    
    @Column(name="lugares_visita")
    private String lugaresVisita;
    
    @Column(name="fecha_hora_salida")
    private String fechaHoraSalida; //SE REPITE EN LA TABLA
    
    @Column(name="fecha_hora_regreso")
    private String fechaHoraRegreso; //SE REPITE EN LA TABLA
    
    @Column(name="descuento")
    private String descuento;
    
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "t_afiliado")
    private Afiliado afiliado;
    
    //@Column(name="t_afiliado")
    //private String afiliado;
    
    @Column(name="estatus_activo")
    private String statusActivo;
    
    @Column(name="dias_anticipado")
    private String diasAnticipado;
    
    //@Column(name="fechaRegreso")
    //private String fechaRegreso;
    
    //@Column(name="fechaSalida")
    //private String fechaSalida;
    
    //@Column(name="horaRegreso")
    //private String horaRegreso;
    
    //@Column(name="horaSalida")
    //private String horaSalida;
    
    //@Column(name="precioadulto")
    //private String precioAdulto;
    
    //@Column(name="precioinapam")
    //private String precioInapam;
    
    //@Column(name="precionino")
    //private String precioNino;
    
    //@Column(name="statusViaje")
    //private String statusViaje;
    
    //@Column(name="t_afiliado_idt_afiliado")
    //private String afiliadoAfiliado;
           
    //@Column(name="t_autobus_idt_autobus")
    //private String autobus;
    
 // @Column(name="c_ciudades_idc_ciudadDestino")
    //private String ciudadDestino;
    
    //@Column(name="c_ciudades_idc_ciudadOrigen")
    //private String ciudadOrigen;
    
    //@Column(name="c_estados_idc_estadoDestino")
   // private String estadosDestino;
    
    //@Column(name="c_estados_idc_estadoOrigen")
    //private String estadosOrigen;
    
    public TourMovil() {
        
    }

    public TourMovil(Long idt_tour, String nombreTour, Estado estadoOrigen, Ciudad ciudadesOrigen, Estado estadoDestino,
            Ciudad ciudadesDestino, String lugaresVisita, String fechaHoraSalida, String fechaHoraRegreso,
            String descuento, Afiliado afiliado, String statusActivo, String diasAnticipado) {
        super();
        this.idt_tour = idt_tour;
        this.nombreTour = nombreTour;
        this.estadoOrigen = estadoOrigen;
        this.ciudadesOrigen = ciudadesOrigen;
        this.estadoDestino = estadoDestino;
        this.ciudadesDestino = ciudadesDestino;
        this.lugaresVisita = lugaresVisita;
        this.fechaHoraSalida = fechaHoraSalida;
        this.fechaHoraRegreso = fechaHoraRegreso;
        this.descuento = descuento;
        this.afiliado = afiliado;
        this.statusActivo = statusActivo;
        this.diasAnticipado = diasAnticipado;
    }

    public Long getIdt_tour() {
        return idt_tour;
    }

    public void setIdt_tour(Long idt_tour) {
        this.idt_tour = idt_tour;
    }

    public String getNombreTour() {
        return nombreTour;
    }

    public void setNombreTour(String nombreTour) {
        this.nombreTour = nombreTour;
    }

    public Estado getEstadoOrigen() {
        return estadoOrigen;
    }

    public void setEstadoOrigen(Estado estadoOrigen) {
        this.estadoOrigen = estadoOrigen;
    }

    public Ciudad getCiudadesOrigen() {
        return ciudadesOrigen;
    }

    public void setCiudadesOrigen(Ciudad ciudadesOrigen) {
        this.ciudadesOrigen = ciudadesOrigen;
    }

    public Estado getEstadoDestino() {
        return estadoDestino;
    }

    public void setEstadoDestino(Estado estadoDestino) {
        this.estadoDestino = estadoDestino;
    }

    public Ciudad getCiudadesDestino() {
        return ciudadesDestino;
    }

    public void setCiudadesDestino(Ciudad ciudadesDestino) {
        this.ciudadesDestino = ciudadesDestino;
    }

    public String getLugaresVisita() {
        return lugaresVisita;
    }

    public void setLugaresVisita(String lugaresVisita) {
        this.lugaresVisita = lugaresVisita;
    }

    public String getFechaHoraSalida() {
        return fechaHoraSalida;
    }

    public void setFechaHoraSalida(String fechaHoraSalida) {
        this.fechaHoraSalida = fechaHoraSalida;
    }

    public String getFechaHoraRegreso() {
        return fechaHoraRegreso;
    }

    public void setFechaHoraRegreso(String fechaHoraRegreso) {
        this.fechaHoraRegreso = fechaHoraRegreso;
    }

    public String getDescuento() {
        return descuento;
    }

    public void setDescuento(String descuento) {
        this.descuento = descuento;
    }

    public Afiliado getAfiliado() {
        return afiliado;
    }

    public void setAfiliado(Afiliado afiliado) {
        this.afiliado = afiliado;
    }

    public String getStatusActivo() {
        return statusActivo;
    }

    public void setStatusActivo(String statusActivo) {
        this.statusActivo = statusActivo;
    }

    public String getDiasAnticipado() {
        return diasAnticipado;
    }

    public void setDiasAnticipado(String diasAnticipado) {
        this.diasAnticipado = diasAnticipado;
    }
    
        
    

}
