package com.gorigeek.springboot.distribution.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.text.Normalizer;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.mail.MessagingException;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.gorigeek.springboot.entity.Transacciones;
import com.gorigeek.springboot.entity.UserFinal;
import com.gorigeek.springboot.entity.DTO.DetallesTicketDTO;
import com.gorigeek.springboot.exception.ResourceNotFoundException;
import com.gorigeek.springboot.distribution.entity.ComisionDistribution;
import com.gorigeek.springboot.distribution.entity.ReservationDetailRequest;
import com.gorigeek.springboot.distribution.entity.ReservationsConfirmRequest;
import com.gorigeek.springboot.distribution.entity.ReservationsCreateRequest;
import com.gorigeek.springboot.distribution.entity.Response;
import com.gorigeek.springboot.distribution.entity.ventas.*;
import com.gorigeek.springboot.distribution.repository.ComisionDistributionRepository;
import com.gorigeek.springboot.distribution.repository.VentasRepository;
import com.gorigeek.springboot.distribution.entity.VancancyTripResponse;
import com.gorigeek.springboot.distribution.entity.VentasDistribution;
import com.gorigeek.springboot.distribution.utils.RequestToDistributionUtils;
import com.gorigeek.springboot.distribution.utils.BarcodeToPDFDistribution;
import com.gorigeek.springboot.repository.RegistrarCuentaRepository;
import com.gorigeek.springboot.repository.TransaccionRepository;
import com.gorigeek.springboot.util.EmailService;

@Service
public class VentasDistributionService {
    @Value("${iva}")
    private double IVA;

    @Autowired
    private VentasRepository ventasRepository;
    @Autowired
    private ComisionDistributionRepository comisionRepository;
    @Autowired
    private RegistrarCuentaRepository userRepository;
    @Autowired
    private TransaccionRepository repoTransaccion;
    @Autowired
    private EmailService emailService;
    private static final Logger logger = LogManager.getLogger(DistributionRetailerService.class);

    @Autowired
    private RequestToDistributionUtils requestUtils;

