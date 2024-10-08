package com.gorigeek.springboot.schedule;

import java.util.ArrayList;
import java.util.List;

import com.gorigeek.springboot.entity.DatosNotificacion;

/*
 * Se crea metodo para poder utilizar Lista de notificaciones de manera global
 * */
public class ListaNotificaciones {

    private List<DatosNotificacion> listaNotificaciones;

    private static ListaNotificaciones instance;

    private ListaNotificaciones() {
        listaNotificaciones = new ArrayList<>();
    }

    public static ListaNotificaciones getInstance() {

        if (instance == null) {

            synchronized (ListaNotificaciones.class) {
                if (instance == null) {
                    instance = new ListaNotificaciones();
                }
            }
        }

        return instance;
    }

    public ArrayList<DatosNotificacion> getArrayList() {
        return (ArrayList<DatosNotificacion>) listaNotificaciones;
    }

}
