package com.gorigeek.springboot.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_imagenes_tour")
public class ImagenesTour {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idt_imagenes_tour;
    
    @Column(name="imagen_ruta")
    private String imagenRuta;
    
    @Column(name="t_tour")
    private Long tour;

    public ImagenesTour() {
        
    }

    public ImagenesTour(Long idt_imagenes_tour, String imagenRuta, Long tour) {
        super();
        this.idt_imagenes_tour = idt_imagenes_tour;
        this.imagenRuta = imagenRuta;
        this.tour = tour;
    }

    public Long getIdt_imagenes_tour() {
        return idt_imagenes_tour;
    }

    public void setIdt_imagenes_tour(Long idt_imagenes_tour) {
        this.idt_imagenes_tour = idt_imagenes_tour;
    }

    public String getImagenRuta() {
        return imagenRuta;
    }

    public void setImagenRuta(String imagenRuta) {
        this.imagenRuta = imagenRuta;
    }

    public Long getTour() {
        return tour;
    }

    public void setTour(Long tour) {
        this.tour = tour;
    }

    
            
}
