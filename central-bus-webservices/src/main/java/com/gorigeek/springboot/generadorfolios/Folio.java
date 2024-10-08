package com.gorigeek.springboot.generadorfolios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;


public class Folio {

    /*
     * Autor: Luis Sanchez Parametro 1: id del afiliado Metodo encargado para
     * obtener el primer digito del id del afiliado
     */
    public String extraerAfiliado(String id) {
        String subId = "0";
        if (id.length() > 1) {
            subId = id.substring(0, 1);
        } else if (id.length() == 1) {
            subId = id;
        } else {
            subId = "1";
        }
        return subId;
    }

    public String extraerNumeroAsiento(String nAsiento) {
        String subnAsiento = "0";
        if (nAsiento.length() > 1) {
            subnAsiento = nAsiento.substring(0, 1);
        } else if (nAsiento.length() == 1) {
            subnAsiento = nAsiento;
        } else {
            subnAsiento = "0";
        }
        return subnAsiento;
    }

    /*
     * Autor: Luis Sanchez Metodo encargado para generar folio de 8 digitos, el
     * metodo se genera con los datos de id ultimo registro en BD, Hora, fecha e id
     * de usuario
     */
    public String extraerFolio(String ultimoRegistros, String hora, String fecha, String idUsuario, String nAsiento,
            Integer i) {
        int suma = 0;
        String cadena = "";
        Random r = new Random();
        String[] separarFecha = fecha.split("-");
        String[] separarHora = hora.split(":");
        suma = Integer.parseInt((ultimoRegistros == "") ? "0" : ultimoRegistros) + 1;
        String numeroAsiento = (Integer.parseInt(extraerNumeroAsiento(nAsiento)) + i) + "";
        int random = r.nextInt(9);
        int randomDia = r.nextInt(3);
        String idUser = "0";
        if (ultimoRegistros.length() >= 1 && ultimoRegistros.length() <= 7) {
            if (idUsuario.length() == 1) {
                idUser = idUsuario;
            } else {
                idUser = idUsuario.substring(0, 1);
            }
            Integer sumaDato = Integer.parseInt(ultimoRegistros) + 1;

            switch (((ultimoRegistros).contains("9") ? (sumaDato + "").length() : ultimoRegistros.length())) {
            case 1:
                cadena = ultimoRegistros.length() + "" + random + "" + suma + "" + extraerNumeroAsiento(numeroAsiento)
                        + "" + randomDia + separarFecha[1].substring(1, 2) + separarHora[1];
                break;
            case 2:
                cadena = ultimoRegistros.length() + "" + random + "" + suma + "" + extraerNumeroAsiento(numeroAsiento)
                        + "" + randomDia + separarFecha[1].substring(1, 2) + separarHora[1].substring(1, 2);
                break;
            case 3:
                cadena = ultimoRegistros.length() + "" + random + "" + suma + "" + extraerNumeroAsiento(numeroAsiento)
                        + "" + randomDia + separarHora[1].substring(0, 1);
                break;
            case 4:
                cadena = ultimoRegistros.length() + "" + random + "" + suma + "" + extraerNumeroAsiento(numeroAsiento)
                        + "" + randomDia;
                break;
            case 5:
                cadena = ultimoRegistros.length() + "" + random + "" + suma + "" + extraerNumeroAsiento(numeroAsiento);
                break;
            case 6:
                cadena = ultimoRegistros.length() + "" + random + "" + suma;
                break;
            case 7:
                cadena = ultimoRegistros.length() + "" + suma;
                break;
            default:
                cadena = "9" + random + "9" + extraerNumeroAsiento(numeroAsiento) + "" + randomDia
                        + separarFecha[1].substring(1, 2) + separarHora[1];
                break;
            }
        } else if (ultimoRegistros.length() == 0) {
            cadena = "1" + "" + random + "1" + extraerNumeroAsiento(numeroAsiento) + "" + randomDia
                    + separarFecha[1].substring(1, 2) + separarHora[1];
        } else {
            cadena = "9" + random + "9" + extraerNumeroAsiento(numeroAsiento) + "" + randomDia
                    + separarFecha[1].substring(1, 2) + separarHora[1];
        }
        return cadena;
    }

