package com.gorigeek.springboot.generadorfolios;

public class ValidarDatos {
    public boolean validarFecha(String fecha) {
        boolean validar = false;

        String[] separarFecha = fecha.split("-");
        System.err.println("DATOS" + separarFecha[0].length());

        if (fecha.length() != 10) {
            validar = false;
        } else if (!fecha.contains("-")) {
            validar = false;
        } else if (!fecha.matches("[0-9-]+")) {
            validar = false;
        } else if (separarFecha[0].length() != 4 && !fecha.matches("[0-9]+")) {
            validar = false;
        } else if (separarFecha[1].length() != 2 && !fecha.matches("[0-9]+")) {
            validar = false;
        } else if (separarFecha[2].length() != 2 && !fecha.matches("[0-9]+")) {
            validar = false;
        } else {
            validar = true;
        }

        return validar;
    }

    public boolean validarHora(String hora) {
        boolean validar = false;
        String[] separarHora = hora.split(":");

        if (hora.length() != 8) {
            validar = false;
        } else if (!hora.contains(":")) {
            validar = false;
        } else if (!hora.matches("[0-9:]+")) {
            validar = false;
        } else if (separarHora[0].length() != 2 && !hora.matches("[0-9]+")) {
            validar = false;
        } else if (separarHora[1].length() != 2 && !hora.matches("[0-9]+")) {
            validar = false;
        } else if (separarHora[2].length() != 2 && !hora.matches("[0-9]+")) {
            validar = false;
        } else {
            validar = true;
        }

        return validar;
    }

}
