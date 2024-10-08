package com.gorigeek.springboot.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gorigeek.springboot.Module;
import com.gorigeek.springboot.entity.DetalleVentaTourModel;
import com.gorigeek.springboot.entity.DetalleVentasModel;
import com.gorigeek.springboot.entity.TAfiliado;
import com.gorigeek.springboot.entity.TUsuariosAdmin;
import com.gorigeek.springboot.service.GenerarFolioService;
import com.gorigeek.springboot.generadorfolios.Folio;
import com.gorigeek.springboot.generadorfolios.ValidarDatos;

@RestController
public class GenerarFolioController {
    @Autowired
    private GenerarFolioService service;

    @SuppressWarnings("unchecked")
    @Module("CentralBus - General/Generar Folio")
    @PostMapping("/generarFolio")
    public ArrayList<?> generarFolio(@Param("idUser") String idUser, @Param("idAfiliado") String idAfiliado,
            @Param("nAsiento") String nAsiento, @Param("idAutobus") String idAutobus,
            @Param("nBoletos") String nBoletos, @Param("tpBoleto") String tpBoleto,
            @Param("fechaViaje") String fechaViaje) {

        nAsiento = (nAsiento.isEmpty()) ? "00" : nAsiento;

        ArrayList<String> json = new ArrayList<String>();

        Date datetime = new Date();
        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(datetime);
        String hora = new SimpleDateFormat("HH:mm:ss").format(datetime);

        try {
            if (fecha.isEmpty() || hora.isEmpty() || idUser.isEmpty() || idAfiliado.isEmpty() || nAsiento.isEmpty()
                    || idAutobus.isEmpty() || nBoletos.isEmpty()) {
                json.add("Datos vacios");
                return json;
            } else {

                String fecha1 = new SimpleDateFormat("yyyy-MM-dd").format(datetime);
                String[] fechaString = fechaViaje.split(" ");
                String fecha2 = fechaString[0];
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate fechaActual = LocalDate.parse(fecha1, formatter);
                LocalDate fechaV = LocalDate.parse(fecha2, formatter);

                List<TAfiliado> datosAfiliado = service.getAfiliado(idAfiliado);
                if (datosAfiliado.size() > 0) {
                    if (datosAfiliado.get(0).getC_statuscuenta_activo().equals("1")) {
                        boolean banderUser = false;
                        if (idUser.equals("0")) {
                            banderUser = true;
                            // agregamos condicion para insert
                        } else {
                            List<TUsuariosAdmin> datosUsuario = service.getUsuario(idUser);
                            banderUser = (datosUsuario.size() > 0) ? true : false;
                        }
                        System.err.println("Bandera: " + banderUser);

                        if (banderUser == true) {
                            System.err.println("SI PASO A generar");
                            System.err.println(fechaV + " === " + fechaActual);
                            ValidarDatos obj = new ValidarDatos();
                            System.err.println(fechaV.isEqual(fechaActual));
                            System.err.println(fechaV.isAfter(fechaActual));

                            if (fechaV.isEqual(fechaActual) || fechaV.isAfter(fechaActual)) {
                                if (!obj.validarFecha(fecha)) {
                                    json.add("Formato fecha incorrecto");
                                    return json;
                                } else if (!obj.validarHora(hora)) {
                                    json.add("Formato hora incorrecto");
                                    return json;
                                } else if (Integer.parseInt(nBoletos) == 0) {
                                    json.add("Numero de boletos incorrecto");
                                    return json;
                                } else {
                                    if (tpBoleto.equals("0")) {
                                        List<DetalleVentasModel> datos = service.getData(idAfiliado);
                                        System.err.println("BOLETO " + datos.size());
                                        if (datos.size() > 0) {
                                            Folio g = new Folio();
                                            ArrayList<String> folioGenerado = (ArrayList<String>) g.separarDatos(fecha,
                                                    hora, idAfiliado, idUser, datos.get(0).getId_detalle_venta(),
                                                    nAsiento, idAutobus, Integer.parseInt(nBoletos), "0");

                                            Date date = new Date();
                                            String fechaI = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                                    .format(datetime);
                                            System.err.println("Fecha: " + fechaI);

                                            for (int i = 0; i < folioGenerado.size(); i++) {
                                                System.out.println("Folio: " + folioGenerado.get(i));
                                                if (idUser.equals("0")) {
                                                    int insertBD = service.insertFolioSinUsuario(
                                                            Integer.parseInt(idAfiliado), folioGenerado.get(i), "0",
                                                            "1", fechaViaje, fechaI, Integer.parseInt(idAutobus), "0");
                                                    System.err.println("SE inserta a BD= " + insertBD);
                                                } else {
                                                    int insertBD = service.insertFolio(Integer.parseInt(idUser),
                                                            Integer.parseInt(idAfiliado), folioGenerado.get(i), "0",
                                                            "1", fechaViaje, fechaI, Integer.parseInt(idAutobus), "0");
                                                    System.err.println("SE inserta a BD= " + insertBD);

                                                }

                                            }
                                            return folioGenerado;
                                        } else {
                                            System.err.println("PASO AL ELSE");
                                            Folio g = new Folio();
                                            ArrayList<String> folioGenerado = (ArrayList<String>) g.separarDatos(fecha,
                                                    hora, idAfiliado, idUser, "0", nAsiento, idAutobus,
                                                    Integer.parseInt(nBoletos), "0");
                                            Date date = new Date();
                                            String fechaI = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                                    .format(datetime);
                                            System.err.println("Fecha: " + fechaI);
                                            for (int i = 0; i < folioGenerado.size(); i++) {
                                                System.out.println("Folio: " + folioGenerado.get(i));

                                                if (idUser.equals("0")) {
                                                    int insertBD = service.insertFolioSinUsuario(
                                                            Integer.parseInt(idAfiliado), folioGenerado.get(i), "0",
                                                            "1", fechaViaje, fechaI, Integer.parseInt(idAutobus), "0");
                                                } else {
                                                    int insertBD = service.insertFolio(Integer.parseInt(idUser),
                                                            Integer.parseInt(idAfiliado), folioGenerado.get(i), "0",
                                                            "1", fechaViaje, fechaI, Integer.parseInt(idAutobus), "0");
                                                }

                                            }
                                            return folioGenerado;
                                        }
                                    } else if (tpBoleto.equals("1")) {
                                        List<DetalleVentaTourModel> datos = null;
                                        try {
                                            datos = service.obtenerUltimoFolioTour(idAfiliado);

                                        } catch (Exception e) {
                                            System.err.println(e.getMessage());
                                        }
                                        System.err.println("Tamaño:  " + datos.size());
                                        if (datos.size() > 0) {
                                            System.out.println("ID: " + datos.get(0).getId_detalle_venta_tour());
                                            Folio g = new Folio();
                                            ArrayList<String> folioGenerado = (ArrayList<String>) g.separarDatos(fecha,
                                                    hora, idAfiliado, idUser, datos.get(0).getId_detalle_venta_tour(),
                                                    nAsiento, idAutobus, Integer.parseInt(nBoletos), "1");
                                            Date date = new Date();
                                            String fechaI = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                                    .format(datetime);
                                            System.err.println("Fecha: " + fechaI);
                                            for (int i = 0; i < folioGenerado.size(); i++) {
                                                System.out.println("Folio: " + folioGenerado.get(i));
                                                if (idUser.equals("0")) {
                                                    int insertBD = service.insertFolioSinUsuario(
                                                            Integer.parseInt(idAfiliado), folioGenerado.get(i), "3",
                                                            "7", fechaViaje, fechaI, Integer.parseInt(idAutobus), "1");
                                                } else {
                                                    int insertBD = service.insertFolio(Integer.parseInt(idUser),
                                                            Integer.parseInt(idAfiliado), folioGenerado.get(i), "3",
                                                            "7", fechaViaje, fechaI, Integer.parseInt(idAutobus), "1");

                                                }

                                            }
                                            return folioGenerado;

                                        } else {
                                            Folio g = new Folio();
                                            ArrayList<String> folioGenerado = (ArrayList<String>) g.separarDatos(fecha,
                                                    hora, idAfiliado, idUser, "0", nAsiento, idAutobus,
                                                    Integer.parseInt(nBoletos), "1");
                                            Date date = new Date();
                                            String fechaI = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                                    .format(datetime);
                                            System.err.println("Fecha: " + fechaI);
                                            for (int i = 0; i < folioGenerado.size(); i++) {
                                                if (idUser.equals("0")) {
                                                    int insertBD = service.insertFolioSinUsuario(
                                                            Integer.parseInt(idAfiliado), folioGenerado.get(i), "3",
                                                            "7", fechaViaje, fechaI, Integer.parseInt(idAutobus), "1");
                                                } else {
                                                    int insertBD = service.insertFolio(Integer.parseInt(idUser),
                                                            Integer.parseInt(idAfiliado), folioGenerado.get(i), "3",
                                                            "7", fechaViaje, fechaI, Integer.parseInt(idAutobus), "1");
                                                }

                                            }
                                            return folioGenerado;
                                        }
                                    } else {
                                        json.add("Error tipo boleto");
                                        return json;
                                    }

                                }
                            } else {
                                json.add("Fecha invalida");
                                return json;
                            }
                        } else {
                            json.add("El usuario no existe");
                            return json;
                        }
                    } else {
                        json.add("Afiliano no activo");
                        return json;
                    }

                } else {
                    json.add("El afiliado no existe");
                    return json;
                }

            }

        } catch (Exception e) {
            // Validar error
            json.add("Error api");
            return json;

        }

    }

}