package com.gorigeek.springboot.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "detalle_venta_tour")
public class DetalleVentaTourModel implements Serializable {


    @Id
    private String id_detalle_venta_tour;
    private String t_venta_tour;
    
    public String getId_detalle_venta_tour() {
        return id_detalle_venta_tour;
    }
    public void setId_detalle_venta_tour(String id_detalle_venta_tour) {
        this.id_detalle_venta_tour = id_detalle_venta_tour;
    }
    public String getT_venta_tour() {
        return t_venta_tour;
    }
    public void setT_venta_tour(String t_venta_tour) {
        this.t_venta_tour = t_venta_tour;
    }

    private static final long serialVersionUID = 1L;
/*  @OneToOne
    @JoinColumn(name = "t_venta_tour", referencedColumnName = "idt_venta_tour", insertable = false, updatable = false)
    TVentasTourModel tVentasTour;
*/

}