    @SuppressWarnings({ "unchecked", "null" })
    @Transactional
    public JSONObject reservationsDetail(ReservationDetailRequest body) {
        JSONObject resp = new JSONObject();
        List<VentasDistribution> details = new ArrayList<>();
        Integer totalPriceInCents = 0;
        String bookingId = null;
        UserFinal user = null;
        JsonNode data = null;

        // PROMO99, CONTADORES PARA VALIDAR BOLETOS QUE TENDRAN PRECIO 99
        int adultSeatsDiscountIda = 0;
        int adultSeatsDiscountVuelta = 0;
        int childSeatsDiscountIda = 0;
        int childSeatsDiscountVuelta = 0;
        int inapamSeatsDiscountIda = 0;
        int inapamSeatsDiscountVuelta = 0;
        Map<String, String> foliosMap = new HashMap<>();
        try {
            List<ComisionDistribution> comisiones = comisionRepository.findAll();
            final String endpoint = "/reservations/detail?idReservation=" + body.getIdReservation();
            Response<JsonNode> requestResponse = requestUtils.request(endpoint, "GET", null);
            if (body.getIdUser() != null) {
                user = userRepository.findById(body.getIdUser())
                        .orElse(null);
            }

            LocalDateTime fechaHora = LocalDateTime.now();

            int totalPint = 0;
            int totalPcil = 0;
            int totalPypo = 0;
            int totalPnos = 0;
            int totalPsoe = 0;
            if (requestResponse.getCode() == 200) {
                // Se agrega el bucle para asegurar que se consiga, si o si los folios
                while (requestResponse.getCode() == 200) {
                    data = requestResponse.getBody().get(0);
                    Iterator<JsonNode> serialCodes = requestResponse.getBody().get(1).elements();
                    String status = requestResponse.getBody().get(0).get("attributes").get("state").asText();

                    if (status.equals("failed")) {
                        String nombreCompleto = body.getConfirmRequest().getFirstName() + " "
                                + body.getConfirmRequest().getLastName();
                        String correo = body.getEmail();
                        System.err.println(body.getIdReservation() + " ** Boletos no generados para el usuario: "
                                + nombreCompleto + " con el correo: " + correo + ".");
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("error", "failed");
                        return jsonObject;
                    }
                    // Crear una lista para almacenar los elementos que cumplan con el criterio
                    foliosMap = new HashMap<>();
                    // Recorrer los elementos del JsonNode folios
                    while (serialCodes.hasNext()) {
                        JsonNode elemento = serialCodes.next();
                        // Verificar si el elemento tiene el atributo "type" y su valor es "passengers"
                        if (elemento.has("type") && elemento.get("type").asText().equals("passengers")) {
                            // Agregar el elemento a la lista de folios
                            foliosMap.put(elemento.get("id").asText(),
                                    elemento.get("attributes").get("serial_code").asText());
                        }
                    }
                    if (!foliosMap.isEmpty()) {
                        //
                        break; // Salir del bucle si se obtienen elementos válidos
                    }
                    // Volver a realizar la petición si no se obtienen elementos válidos
                    requestResponse = requestUtils.request(endpoint, "GET", null);
                }
                totalPriceInCents = convertirPesosACentavos(body.getCosto());

                Iterator<JsonNode> includedElements = requestResponse.getBody().get(1).elements();
                while (includedElements.hasNext()) {
                    JsonNode includedElement = includedElements.next();
                    if ("bookings".equals(includedElement.get("type").asText())) {
                        bookingId = includedElement.get("id").asText();
                        break;
                    }
                }
                // Obtener precio de los boletos de la clase vacancy
                VancancyTripResponse prices = body.getVacancyResponse();

                ReservationsCreateRequest reservationsCreated = body.getReservationsCreateRequest();
                ReservationsConfirmRequest confirmRequest = body.getConfirmRequest();
                // obtener pasajeros;
                List<PassengerReservationsCreate> passengers = reservationsCreated.getPassengers();
                // obtener nombres pasajeros
                List<PassengerReservationsConfirm> names = confirmRequest.getPassengers();

                for (int i = 0; i < passengers.size(); i++) {
                    PassengerReservationsCreate passenger = passengers.get(i);
                    // obtener asientos del pasajero
                    List<Seat> seats = passenger.getSeats();
                    if (seats != null) {
                        // Ordenar los asientos por segmentIndex
                        Collections.sort(seats, new Comparator<Seat>() {
                            @Override
                            public int compare(Seat seat1, Seat seat2) {
                                // Comparar los segmentIndex
                                return Integer.compare(seat1.getSegmentIndex(), seat2.getSegmentIndex());
                            }
                        });
                    }

                    PassengerReservationsConfirm name = names.get(i);
                    if (seats != null) {
                        for (int j = 0; j < seats.size(); j++) {
                            Seat seat = seats.get(j);
                            JsonNode attributes = data.get("attributes");
                            VentasDistribution ventasDistribution = new VentasDistribution();

                            ventasDistribution
                                    .setFechaHoraCompra(fechaHora.toString());
                            if (user != null) {
                                ventasDistribution.setIdUsuarioFinal(user.getIdtUsuariosfinal());
                            } else {
                                // Si es invitado
                                ventasDistribution.setIdUsuarioFinal(null);
                            }
                            if (seat.getSegmentIndex() == 1) {
                                ventasDistribution
                                        .setFechaHoraViaje(
                                                parseFechaHora(reservationsCreated.getReturnDepartureTime()));
                                ventasDistribution.setOrigen(body.getDestino());
                                ventasDistribution.setDestino(body.getOrigen());
                            } else {
                                ventasDistribution
                                        .setFechaHoraViaje(parseFechaHora(reservationsCreated.getDepartureTime()));
                                ventasDistribution.setOrigen(body.getOrigen());
                                ventasDistribution.setDestino(body.getDestino());
                            }
                            ventasDistribution.setNumAsiento(seat.getSeatCode());
                            ventasDistribution.setNombrePasajero(name.getFirstName() + " " + name.getLastName());

                            // Setear folio si existe
                            String lastNameWithoutAccents = removeAccentsAndReplaceSpaces(
                                    name.getLastName().toUpperCase());
                            String firstNameWithoutAccents = removeAccentsAndReplaceSpaces(
                                    name.getFirstName().toUpperCase());

                            String idFolio = passenger.getType() + "-" + lastNameWithoutAccents + "-"
                                    + firstNameWithoutAccents + "-OUTBOUND";
                            try {
                                List<String> coincidentFolio = getFolios(foliosMap, idFolio);
                                // Eliminar la primera clave coincidente del mapa para evitar repeticiones
                                if (!coincidentFolio.isEmpty()) {
                                    foliosMap.remove(coincidentFolio.get(0));
                                }
                                ventasDistribution
                                        .setFolioBoleto(coincidentFolio.get(1));
                            } catch (Exception e) {
                                ventasDistribution.setFolioBoleto(null);
                            }
                            String type = passenger.getType();
                            ventasDistribution.setEmailUsuario(body.getEmail());
                            int[] response = asignarPrecioBoleto(type, prices, ventasDistribution,
                                    (seat.getSegmentIndex() == 1) ? body.getIsDiscountActivatedReturn()
                                            : body.getIsDiscountActivatedGo(),
                                    body.getDiscountRate(), totalPint, totalPcil,
                                    totalPypo, totalPnos,
                                    totalPsoe, body.getIsRedondo() ? true : false);
                            if (seat.getSegmentIndex() == 1) {
                                // redondo
                                switch (type) {
                                    case "PCIL":
                                        childSeatsDiscountVuelta += 1;
                                        if (body.getChildSeatsDiscountVuelta() != null
                                                && body.getChildSeatsDiscountVuelta() > 0) {
                                            if (body.getChildSeatsDiscountVuelta() >= childSeatsDiscountVuelta) {                                                
                                                ventasDistribution.setDescuento(ventasDistribution.getPrecioBoletoSinDescuento().toString());
                                                ventasDistribution.setPrecioBoleto(99.00);
                                            } else {
                                                ventasDistribution.setDescuento(null);
                                            }
                                        }
                                        break;
                                    case "PNOS":
                                        adultSeatsDiscountVuelta += 1;
                                        if (body.getAdultSeatsDiscountVuelta() != null
                                                && body.getAdultSeatsDiscountVuelta() > 0) {
                                            if (body.getAdultSeatsDiscountVuelta() >= adultSeatsDiscountVuelta) {
                                                ventasDistribution.setDescuento(ventasDistribution.getPrecioBoletoSinDescuento().toString());
                                                ventasDistribution.setPrecioBoleto(99.00);
                                            } else {
                                                ventasDistribution.setDescuento(null);
                                            }
                                        }
                                        break;
                                    case "PSOE":
                                        inapamSeatsDiscountVuelta += 1;
                                        if (body.getInapamSeatsDiscountVuelta() != null
                                                && body.getInapamSeatsDiscountVuelta() > 0) {
                                            if (body.getInapamSeatsDiscountVuelta() >= inapamSeatsDiscountVuelta) {
                                                ventasDistribution.setDescuento(ventasDistribution.getPrecioBoletoSinDescuento().toString());
                                                ventasDistribution.setPrecioBoleto(99.00);
                                            } else {
                                                ventasDistribution.setDescuento(null);
                                            }
                                        }
                                        break;
                                }
                            } else {
                                switch (type) {
                                    case "PCIL":
                                        childSeatsDiscountIda += 1;
                                        if (body.getChildSeatsDiscountIda() != null
                                                && body.getChildSeatsDiscountIda() > 0) {
                                            if (body.getChildSeatsDiscountIda() >= childSeatsDiscountIda) {
                                                ventasDistribution.setDescuento(ventasDistribution.getPrecioBoletoSinDescuento().toString());
                                                ventasDistribution.setPrecioBoleto(99.00);
                                            } else {
                                                ventasDistribution.setDescuento(null);
                                            }
                                        }
                                        break;
                                    case "PNOS":
                                        adultSeatsDiscountIda += 1;
                                        if (body.getAdultSeatsDiscountIda() != null
                                                && body.getAdultSeatsDiscountIda() > 0) {
                                            if (body.getAdultSeatsDiscountIda() >= adultSeatsDiscountIda) {
                                                ventasDistribution.setDescuento(ventasDistribution.getPrecioBoletoSinDescuento().toString());
                                                ventasDistribution.setPrecioBoleto(99.00);
                                            } else {
                                                ventasDistribution.setDescuento(null);
                                            }
                                        }
                                        break;
                                    case "PSOE":
                                        inapamSeatsDiscountIda += 1;
                                        if (body.getInapamSeatsDiscountVuelta() != null
                                                && body.getInapamSeatsDiscountVuelta() > 0) {
                                            if (body.getInapamSeatsDiscountIda() >= inapamSeatsDiscountIda) {
                                                ventasDistribution.setDescuento(ventasDistribution.getPrecioBoletoSinDescuento().toString());
                                                ventasDistribution.setPrecioBoleto(99.00);
                                            } else {
                                                ventasDistribution.setDescuento(null);
                                            }
                                        }
                                        break;
                                }

                            }
                            totalPint = response[0];
                            totalPcil = response[1];
                            totalPypo = response[2];
                            totalPnos = response[3];
                            totalPsoe = response[4];
                            ventasDistribution.setLineaTransporte(body.getLineaTransporte());
                            ventasDistribution.setIdReservacion(data.get("id").asText());
                            ventasDistribution.setLogo(convertirBase64ABytes(body.getLogo()));
                            // Agregar monto de comision
                            for (ComisionDistribution v : comisiones) {
                                if (v.getCarrier().trim()
                                        .equals(body.getReservationsCreateRequest().getMarketingCarrier().trim())) {
                                    ventasDistribution.setComisionPorcentaje(v.getComision());

                                    double comision = Double.parseDouble(v.getComision());
                                    double comisionMonto = ventasDistribution.getPrecioBoletoSinDescuento()
                                            * (comision / 100.0);

                                    // Usar BigDecimal para formatear comisionMonto a dos decimales
                                    BigDecimal comisionMontoBD = new BigDecimal(comisionMonto);
                                    comisionMontoBD = comisionMontoBD.setScale(2, RoundingMode.HALF_UP);

                                    ventasDistribution.setComisionMonto(comisionMontoBD.toString());
                                    break;
                                }
                            }

                            VentasDistribution savedVenta = new VentasDistribution();
                            savedVenta = ventasRepository.save(ventasDistribution);
                            if (savedVenta != null) {
                                // guardar transaccion
                                Transacciones transaccion = new Transacciones();
                                // PROMO99,  el precio original/sin descuento del boleto, se guardara en descuento. Indicaciones del Ing. Romeo
                                if (savedVenta.getDescuento() != null && savedVenta.getDescuento().trim().length() > 2) {
                                    transaccion.setMontoPago(99);
                                } else {
                                    transaccion.setMontoPago(savedVenta.getPrecioBoleto());
                                }

                                if (user != null) {
                                    transaccion.setUsuarioFinal(Long.valueOf(user.getIdtUsuariosfinal()).intValue());
                                }
                                transaccion.setFechaHora(fechaHora.toString());
                                transaccion.setNumAutorizacion(body.getnAutorizacion());
                                transaccion.setStatusAprobado(1);
                                transaccion.setNumTransaccion(body.getIdTransaccion());
                                transaccion.setCorreo(body.getConfirmRequest().getEmail());
                                transaccion.setIdVentasDistribusion(savedVenta.getId());
                                repoTransaccion.save(transaccion);
                            }
                            details.add(ventasDistribution);

                        }
                    } else {
                        if (body.getIsRedondo() == true) {                            
                            for (int x = 0; x < 2; x++) {
                                JsonNode attributes = data.get("attributes");
                                VentasDistribution ventasDistribution = new VentasDistribution();
                                ventasDistribution
                                        .setFechaHoraCompra(fechaHora.toString());
                                if (user != null) {
                                    ventasDistribution.setIdUsuarioFinal(user.getIdtUsuariosfinal());
                                } else {
                                    // Si es invitado
                                    ventasDistribution.setIdUsuarioFinal(null);
                                }

                                if (x == 1) {
                                    ventasDistribution
                                            .setFechaHoraViaje(
                                                    parseFechaHora(reservationsCreated.getReturnDepartureTime()));
                                    ventasDistribution.setOrigen(body.getDestino());
                                    ventasDistribution.setDestino(body.getOrigen());
                                } else {
                                    ventasDistribution
                                            .setFechaHoraViaje(parseFechaHora(reservationsCreated.getDepartureTime()));
                                    ventasDistribution.setOrigen(body.getOrigen());
                                    ventasDistribution.setDestino(body.getDestino());
                                }
                                ventasDistribution.setNumAsiento(null);
                                ventasDistribution.setNombrePasajero(name.getFirstName() + " " + name.getLastName());
                                // Setear folio si existe
                                String lastNameWithoutAccents = removeAccentsAndReplaceSpaces(
                                        name.getLastName().toUpperCase());
                                String firstNameWithoutAccents = removeAccentsAndReplaceSpaces(
                                        name.getFirstName().toUpperCase());

                                String idFolio = passenger.getType() + "-" + lastNameWithoutAccents + "-"
                                        + firstNameWithoutAccents + "-OUTBOUND";
                                try {
                                    List<String> coincidentFolio = getFolios(foliosMap, idFolio);
                                    // Eliminar la primera clave coincidente del mapa para evitar repeticiones
                                    if (!coincidentFolio.isEmpty()) {
                                        foliosMap.remove(coincidentFolio.get(0));
                                    }
                                    ventasDistribution
                                            .setFolioBoleto(coincidentFolio.get(1));
                                } catch (Exception e) {
                                    ventasDistribution.setFolioBoleto(null);
                                }
                                ventasDistribution.setEmailUsuario(body.getEmail());
                                String type = passenger.getType();
                                int[] response = asignarPrecioBoleto(type, prices, ventasDistribution,
                                        (x == 1) ? body.getIsDiscountActivatedReturn()
                                                : body.getIsDiscountActivatedGo(),
                                        body.getDiscountRate(), totalPint,
                                        totalPcil,
                                        totalPypo, totalPnos,
                                        totalPsoe, body.getIsRedondo() ? true : false);
                                totalPint = response[0];
                                totalPcil = response[1];
                                totalPypo = response[2];
                                totalPnos = response[3];
                                totalPsoe = response[4];

                                ventasDistribution.setLineaTransporte(body.getLineaTransporte());
                                ventasDistribution.setIdReservacion(data.get("id").asText());
                                ventasDistribution.setLogo(convertirBase64ABytes(body.getLogo()));
                                // Agregar monto de comision
                                for (ComisionDistribution v : comisiones) {
                                    if (v.getCarrier().trim()
                                            .equals(body.getReservationsCreateRequest().getMarketingCarrier().trim())) {
                                        ventasDistribution.setComisionPorcentaje(v.getComision());

                                        double comision = Double.parseDouble(v.getComision());
                                        double comisionMonto = ventasDistribution.getPrecioBoletoSinDescuento()
                                                * (comision / 100.0);

                                        // Usar BigDecimal para formatear comisionMonto a dos decimales
                                        BigDecimal comisionMontoBD = new BigDecimal(comisionMonto);
                                        comisionMontoBD = comisionMontoBD.setScale(2, RoundingMode.HALF_UP);

                                        ventasDistribution.setComisionMonto(comisionMontoBD.toString());
                                        break;
                                    }
                                }

                                VentasDistribution savedVenta = new VentasDistribution();
                                savedVenta = ventasRepository.save(ventasDistribution);
                                if (savedVenta != null) {
                                    // guardar transaccion
                                    Transacciones transaccion = new Transacciones();
                                    // preguntar si existe el registro de transaccion
//                                    if (repoTransaccion.existsByNumTransaccion(body.getIdTransaccion())) {
//                                        transaccion = repoTransaccion.findByNumTransaccion(body.getIdTransaccion());
//                                    }

                                    transaccion.setMontoPago(savedVenta.getPrecioBoleto());

                                    if (user != null) {
                                        transaccion
                                                .setUsuarioFinal(Long.valueOf(user.getIdtUsuariosfinal()).intValue());
                                    }
                                    transaccion.setFechaHora(fechaHora.toString());
                                    transaccion.setNumAutorizacion(body.getnAutorizacion());
                                    transaccion.setStatusAprobado(1);
                                    transaccion.setNumTransaccion(body.getIdTransaccion());
                                    transaccion.setCorreo(body.getConfirmRequest().getEmail());
                                    transaccion.setIdVentasDistribusion(savedVenta.getId());
                                    repoTransaccion.save(transaccion);
                                }
                                details.add(ventasDistribution);
                            }
                        } else {
                            System.err.println("No es redondo?");
                            JsonNode attributes = data.get("attributes");
                            VentasDistribution ventasDistribution = new VentasDistribution();
                            ventasDistribution
                                    .setFechaHoraCompra(fechaHora.toString());
                            if (user != null) {
                                ventasDistribution.setIdUsuarioFinal(user.getIdtUsuariosfinal());
                            } else {
                                // Si es invitado
                                ventasDistribution.setIdUsuarioFinal(null);
                            }
                            ventasDistribution
                                    .setFechaHoraViaje(parseFechaHora(reservationsCreated.getDepartureTime()));
                            ventasDistribution.setOrigen(body.getOrigen());
                            ventasDistribution.setDestino(body.getDestino());

                            ventasDistribution.setNumAsiento(null);
                            ventasDistribution.setNombrePasajero(name.getFirstName() + " " + name.getLastName());
                            // Setear folio si existe
                            String lastNameWithoutAccents = removeAccentsAndReplaceSpaces(
                                    name.getLastName().toUpperCase());
                            String firstNameWithoutAccents = removeAccentsAndReplaceSpaces(
                                    name.getFirstName().toUpperCase());

                            String idFolio = passenger.getType() + "-" + lastNameWithoutAccents + "-"
                                    + firstNameWithoutAccents + "-OUTBOUND";
                            try {
                                List<String> coincidentFolio = getFolios(foliosMap, idFolio);
                                // Eliminar la primera clave coincidente del mapa para evitar repeticiones
                                if (!coincidentFolio.isEmpty()) {
                                    foliosMap.remove(coincidentFolio.get(0));
                                }
                                ventasDistribution
                                        .setFolioBoleto(coincidentFolio.get(1));
                            } catch (Exception e) {
                                ventasDistribution.setFolioBoleto(null);
                            }

                            ventasDistribution.setEmailUsuario(body.getEmail());
                            String type = passenger.getType();
                            int[] response = asignarPrecioBoleto(type, prices, ventasDistribution,
                                    body.getIsDiscountActivatedGo(), body.getDiscountRate(), totalPint, totalPcil,
                                    totalPypo, totalPnos,
                                    totalPsoe, body.getIsRedondo() ? true : false);
                            totalPint = response[0];
                            totalPcil = response[1];
                            totalPypo = response[2];
                            totalPnos = response[3];
                            totalPsoe = response[4];
                            ventasDistribution.setLineaTransporte(body.getLineaTransporte());
                            ventasDistribution.setIdReservacion(data.get("id").asText());
                            ventasDistribution.setLogo(convertirBase64ABytes(body.getLogo()));
                            // Agregar monto de comision
                            for (ComisionDistribution v : comisiones) {
                                if (v.getCarrier().trim()
                                        .equals(body.getReservationsCreateRequest().getMarketingCarrier().trim())) {
                                    ventasDistribution.setComisionPorcentaje(v.getComision());

                                    double comision = Double.parseDouble(v.getComision());
                                    double comisionMonto = ventasDistribution.getPrecioBoletoSinDescuento()
                                            * (comision / 100.0);

                                    // Usar BigDecimal para formatear comisionMonto a dos decimales
                                    BigDecimal comisionMontoBD = new BigDecimal(comisionMonto);
                                    comisionMontoBD = comisionMontoBD.setScale(2, RoundingMode.HALF_UP);

                                    ventasDistribution.setComisionMonto(comisionMontoBD.toString());
                                    break;
                                }
                            }
                            VentasDistribution savedVenta = new VentasDistribution();
                            savedVenta = ventasRepository.save(ventasDistribution);
                            if (savedVenta != null) {
                                // guardar transaccion
                                Transacciones transaccion = new Transacciones();
                                // preguntar si existe el registro de transaccion
                                if (repoTransaccion.existsByNumTransaccion(body.getIdTransaccion())) {
                                    transaccion = repoTransaccion.findByNumTransaccion(body.getIdTransaccion());
                                }

                                transaccion.setMontoPago(savedVenta.getPrecioBoleto());

                                if (user != null) {
                                    transaccion.setUsuarioFinal(Long.valueOf(user.getIdtUsuariosfinal()).intValue());
                                }
                                transaccion.setFechaHora(fechaHora.toString());
                                transaccion.setNumAutorizacion(body.getnAutorizacion());
                                transaccion.setStatusAprobado(1);
                                transaccion.setNumTransaccion(body.getIdTransaccion());
                                transaccion.setCorreo(body.getConfirmRequest().getEmail());
                                transaccion.setIdVentasDistribusion(savedVenta.getId());
                                repoTransaccion.save(transaccion);
                            }
                            details.add(ventasDistribution);
                        }
                    }
                }

                String sendModifiedTicket = sendTicketEdited(bookingId, totalPriceInCents, body.getEmail(),
                        details, body.getNumTarjeta(), body.getnAutorizacion(), body.getIdTransaccion(),
                        body.getIsRedondo());
                if (sendModifiedTicket != null) {
                    resp.put("idVentaViaje", details.get(0).getId());
                    return resp;
                } else {
                    return null;
                }
            } else {
                logger.error("Se produjo un error al consumir /reservations/detail. Status code: " +
                        ((requestResponse.getCode() == 0) ? "Unknown" : requestResponse.getCode()));
                return null;
            }

        } catch (Exception e) {
            logger.error("¡Error en el método: reservationsDetail!" + e.getMessage(), e);
            return null;
        }
    }

