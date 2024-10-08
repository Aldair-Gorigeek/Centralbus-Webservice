package com.gorigeek.springboot.entity;

import java.util.ArrayList;
import java.util.List;

public class ListNotify {
    private List<DetalleNotificacionMovil> listaNotificacionesPendientes = new ArrayList<DetalleNotificacionMovil>();
    
    public ListNotify() {
    }

    public List<DetalleNotificacionMovil> getListaNotificacionesPendientes() {
        return listaNotificacionesPendientes;
    }

    public void setListaNotificacionesPendientes(List<DetalleNotificacionMovil> listaNotificacionesPendientes) {
        this.listaNotificacionesPendientes = listaNotificacionesPendientes;
    }
    
}
