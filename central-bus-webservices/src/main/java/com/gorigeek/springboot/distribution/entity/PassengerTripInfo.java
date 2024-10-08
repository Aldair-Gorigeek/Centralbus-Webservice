package com.gorigeek.springboot.distribution.entity;

import java.time.LocalDateTime;

public class PassengerTripInfo {
    private Long id;
    private LocalDateTime fechaCompra;
    private LocalDateTime fechaViaje;

    public PassengerTripInfo(Long id, LocalDateTime fechaCompra, LocalDateTime fechaViaje) {
        this.id = id;
        this.fechaCompra = fechaCompra;
        this.fechaViaje = fechaViaje;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }   

    public LocalDateTime getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDateTime fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public LocalDateTime getFechaViaje() {
        return fechaViaje;
    }

    public void setFechaViaje(LocalDateTime fechaViaje) {
        this.fechaViaje = fechaViaje;
    }

 
}
