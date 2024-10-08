package com.gorigeek.springboot.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "detalle_venta")
public class DetalleVentasModel implements Serializable {

    @Id
    private String id_detalle_venta;
    private String t_venta_viajes;
    
/*  @OneToOne
    @JoinColumn(name = "t_venta_viajes", referencedColumnName = "idt_venta_viajes", insertable = false, updatable = false)
    TVentasViajesModel tVentasViajes;
*/
    public String getId_detalle_venta() {
        return id_detalle_venta;
    }

    public void setId_detalle_venta(String id_detalle_venta) {
        this.id_detalle_venta = id_detalle_venta;
    }

    public String getT_venta_viajes() {
        return t_venta_viajes;
    }

    public void setT_venta_viajes(String t_venta_viajes) {
        this.t_venta_viajes = t_venta_viajes;
    }

    private static final long serialVersionUID = 1L;

}