    public String sumarSegundo(String segundo, Integer valor) {
        String sumaSegundo = "0";
        String suma = (Integer.parseInt(segundo) + valor) + "";
        if (suma.length() > 1) {
            // separarHora[2].substring(0, 1)
            sumaSegundo = (Integer.parseInt(suma.substring(1, 2)) + 1) + "";
        } else if (suma.length() == 1) {
            sumaSegundo = (Integer.parseInt(suma) + 1) + "";
        } else {
            sumaSegundo = "0";
        }

        return (sumaSegundo == "2") ? "0" : sumaSegundo.substring(0, 1);
    }

    /*
     * Autor: Luis Sanchez Metodo encargado de obtener datos y crear folio de 15
     * digitos Se espera recibir los siguientes datos todos tipo String:
     * fecha:yyyy-MM-dd hora: hh:mm:ss idcAfiliado: id Usuario: folio BD: numero de
     * asiento: id de autobus:
     */
    public ArrayList<?> separarDatos(String fecha, String hora, String idAiliado, String idUser, String folioBD,
            String nAsiento, String idAutobus, int numeroFolios, String tpBoleto) {

        ArrayList<String> responseJson = new ArrayList();
        String dv1 = (tpBoleto.equals("0")) ? "0" : "3";
        String dv2 = (tpBoleto.equals("0")) ? "1" : "7";

        if (numeroFolios == 1) {

            String[] separarFecha = fecha.split("-");
            String[] separarHora = hora.split(":");
            String folioCifrado = "";
            folioCifrado = extraerFolio(folioBD, hora, fecha, idUser, nAsiento, 0);

            responseJson.add(dv1 + folioCifrado.substring(3, 5) + separarHora[2].substring(1, 2)
                    + separarFecha[2].substring(1, 2) + separarHora[2].substring(0, 1) + folioCifrado.substring(7, 8)
                    + extraerAfiliado(idAiliado) + separarFecha[2].substring(0, 1) + dv2 + folioCifrado.substring(1, 3)
                    + folioCifrado.substring(0, 1) + folioCifrado.substring(5, 7));

        } else {
            for (int i = 0; i < numeroFolios; i++) {
                String[] separarHora = hora.split(":");

                String[] separarFecha = fecha.split("-");
                String folioCifrado = "";
                String sumarDia = (Integer.parseInt(separarFecha[1]) + i) + "";
                String fechaDia = separarFecha[0] + "-" + ((sumarDia.length() == 1) ? "0" + sumarDia : sumarDia) + "-"
                        + separarFecha[2];
                String sumarMinutos = (Integer.parseInt(separarHora[1]) + i) + "";
                String horaMinutos = separarHora[0] + ":"
                        + ((sumarMinutos.length() == 1) ? "0" + sumarMinutos : sumarMinutos) + ":" + separarHora[2];
                Integer sumaFolio = (Integer.parseInt((folioBD.equals("") ? "0" : folioBD)) + i);

                folioCifrado = extraerFolio(sumaFolio.toString(), horaMinutos, fechaDia, idUser, nAsiento, i);

                responseJson.add(dv1 + folioCifrado.substring(3, 5) + sumarSegundo(separarHora[2].substring(1, 2), i)
                        + separarFecha[2].substring(1, 2) + sumarSegundo(separarHora[2].substring(0, 1), i)
                        + folioCifrado.substring(7, 8) + separarFecha[2].substring(0, 1)+extraerAfiliado(idAiliado) + dv2
                        + folioCifrado.substring(1, 3) + folioCifrado.substring(0, 1) + folioCifrado.substring(5, 7)
                        );

            }
        }

        return responseJson;
    }

    public static void main(String[] args) {
        Folio f = new Folio();

        System.err.println("" + f.separarDatos("2023-08-25", "13:00:00", "2", "18", "2", "10", "25", 30, "0"));

    }

}