    // Método para quitar los acentos de una cadena
    public static String removeAccentsAndReplaceSpaces(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        String withoutAccents = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return withoutAccents.replaceAll("\\s+", "-");
    }

    private boolean generarPDFyEnviarCorreo(boolean isRedondo, String email, JsonNode data,
            List<VentasDistribution> details, int totalPint, int totalPcil, int totalPypo, int totalPnos,
            int totalPsoe) throws MessagingException {
        BarcodeToPDFDistribution pdf = new BarcodeToPDFDistribution();
        boolean pdfCreado = false;
        if (isRedondo) {
            pdfCreado = pdf.boletoPDFRedondo(details, 0.16);
        } else {
            pdfCreado = pdf.boletoPDF(details, 0.16);
        }
        // Envío de correo electrónico - Se comenta termporalmente debido a la
        // certificacion de Distribution
//        emailService.sendEmailTicketDistribution(email, "Compra de boletos",
//                data.get("attributes").get("total_price").asText(), details, totalPint, totalPcil,
//                totalPypo, totalPnos,
//                totalPsoe,
//                isRedondo);
        // Si el PDF se creó satisfactoriamente, retornar true
        return pdfCreado;
    }

    private int[] asignarPrecioBoleto(String type, VancancyTripResponse prices, VentasDistribution ventasDistribution,
            Boolean isDiscountActivated, Integer discountRate,
            int totalPint, int totalPcil, int totalPypo, int totalPnos,
            int totalPsoe, Boolean isWithoutSeatRedondo) {
        // Setea comision si esta activo
        if (isDiscountActivated) {
            ventasDistribution.setDescuento(discountRate.toString());
        }

        int[] nuevosTotales = new int[5];
        nuevosTotales[0] = totalPint;
        nuevosTotales[1] = totalPcil;
        nuevosTotales[2] = totalPypo;
        nuevosTotales[3] = totalPnos;
        nuevosTotales[4] = totalPsoe;
        switch (type) {
            case "PINT":
                nuevosTotales[0] = totalPint + 1;
                ventasDistribution.setTipoBoleto("Infante");
                if (isWithoutSeatRedondo) {
                    ventasDistribution.setPrecioBoleto(
                            applyDiscountToPrice(isDiscountActivated, discountRate, (prices.getPint() / 2)));
                    ventasDistribution.setPrecioBoletoSinDescuento(prices.getPint() / 2);
                } else {
                    ventasDistribution.setPrecioBoleto(
                            applyDiscountToPrice(isDiscountActivated, discountRate, (prices.getPint())));
                    ventasDistribution.setPrecioBoletoSinDescuento(prices.getPint());
                }
                break;
            case "PCIL":
                nuevosTotales[1] = totalPcil + 1;
                ventasDistribution.setTipoBoleto("Niño");
                if (isWithoutSeatRedondo) {
                    ventasDistribution.setPrecioBoleto(
                            applyDiscountToPrice(isDiscountActivated, discountRate, (prices.getPcil() / 2)));
                    ventasDistribution.setPrecioBoletoSinDescuento(prices.getPcil() / 2);
                } else {
                    ventasDistribution
                            .setPrecioBoleto(applyDiscountToPrice(isDiscountActivated, discountRate, prices.getPcil()));
                    ventasDistribution.setPrecioBoletoSinDescuento(prices.getPcil());
                }
                break;
            case "PYPO":
                nuevosTotales[2] = totalPypo + 1;
                ventasDistribution.setTipoBoleto("Joven");
                if (isWithoutSeatRedondo) {
                    ventasDistribution.setPrecioBoleto(
                            applyDiscountToPrice(isDiscountActivated, discountRate, (prices.getPypo() / 2)));
                    ventasDistribution.setPrecioBoletoSinDescuento(prices.getPypo() / 2);
                } else {
                    ventasDistribution.setPrecioBoleto(
                            applyDiscountToPrice(isDiscountActivated, discountRate, prices.getPypo()));
                    ventasDistribution.setPrecioBoletoSinDescuento(prices.getPypo());
                }
                break;
            case "PNOS":
                nuevosTotales[3] = totalPnos + 1;
                ventasDistribution.setTipoBoleto("Adulto");
                if (isWithoutSeatRedondo) {
                    ventasDistribution.setPrecioBoleto(
                            applyDiscountToPrice(isDiscountActivated, discountRate, (prices.getPnos() / 2)));
                    ventasDistribution.setPrecioBoletoSinDescuento(prices.getPnos() / 2);
                } else {
                    ventasDistribution
                            .setPrecioBoleto(applyDiscountToPrice(isDiscountActivated, discountRate, prices.getPnos()));
                    ventasDistribution.setPrecioBoletoSinDescuento(prices.getPnos());
                }
                break;
            case "PSOE":
                nuevosTotales[4] = totalPsoe + 1;
                ventasDistribution.setTipoBoleto("INAPAM");
                if (isWithoutSeatRedondo) {
                    ventasDistribution.setPrecioBoleto(
                            applyDiscountToPrice(isDiscountActivated, discountRate, (prices.getPsoe() / 2)));
                    ventasDistribution.setPrecioBoletoSinDescuento(prices.getPsoe() / 2);
                } else {
                    ventasDistribution
                            .setPrecioBoleto(applyDiscountToPrice(isDiscountActivated, discountRate, prices.getPsoe()));
                    ventasDistribution.setPrecioBoletoSinDescuento(prices.getPsoe());
                }
                break;
            default:
                break;
        }
        return nuevosTotales;
    }

    private Double applyDiscountToPrice(Boolean isDiscountActivated, Integer discountRate, Double price) {
        if (isDiscountActivated != null && isDiscountActivated && discountRate != null && discountRate > 0) {
            // Calcular el descuento
            double discount = price * (discountRate.doubleValue() / 100.0);
            price = price - discount;
        }

        // Redondear el precio a dos decimales truncando
        double truncatedPrice = Math.floor(price * 100) / 100.0;
        return truncatedPrice;
    }

    private byte[] convertirBase64ABytes(String base64Image) {
        // Eliminar la primera parte del formato "data:image/jpeg;base64,"
        String base64SinEncabezado = base64Image.substring(base64Image.indexOf(',') + 1);

        // Eliminar los caracteres "\n" del string base64
        String base64Limpio = base64SinEncabezado.replaceAll("\\n", "");

        // Convierte la cadena base64 de la imagen a bytes
        return javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Limpio);
    }

    private String parseFechaHora(String fechaHora) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime localDateTime = LocalDateTime.parse(fechaHora, formatter);
        return localDateTime.toString();
    }

    public List<VentasDistribution> getAllTickets(Long idUsuario) {
        List<VentasDistribution> ventas = ventasRepository.findByIdUsuarioFinal(idUsuario);

        // Filter sales where the 'eliminado' property is not 1
        List<VentasDistribution> ventasFiltradas = ventas.stream()
                .filter(venta -> venta.getEliminado() == null)
                .map(venta -> {
                    venta.setHoraViaje(getHoraDetalle(venta.getFechaHoraViaje()));
                    return venta;
                })
                .collect(Collectors.toList());

        return ventasFiltradas;
    }

    public DetallesTicketDTO getDetails(Long idVenta) {
        VentasDistribution findOne = ventasRepository.findById(idVenta).orElse(null);
        DetallesTicketDTO details = new DetallesTicketDTO();
        double iva = IVA / 100;
        // double iva = 0.16;
        double subtotal = 0;
        double ivaTotal = 0;

        if (findOne != null) {
            details.setAfiliado(null);
            if (findOne.getNumAsiento() != null) {
                details.setAsiento(Integer.parseInt(findOne.getNumAsiento()));
            }
            details.setDestino(findOne.getDestino());
            details.setFecha(getFechaDetalle(findOne.getFechaHoraViaje()));
            details.setFolio(findOne.getFolioBoleto());
            details.setHora(getHoraDetalle(findOne.getFechaHoraViaje()));
            details.setIdTicket(findOne.getId());
            ivaTotal = findOne.getPrecioBoleto() * iva;
            subtotal = findOne.getPrecioBoleto() - ivaTotal;

            subtotal = Math.round(subtotal * 100.0) / 100.0;
            ivaTotal = Math.round(ivaTotal * 100.0) / 100.0;
            details.setIva(ivaTotal);
            String logoBase64 = Base64.encodeBase64String(findOne.getLogo());
            details.setLogo("data:image/jpeg;base64," + logoBase64);
            details.setNombrePasajero(findOne.getNombrePasajero());
            details.setOrigen(findOne.getOrigen());
            details.setSubtotal(subtotal);
            details.setTipoBoleto(findOne.getTipoBoleto());
            details.setTotal(findOne.getPrecioBoleto());
            details.setTour(false);
            // get terms and conditions

            String idReservation = findOne.getIdReservacion();
            String idBooking = "";
            if (idReservation != null) {
                final String detail = "/reservations/detail?idReservation=" + idReservation;
                Response<JsonNode> detailRequest = requestUtils.request(detail, "GET",
                        null);
                if (detailRequest.getCode() == 200) {
                    Iterator<JsonNode> included = detailRequest.getBody().get(1).elements();
                    while (included.hasNext()) {
                        JsonNode includedNext = included.next();
                        if (includedNext.get("type").asText().equals("bookings")) {
                            idBooking = includedNext.get("id").asText();
                        }

                    }
                    if (idBooking != null) {
                        final String booking = "/bookings?idBooking=" + idBooking;
                        Response<JsonNode> bookingRequest = requestUtils.request(booking, "GET",
                                null);
                        if (bookingRequest.getCode() == 200) {
                            Iterator<JsonNode> includedBooking = bookingRequest.getBody().get(1).elements();
                            while (includedBooking.hasNext()) {
                                JsonNode includedBookingNext = includedBooking.next();
                                if (includedBookingNext.get("type").asText().equals("marketing_carriers")) {
                                    details.setTerminosCondiciones(
                                            includedBookingNext.get("attributes").get("terms").asText());
                                }
                            }
                        }
                    }
                }
            }
            return details;
        }
        return null;
    }

    private List<String> getFolios(Map<String, String> foliosMap, String idFolio) {
        // Array para almacenar el primer ID que coincide
        List<String> coincidentIds = new ArrayList<>();

        // Iterar sobre las entradas del mapa
        for (Map.Entry<String, String> entry : foliosMap.entrySet()) {
            String clave = entry.getKey();
            String valor = entry.getValue();
            // Verificar si la clave contiene la parte del ID generado
            if (clave.contains(idFolio)) {
                // Si hay coincidencia, almacenar la clave y el valor y salir del bucle
                coincidentIds.add(clave);
                coincidentIds.add(valor);
                break;
            }
        }
        return coincidentIds;
    }

    public String getFechaDetalle(String date) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parsedDate = inputFormat.parse(date);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
            return outputFormat.format(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getHoraDetalle(String date) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parsedDate = inputFormat.parse(date);
            SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a");
            return outputFormat.format(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Optional<VentasDistribution> findById(Long idVenta) {
        if (idVenta == null || idVenta <= 0) {
            throw new IllegalArgumentException("ID de venta no válido: " + idVenta);
        }

        return ventasRepository.findById(idVenta);
    }

    public VentasDistribution save(VentasDistribution venta) {
        if (venta == null) {
            throw new IllegalArgumentException("La venta proporcionada es nula.");
        }

        return ventasRepository.save(venta);
    }

    public static double convertirCentavosAPesos(int precioEnCentavos) {
        // Convertir centavos a pesos mexicanos
        double precioEnPesos = precioEnCentavos / 100.0;
        return precioEnPesos;
    }

    public static int convertirPesosACentavos(double precioEnPesos) {
        BigDecimal precioEnPesosBigDecimal = BigDecimal.valueOf(precioEnPesos);
        BigDecimal precioEnCentavosBigDecimal = precioEnPesosBigDecimal.multiply(BigDecimal.valueOf(100));
        int precioEnCentavos = precioEnCentavosBigDecimal.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
        return precioEnCentavos;
    }

    public String resendTicket(Long idVenta) {
        // Obtener la venta por ID
        VentasDistribution ventaDistribution = ventasRepository.findById(idVenta)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la venta con ID: " + idVenta));

        // Construir la URL para obtener booking_id y total_price de DT
        final String detailEndpoint = "/reservations/detail?idReservation=" + ventaDistribution.getIdReservacion();
        Response<JsonNode> detailRequest = requestUtils.request(detailEndpoint, "GET", null);

        if (detailRequest.getCode() == 200) {
            JsonNode responseBody = detailRequest.getBody();

            String idBooking = null;
            Iterator<JsonNode> includedElements = responseBody.get(1).elements();
            while (includedElements.hasNext()) {
                JsonNode includedElement = includedElements.next();
                if ("bookings".equals(includedElement.get("type").asText())) {
                    idBooking = includedElement.get("id").asText();
                    break;
                }
            }

            if (idBooking != null) {
                // Construir la URL para solicitar ticket a Distribution
                final String bookingEndpoint = "/resendTicket?idBooking=" + idBooking;
                Response<JsonNode> bookingRequest = requestUtils.request(bookingEndpoint, "GET", null);

                if (bookingRequest.getCode() == 200) {
                    // Obtener el PDF en base64
                    String pdfBytes = bookingRequest.getBody().asText();
                    // Enviar el correo con el ticket adjunto (Estilo CB)
                    return pdfBytes;
                }
            }
        }
        return null;
    }

    public String sendTicketEdited(String bookingId, Integer totalPriceInCents, String correo,
            List<VentasDistribution> ventas, String numTarjeta, String numAut, String idTrans, Boolean isRedondo) {

        Double totalInPesos = convertirCentavosAPesos(totalPriceInCents);

        // Construir la URL para solicitar ticket a Distribution(ya nos devuelve el pdf
        // editado)
        final String bookingEndpoint = "/resendTicket?idBooking=" + bookingId;
        Response<JsonNode> bookingRequest = requestUtils.request(bookingEndpoint, "GET", null);

        if (bookingRequest.getCode() == 200) {
            // Obtener el PDF en base64
            String pdfBytes = bookingRequest.getBody().asText();
            // Enviar el correo con el ticket adjunto (Estilo CB)
            emailService.resendEmailTicketDistribution(correo, "COMPRA DE BOLETOS", totalInPesos.toString(),
                    pdfBytes, ventas, numTarjeta, numAut, idTrans, isRedondo);
            return "OK";
        }

        return null;
    }

}
