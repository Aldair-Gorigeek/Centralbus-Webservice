package com.gorigeek.springboot.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Collections;
import java.util.Arrays;

import javax.mail.MessagingException;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gorigeek.springboot.Module;
import com.gorigeek.springboot.distribution.entity.PassengerTripInfo;
import com.gorigeek.springboot.entity.Afiliado;
import com.gorigeek.springboot.entity.Autobus;
import com.gorigeek.springboot.entity.DetalleVentaMovil;
import com.gorigeek.springboot.entity.DetalleVentaTourMovil;
import com.gorigeek.springboot.entity.Terminales;
import com.gorigeek.springboot.entity.TipoBoleto;
import com.gorigeek.springboot.entity.TourMovil;
import com.gorigeek.springboot.entity.Transacciones;
import com.gorigeek.springboot.entity.VentaTourMovil;
import com.gorigeek.springboot.entity.VentaViajesMovil;
import com.gorigeek.springboot.entity.DTO.DetallesTicketDTO;
import com.gorigeek.springboot.entity.DTO.TicketDTO;
import com.gorigeek.springboot.exception.ResourceNotFoundException;
import com.gorigeek.springboot.repository.AfiliadoRepository;
import com.gorigeek.springboot.repository.AutobusRepository;
import com.gorigeek.springboot.repository.DetalleVentaRepository;
import com.gorigeek.springboot.repository.DetalleVentaTourRepository;
import com.gorigeek.springboot.repository.TerminalRepository;
import com.gorigeek.springboot.repository.TipoBoletoRepository;
import com.gorigeek.springboot.repository.TourMovilRepository;
import com.gorigeek.springboot.repository.TransaccionRepository;
import com.gorigeek.springboot.repository.VentaTourRepository;
import com.gorigeek.springboot.repository.VentaViajesRepository;
import com.gorigeek.springboot.util.BarcodeToPDF;
import com.gorigeek.springboot.util.EmailService;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

@RestController
@RequestMapping("/api/misBoletos")
public class MisBoletosController {

    @Value("${iva}")
    private double IVA;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VentaViajesRepository repo;

    @Autowired
    private DetalleVentaRepository repoDetalle;

    @Autowired
    private VentaTourRepository repoTour;

    @Autowired
    private TourMovilRepository repoTourMovil;

    @Autowired
    private DetalleVentaTourRepository repoDetalleTour;

    @Autowired
    private AfiliadoRepository repoAfiliado;

    @Autowired
    private TerminalRepository repoTerminal;

    @Autowired
    private TipoBoletoRepository repoTipoBoleto;

    @Autowired
    private TransaccionRepository repoTransaccion;

    @Autowired
    private AutobusRepository repoAutobus;

    // obtener todos los boletos
    @GetMapping
    public List<DetalleVentaMovil> getAllTickets() {
        return repoDetalle.findAll();
    }

    // obtener lista de boletos de viaje sencillo y tour
    @GetMapping("/v1")
    public List<TicketDTO> getAllTicketsT() {
        // List<VentaViajesMovil> sencillo = repo.findAll();
        // List<VentaTourMovil> tour = repoTour.findAll();

        List<DetalleVentaMovil> sencillo = repoDetalle.findAll();
        List<DetalleVentaTourMovil> tour = repoDetalleTour.findAll();

        List<TicketDTO> tickets = new ArrayList<>();

        for (int i = 0; i < sencillo.size(); i++) {
            TicketDTO ticket = new TicketDTO();

            ticket.setId(sencillo.get(i).getId_detalle_venta());
            ticket.setTour(false);
            ticket.setNombrePasajero(sencillo.get(i).getNombrePasajero());
            ticket.setOrigen(sencillo.get(i).getVentaViajes().getTerminalOrigen().getCiudad().getDescripcion()
                    + ", " + sencillo.get(i).getVentaViajes().getTerminalOrigen().getEstados().getDescripcion());
            ticket.setDestino(sencillo.get(i).getVentaViajes().getTerminalDestino().getCiudad().getDescripcion()
                    + ", " + sencillo.get(i).getVentaViajes().getTerminalDestino().getEstados().getDescripcion());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime fechaHora = LocalDateTime.parse(sencillo.get(i).getFechaViaje(), formatter);
            ticket.setFecha(
                    fechaHora.format(DateTimeFormatter.ofPattern("dd MMM yyyy")).toUpperCase().replace(".", ""));
            ticket.setHora(fechaHora.format(DateTimeFormatter.ofPattern("hh:mm a")).replace(".", ""));

            tickets.add(ticket);
        }

        for (int i = 0; i < tour.size(); i++) {
            TicketDTO ticket = new TicketDTO();

            ticket.setId(tour.get(i).getId_detalle_venta_tour());
            ticket.setTour(true);
            ticket.setNombrePasajero(tour.get(i).getNombrePasajero());
            ticket.setOrigen(tour.get(i).getVentaTour().getTour().getCiudadesOrigen().getDescripcion()
                    + ", " + tour.get(i).getVentaTour().getTour().getEstadoOrigen().getDescripcion());
            ticket.setDestino(tour.get(i).getVentaTour().getTour().getCiudadesDestino().getDescripcion()
                    + ", " + tour.get(i).getVentaTour().getTour().getEstadoDestino().getDescripcion());
            System.out.println("La fecha es: " + tour.get(i).getFechaViaje());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime fechaHora = LocalDateTime.parse(tour.get(i).getFechaViaje(), formatter);
            System.out.println(fechaHora);
            ticket.setFecha(
                    fechaHora.format(DateTimeFormatter.ofPattern("dd MMM yyyy")).toUpperCase().replace(".", ""));
            ticket.setHora(fechaHora.format(DateTimeFormatter.ofPattern("hh:mm a")).replace(".", "").trim());

            tickets.add(ticket);
        }

        return tickets;
    }

    // obtener lista de boletos de viaje sencillo y tour por usuario 2 donde el
    // valor de status sea distinto a 2
    @Module("CentralBus - Mis Boletos/Todos Los Boletos")
    @GetMapping("/v1/{idUsuario}")
    public List<TicketDTO> getAllTicketsTUsuario2(@PathVariable(value = "idUsuario") Long idUsuario) {
        List<DetalleVentaMovil> sencillo = repoDetalle
                .findByUsuarioFinalAndStatusDisponibleNotOrderByIdDetalleVentaAsc(idUsuario, 2);
        List<DetalleVentaTourMovil> tour = repoDetalleTour
                .findByUsuarioFinalAndStatusDisponibleNotOrderByIdDetalleVentaTourDesc(idUsuario, 2);
        List<TicketDTO> tickets = new ArrayList<>();
        Map<String, PassengerTripInfo> processedPassengers = new HashMap<>();

        for (DetalleVentaMovil detalle : sencillo) {
            String passengerName = detalle.getNombrePasajero().trim();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime fechaViaje = LocalDateTime.parse(detalle.getFechaViaje(), formatter);
            LocalDateTime fechaCompra = LocalDateTime.parse(detalle.getVentaViajes().getFechaCompra(), formatter);

            // Verifica si el pasajero ya tiene un registro con una fecha anterior o igual
            if (processedPassengers.containsKey(passengerName)) {
                PassengerTripInfo tripInfo = processedPassengers.get(passengerName);
                if (!fechaViaje.isEqual(tripInfo.getFechaViaje())
                        && (tripInfo.getId() + 1) == detalle.getVentaViajes().getIdVentaViaje()
                        && tripInfo.getFechaCompra().isEqual(fechaCompra)) {
                    continue; // Si ya no son iguales quiere decir que son los lugares de vuelta
                } else {
                    processedPassengers.put(passengerName,
                            new PassengerTripInfo(detalle.getVentaViajes().getIdVentaViaje(),
                                    fechaCompra, fechaViaje));
                }
            } else {
                // Agrega el nombre del pasagero si aun no existe
                processedPassengers.put(passengerName,
                        new PassengerTripInfo(detalle.getVentaViajes().getIdVentaViaje(),
                                fechaCompra, fechaViaje));
            }

            TicketDTO ticket = new TicketDTO();
            ticket.setId(detalle.getId_detalle_venta());
            ticket.setTour(false);
            ticket.setNombrePasajero(detalle.getNombrePasajero());
            ticket.setOrigen(detalle.getVentaViajes().getTerminalOrigen().getCiudad().getDescripcion()
                    + ", " + detalle.getVentaViajes().getTerminalOrigen().getEstados().getDescripcion());
            ticket.setDestino(detalle.getVentaViajes().getTerminalDestino().getCiudad().getDescripcion()
                    + ", " + detalle.getVentaViajes().getTerminalDestino().getEstados().getDescripcion());

            ticket.setFecha(
                    fechaViaje.format(DateTimeFormatter.ofPattern("dd MMM yyyy")).toUpperCase().replace(".", ""));
            ticket.setHora(fechaViaje.format(DateTimeFormatter.ofPattern("hh:mm a")).replace(".", ""));

            ticket.setStatus(detalle.getStatusDisponible());

            tickets.add(ticket);
        }

        for (int i = 0; i < tour.size(); i++) {
            TicketDTO ticket = new TicketDTO();

            ticket.setId(tour.get(i).getId_detalle_venta_tour());
            ticket.setTour(true);
            ticket.setNombrePasajero(tour.get(i).getNombrePasajero());
            ticket.setOrigen(tour.get(i).getVentaTour().getTour().getCiudadesOrigen().getDescripcion()
                    + ", " + tour.get(i).getVentaTour().getTour().getEstadoOrigen().getDescripcion());
            ticket.setDestino(tour.get(i).getVentaTour().getTour().getCiudadesDestino().getDescripcion()
                    + ", " + tour.get(i).getVentaTour().getTour().getEstadoDestino().getDescripcion());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime fechaHora = LocalDateTime.parse(tour.get(i).getFechaViaje(), formatter);
            ticket.setFecha(
                    fechaHora.format(DateTimeFormatter.ofPattern("dd MMM yyyy")).toUpperCase().replace(".", ""));
            ticket.setHora(fechaHora.format(DateTimeFormatter.ofPattern("hh:mm a")).replace(".", "").trim());

            ticket.setStatus(tour.get(i).getStatusDisponible());

            tickets.add(ticket);
        }
        return tickets;
    }

    // obtener detalles de boletos de viaje
    @Module("CentralBus - Mis Boletos")
    @GetMapping("/detalles/{idVenta}")
    public List<DetallesTicketDTO> getDetailsTicket(@PathVariable(value = "idVenta") Long idVenta) {
        List<DetallesTicketDTO> detallesTicket = new ArrayList<>();

        double iva = IVA / 100;
        double subtotal = 0;
        double ivaTotal = 0;

        DetalleVentaMovil detalleTicket = repoDetalle.findById(idVenta)
                .orElseThrow(() -> new ResourceNotFoundException("ticket not found with id: " + idVenta));

        List<DetalleVentaMovil> tickets = repoDetalle
                .findByFechaCompraVentaViajes(detalleTicket.getVentaViajes().getFechaCompra(),
                        detalleTicket.getUsuarioFinal());

        // Mapa para almacenar los tickets por fecha de viaje, ordenado por fecha
        Map<String, List<DetalleVentaMovil>> ticketsPorFechaViaje = tickets.stream()
                .collect(Collectors.groupingBy(DetalleVentaMovil::getFechaViaje, TreeMap::new, Collectors.toList()));

        // Mapa para almacenar los tickets con claves numéricas
        Map<Integer, List<DetalleVentaMovil>> ticketsPorNumero = new LinkedHashMap<>();

        // Asignar claves numéricas secuenciales
        int key = 1;
        for (List<DetalleVentaMovil> listaTickets : ticketsPorFechaViaje.values()) {
            ticketsPorNumero.put(key++, listaTickets);
        }

        Boolean isRedondo = ticketsPorFechaViaje.size() > 1 ? true : false;

        if (!isRedondo) {
            for (DetalleVentaMovil ticket : tickets) {
                if (ticket.getId_detalle_venta() != detalleTicket.getId_detalle_venta()) {
                    continue;
                }

                DetallesTicketDTO ticketDTO = new DetallesTicketDTO();
                ticketDTO.setIdTicket(ticket.getId_detalle_venta());
                ticketDTO.setTour(false);
                ticketDTO.setTipoBoleto(ticket.getTipoBoleto().getDescripcion().toUpperCase());
                ticketDTO.setNombrePasajero(ticket.getNombrePasajero());

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime fechaHora = LocalDateTime.parse(ticket.getFechaViaje(), formatter);

                ticketDTO.setFecha(
                        fechaHora.format(DateTimeFormatter.ofPattern("dd MMM yyyy")).toUpperCase().replace(".", ""));
                ticketDTO.setHora(fechaHora.format(DateTimeFormatter.ofPattern("hh:mm a")).replace(".", "").trim());
                ticketDTO.setOrigen(ticket.getVentaViajes().getTerminalOrigen().getCiudad().getDescripcion() + ", "
                        + ticket.getVentaViajes().getTerminalOrigen().getEstados().getDescripcion());
                ticketDTO.setDestino(ticket.getVentaViajes().getTerminalDestino().getCiudad().getDescripcion() + ", "
                        + ticket.getVentaViajes().getTerminalDestino().getEstados().getDescripcion());
                ticketDTO.setAsiento(ticket.getNumeroAsiento());

                // subtotal = ticket.getVentaViajes().getTotalPagado()/(1+iva);
                // ivaTotal = subtotal * iva;

                ivaTotal = ticket.getCosto() * iva;
                subtotal = ticket.getCosto() - ivaTotal;

                subtotal = Math.round(subtotal * 100.0) / 100.0;
                ivaTotal = Math.round(ivaTotal * 100.0) / 100.0;
                // DecimalFormat df = new DecimalFormat("#.00");

                ticketDTO.setSubtotal(subtotal);
                ticketDTO.setIva(ivaTotal);
                ticketDTO.setTotal(ticket.getCosto());

                ticketDTO.setFolio(ticket.getFolio());

                ticketDTO.setLogo(ticket.getVentaViajes().getAfiliado().getLogotipo());
                ticketDTO.setAfiliado(ticket.getVentaViajes().getAfiliado().getNombreLinea());

                ticketDTO.setMostrarDireccion(ticket.getVentaViajes().getAfiliado().getMostrarDireccion());
                ticketDTO.setDireccionTerminalOrigen(ticket.getVentaViajes().getTerminalOrigen().getDireccion());
                ticketDTO.setDireccionTerminalDestino(ticket.getVentaViajes().getTerminalDestino().getDireccion());

                // comprobar si es double decker
                Optional<Autobus> optionalAutobus = repoAutobus.findById(Math.round(ticket.getAutobus()));

                if (optionalAutobus.isPresent()) {
                    Autobus autobus = optionalAutobus.get();
                    if (Arrays.asList(7L, 8L, 9L).contains(autobus.getTipoTransporte().getIdTipoTransporte())) {
                        ticketDTO.setIsDoubleDecker(true);

                    } else {
                        ticketDTO.setIsDoubleDecker(false);
                    }
                } else {
                    ticketDTO.setIsDoubleDecker(false);
                }

                ticketDTO.setNumeroPlanta(ticket.getTipoPlanta());

                detallesTicket.add(ticketDTO);

            }
        } else

        {
            List<DetalleVentaMovil> viajesDeIda = ticketsPorNumero.get(1);
            List<DetalleVentaMovil> viajesDeVuelta = ticketsPorNumero.get(2);
            for (int i = 0; i < viajesDeIda.size(); i++) {
                if (detalleTicket.getNumeroAsiento() != viajesDeIda.get(i).getNumeroAsiento()) {
                    continue;
                }
                DetallesTicketDTO ticketDTOIda = new DetallesTicketDTO();
                DetallesTicketDTO ticketDTOVuelta = new DetallesTicketDTO();
                ticketDTOIda.setIdTicket(viajesDeIda.get(i).getId_detalle_venta());
                ticketDTOIda.setTour(false);
                ticketDTOIda.setTipoBoleto(viajesDeIda.get(i).getTipoBoleto().getDescripcion().toUpperCase());
                ticketDTOIda.setNombrePasajero(viajesDeIda.get(i).getNombrePasajero());

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime fechaHora = LocalDateTime.parse(viajesDeIda.get(i).getFechaViaje(), formatter);

                ticketDTOIda.setFecha(
                        fechaHora.format(DateTimeFormatter.ofPattern("dd MMM yyyy")).toUpperCase().replace(".", ""));
                ticketDTOIda.setHora(fechaHora.format(DateTimeFormatter.ofPattern("hh:mm a")).replace(".", "").trim());
                ticketDTOIda.setOrigen(viajesDeIda.get(i).getVentaViajes().getTerminalOrigen().getCiudad()
                        .getDescripcion() + ", "
                        + viajesDeIda.get(i).getVentaViajes().getTerminalOrigen().getEstados().getDescripcion());
                ticketDTOIda.setDestino(viajesDeIda.get(i).getVentaViajes().getTerminalDestino().getCiudad()
                        .getDescripcion() + ", "
                        + viajesDeIda.get(i).getVentaViajes().getTerminalDestino().getEstados().getDescripcion());
                ticketDTOIda.setAsiento(viajesDeIda.get(i).getNumeroAsiento());

                ivaTotal = viajesDeIda.get(i).getCosto() * iva;
                subtotal = viajesDeIda.get(i).getCosto() - ivaTotal;

                subtotal = Math.round(subtotal * 100.0) / 100.0;
                ivaTotal = Math.round(ivaTotal * 100.0) / 100.0;

                ticketDTOIda.setSubtotal(subtotal);
                ticketDTOIda.setIva(ivaTotal);
                ticketDTOIda.setTotal(viajesDeIda.get(i).getCosto());

                ticketDTOIda.setFolio(viajesDeIda.get(i).getFolio());

                ticketDTOIda.setLogo(viajesDeIda.get(i).getVentaViajes().getAfiliado().getLogotipo());
                ticketDTOIda.setAfiliado(viajesDeIda.get(i).getVentaViajes().getAfiliado().getNombreLinea());

                ticketDTOIda
                        .setMostrarDireccion(viajesDeIda.get(i).getVentaViajes().getAfiliado().getMostrarDireccion());
                ticketDTOIda.setDireccionTerminalOrigen(
                        viajesDeIda.get(i).getVentaViajes().getTerminalOrigen().getDireccion());
                ticketDTOIda.setDireccionTerminalDestino(
                        viajesDeIda.get(i).getVentaViajes().getTerminalDestino().getDireccion());

                // comprobar si es double decker/ ida
                Optional<Autobus> autobusIda = repoAutobus.findById(Math.round(viajesDeIda.get(i).getAutobus()));

                if (autobusIda.isPresent()) {
                    Autobus autobus = autobusIda.get();
                    System.err.println(autobus.getTipoTransporte().getIdTipoTransporte());
                    if (Arrays.asList(7L, 8L, 9L).contains(autobus.getTipoTransporte().getIdTipoTransporte())) {
                        ticketDTOIda.setIsDoubleDecker(true);
                    } else {
                        ticketDTOIda.setIsDoubleDecker(false);
                    }
                } else {
                    ticketDTOIda.setIsDoubleDecker(false);
                }
                ticketDTOIda.setNumeroPlanta(viajesDeIda.get(i).getTipoPlanta());
                detallesTicket.add(ticketDTOIda);

                ticketDTOVuelta.setIdTicket(viajesDeVuelta.get(i).getId_detalle_venta());
                ticketDTOVuelta.setTour(false);
                ticketDTOVuelta.setTipoBoleto(viajesDeVuelta.get(i).getTipoBoleto().getDescripcion().toUpperCase());
                ticketDTOVuelta.setNombrePasajero(viajesDeVuelta.get(i).getNombrePasajero());

                LocalDateTime fechaHoraVuelta = LocalDateTime.parse(viajesDeVuelta.get(i).getFechaViaje(), formatter);

                ticketDTOVuelta.setFecha(
                        fechaHoraVuelta.format(DateTimeFormatter.ofPattern("dd MMM yyyy")).toUpperCase().replace(".",
                                ""));
                ticketDTOVuelta.setHora(
                        fechaHoraVuelta.format(DateTimeFormatter.ofPattern("hh:mm a")).replace(".", "").trim());
                ticketDTOVuelta.setOrigen(viajesDeVuelta.get(i).getVentaViajes().getTerminalOrigen().getCiudad()
                        .getDescripcion() + ", "
                        + viajesDeVuelta.get(i).getVentaViajes().getTerminalOrigen().getEstados().getDescripcion());
                ticketDTOVuelta.setDestino(viajesDeVuelta.get(i).getVentaViajes().getTerminalDestino().getCiudad()
                        .getDescripcion() + ", "
                        + viajesDeVuelta.get(i).getVentaViajes().getTerminalDestino().getEstados().getDescripcion());
                ticketDTOVuelta.setAsiento(viajesDeVuelta.get(i).getNumeroAsiento());

                ivaTotal = viajesDeVuelta.get(i).getCosto() * iva;
                subtotal = viajesDeVuelta.get(i).getCosto() - ivaTotal;

                subtotal = Math.round(subtotal * 100.0) / 100.0;
                ivaTotal = Math.round(ivaTotal * 100.0) / 100.0;

                ticketDTOVuelta.setSubtotal(subtotal);
                ticketDTOVuelta.setIva(ivaTotal);
                ticketDTOVuelta.setTotal(viajesDeVuelta.get(i).getCosto());

                ticketDTOVuelta.setFolio(viajesDeVuelta.get(i).getFolio());

                ticketDTOVuelta.setLogo(viajesDeVuelta.get(i).getVentaViajes().getAfiliado().getLogotipo());
                ticketDTOVuelta.setAfiliado(viajesDeVuelta.get(i).getVentaViajes().getAfiliado().getNombreLinea());

                ticketDTOVuelta.setMostrarDireccion(
                        viajesDeVuelta.get(i).getVentaViajes().getAfiliado().getMostrarDireccion());
                ticketDTOVuelta.setDireccionTerminalOrigen(
                        viajesDeVuelta.get(i).getVentaViajes().getTerminalOrigen().getDireccion());
                ticketDTOVuelta.setDireccionTerminalDestino(
                        viajesDeVuelta.get(i).getVentaViajes().getTerminalDestino().getDireccion());

                // comprobar si es double decker/ vuelta
                Optional<Autobus> autobusVuelta = repoAutobus.findById(Math.round(viajesDeVuelta.get(i).getAutobus()));

                if (autobusVuelta.isPresent()) {
                    Autobus autobus = autobusVuelta.get();
                    if (Arrays.asList(7L, 8L, 9L).contains(autobus.getTipoTransporte().getIdTipoTransporte())) {
                        ticketDTOVuelta.setIsDoubleDecker(true);
                    } else {
                        ticketDTOVuelta.setIsDoubleDecker(false);
                    }
                } else {
                    ticketDTOVuelta.setIsDoubleDecker(false);
                }
                ticketDTOVuelta.setNumeroPlanta(viajesDeVuelta.get(i).getTipoPlanta());
                detallesTicket.add(ticketDTOVuelta);

            }
        }

        return detallesTicket;
    }

    // obtener detalles de boletos de viaje
    @GetMapping("/detallesTour/{idVentaTour}")
    public DetallesTicketDTO getDetailsTicketTour(@PathVariable(value = "idVentaTour") Long idVenta) {
        System.out.println("El valor del iva es: " + IVA);

        double iva = IVA / 100;
        // double iva = 0.16;
        double subtotal = 0;
        double ivaTotal = 0;

        // VentaTourMovil ticket = repoTour.findById(idVenta)
        // .orElseThrow(()->new ResourceNotFoundException("ticket not found with id: " +
        // idVenta));

        DetalleVentaTourMovil ticket = repoDetalleTour.findById(idVenta)
                .orElseThrow(() -> new ResourceNotFoundException("ticket not found with id: " + idVenta));

        DetallesTicketDTO ticketDTO = new DetallesTicketDTO();
        ticketDTO.setIdTicket(ticket.getId_detalle_venta_tour());
        ticketDTO.setTour(true);
        ticketDTO.setTipoBoleto(ticket.getTipoBoleto().getDescripcion().toUpperCase());
        ticketDTO.setNombrePasajero(ticket.getNombrePasajero());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime fechaHora = LocalDateTime.parse(ticket.getFechaViaje(), formatter);

        ticketDTO.setFecha(fechaHora.format(DateTimeFormatter.ofPattern("dd MMM yyyy")).toUpperCase().replace(".", ""));
        ticketDTO.setHora(fechaHora.format(DateTimeFormatter.ofPattern("hh:mm a")).replace(".", "").trim());
        ticketDTO.setOrigen(ticket.getVentaTour().getTour().getCiudadesOrigen().getDescripcion() + ", "
                + ticket.getVentaTour().getTour().getEstadoOrigen().getDescripcion());
        ticketDTO.setDestino(ticket.getVentaTour().getTour().getCiudadesDestino().getDescripcion() + ", "
                + ticket.getVentaTour().getTour().getEstadoDestino().getDescripcion());
        ticketDTO.setAsiento(ticket.getNumeroAsiento());

        // subtotal = ticket.getVentaTour().getTotalPagado()/(1+iva);
        // ivaTotal = subtotal * iva;

        ivaTotal = ticket.getCosto() * iva;
        subtotal = ticket.getCosto() - ivaTotal;

        subtotal = Math.round(subtotal * 100.0) / 100.0;
        ivaTotal = Math.round(ivaTotal * 100.0) / 100.0;

        ticketDTO.setSubtotal(subtotal);
        ticketDTO.setIva(ivaTotal);
        ticketDTO.setTotal(ticket.getCosto());
        ticketDTO.setFolio(ticket.getFolio());

        ticketDTO.setLogo(ticket.getVentaTour().getAfiliado().getLogotipo());
        ticketDTO.setAfiliado(ticket.getVentaTour().getAfiliado().getNombreLinea());
        return ticketDTO;
    }

    /** Actualizar el estado del boleto */
    @Module("CentralBus - Mis Boletos/Eliminar")
    @PostMapping("status/{idDetalle}")
    public String updateStatusTicket(@PathVariable(value = "idDetalle") Long idDetalle,
            @RequestBody DetalleVentaMovil detalles) {
        DetalleVentaMovil detalleDb = this.repoDetalle.findById(idDetalle)
                .orElseThrow(() -> new ResourceNotFoundException("ticket not found with id: " + idDetalle));

        List<DetalleVentaMovil> tickets = repoDetalle
                .findByFechaCompraVentaViajes(detalleDb.getVentaViajes().getFechaCompra(),
                        detalleDb.getUsuarioFinal());
        for (DetalleVentaMovil ticket : tickets) {
            detalleDb.setStatusDisponible(detalles.getStatusDisponible());
            repoDetalle.save(ticket);
        }

        return "ok";
    }

    /** Actualizar el estado del boleto tour */
    @Transactional
    @PostMapping("statusTour/{idDetalle}")
    public String updateStatusTicketTour(@PathVariable(value = "idDetalle") Long idDetalle,
            @RequestBody DetalleVentaTourMovil detalles) {
        DetalleVentaTourMovil detalleDb = this.repoDetalleTour.findById(idDetalle)
                .orElseThrow(() -> new ResourceNotFoundException("ticket not found with id: " + idDetalle));

        detalleDb.setStatusDisponible(detalles.getStatusDisponible());
        repoDetalleTour.save(detalleDb);
        return "ok";
    }

    // obtener detalles de boletos de viaje
    @Module("CentralBus - Obtener Iva")
    @GetMapping("/iva")
    public JSONObject getIVA() {
        System.out.println("El valor del iva es: " + IVA);

        double iva = IVA;

        JSONObject resp = new JSONObject();
        resp.put("iva", iva);
        return resp;
    }

    /**
     * Insertar boleto
     * 
     * 
     * {
     * "afiliado": 2,
     * "origen": 26,
     * "destino": 27,
     * "cajero": 2,
     * "ruta": 7,
     * "total": 500,
     * "tipoBoleto":2,
     * "autobus": 25,
     * "costo":500,
     * "fechaViaje":"2023-09-09 14:00:00",
     * "folio":"1234567890123456",
     * "nombre":"Prueba postman",
     * "asiento": 2,
     * "usuario":11
     * }
     * 
     * @throws MessagingException
     */
    @Module("CentralBus - Comprar Boleto/Generar Boleto")
    @PostMapping("guardarTicket")
    @Transactional
    public String insertTicket(@RequestBody JSONObject ticket) {
        double iva = IVA / 100;

        // generar id de consolidado
        VentaViajesMovil viaje = new VentaViajesMovil();
        Afiliado afiliado = repoAfiliado.findById(Long.parseLong(ticket.get("afiliado").toString()))
                .orElseThrow(
                        () -> new ResourceNotFoundException("afiliado not found with id: " + ticket.get("afiliado")));
        Terminales origen = repoTerminal.findById(Long.parseLong(ticket.get("origen").toString()))
                .orElseThrow(() -> new ResourceNotFoundException("origen not found with id: " + ticket.get("origen")));
        Terminales destino = repoTerminal.findById(Long.parseLong(ticket.get("destino").toString()))
                .orElseThrow(
                        () -> new ResourceNotFoundException("destino not found with id: " + ticket.get("destino")));

        List<String> folios = (List<String>) ticket.get("folios");
        System.out.println("numero de folios " + folios.size());

        viaje.setAfiliado(afiliado);

        LocalDateTime fechaHora = LocalDateTime.now();
        viaje.setFechaCompra(fechaHora.toString());
        viaje.setRuta(Long.parseLong(ticket.get("ruta").toString()));
        viaje.setTerminalOrigen(origen);
        viaje.setTerminalDestino(destino);
        viaje.setTotalPagado(Double.parseDouble(ticket.get("total").toString()));
        if (ticket.get("promocion") != null) {
            viaje.setPromocion(Long.parseLong(ticket.get("promocion").toString()));
        }

        System.out.println("funciona aqui");
        VentaViajesMovil viajeGuardado = this.repo.save(viaje);
        VentaViajesMovil viajeRegresoGuardado = new VentaViajesMovil();

        // Almacenar información para correo
        List<DetalleVentaMovil> listaDetalles = new ArrayList<>();
        //
        if (viajeGuardado != null) {
            System.out.println("funciona aqui3");
            int tipoVIaje = (int) ticket.get("tipoViaje");
            if (tipoVIaje == 0) {
                System.out.println("Sencillo");
            } else {
                System.out.println("Redondo");
            }
            // System.out.println("funciona");
            List<String> detalles = (List<String>) ticket.get("detalles");
            int posicionFolio = 0;
            for (int i = 0; i < detalles.size(); i++) {

                System.out.println("elementos " + i);
                System.out.println(detalles.get(i));
                String[] valores = detalles.get(i).split("%");
                System.out.println("ida " + valores[0]);

                TipoBoleto tipoBoleto = repoTipoBoleto.findById(Long.parseLong(valores[3]))
                        .orElseThrow(() -> new ResourceNotFoundException("boleto not found with id: " + valores[3]));

                DetalleVentaMovil detalleVenta = new DetalleVentaMovil();
                detalleVenta.setAutobus(Double.parseDouble(ticket.get("autobus").toString()));
                detalleVenta.setUsuarioFinal(Long.parseLong(ticket.get("usuario").toString()));
                detalleVenta.setFechaViaje(ticket.get("fechaViaje").toString());
                detalleVenta.setStatusDisponible(1);
                detalleVenta.setVentaViajes(viaje);

                detalleVenta.setCosto(Double.parseDouble(valores[4]));
                detalleVenta.setNombrePasajero(valores[2]);
                detalleVenta.setNumeroAsiento(Integer.parseInt(valores[0]));
                detalleVenta.setTipoBoleto(tipoBoleto);
                detalleVenta.setStatusNotificacion(2);

                detalleVenta.setFolio(folios.get(i));
                posicionFolio = +1;

                DetalleVentaMovil detalleDB = this.repoDetalle.save(detalleVenta);
                listaDetalles.add(detalleDB);
            }
            if (tipoVIaje == 1) {
                // agregar viaje redondo regreso
                VentaViajesMovil viajeRegreso = new VentaViajesMovil();
                viajeRegreso.setAfiliado(afiliado);
                viajeRegreso.setFechaCompra(fechaHora.toString());
                viajeRegreso.setRuta(Long.parseLong(ticket.get("rutaVuelta").toString()));
                viajeRegreso.setTerminalOrigen(destino);
                viajeRegreso.setTerminalDestino(origen);
                viajeRegreso.setTotalPagado(Double.parseDouble(ticket.get("total").toString()));

                if (ticket.get("promocionVuelta") != null) {
                    viajeRegreso.setPromocion(Long.parseLong(ticket.get("promocionVuelta").toString()));
                }
                viajeRegresoGuardado = repo.save(viajeRegreso);
                if (viajeRegresoGuardado != null) {
                    for (int i = 0; i < detalles.size(); i++) {

                        System.out.println("elementosVuelta " + i);
                        System.out.println(detalles.get(i));
                        String[] valores = detalles.get(i).split("%");
                        System.out.println("Vuelta " + valores[1]);

                        TipoBoleto tipoBoleto = repoTipoBoleto.findById(Long.parseLong(valores[3]))
                                .orElseThrow(
                                        () -> new ResourceNotFoundException("boleto not found with id: " + valores[3]));

                        DetalleVentaMovil detalleVenta = new DetalleVentaMovil();
                        detalleVenta.setAutobus(Double.parseDouble(ticket.get("autobus").toString()));
                        detalleVenta.setUsuarioFinal(Long.parseLong(ticket.get("usuario").toString()));
                        detalleVenta.setFechaViaje(ticket.get("fechaViajeVuelta").toString());
                        detalleVenta.setStatusDisponible(1);
                        detalleVenta.setVentaViajes(viajeRegreso);
                        // se modificó el total, antes tenia 4, en valores, deberia de funcionar
                        detalleVenta.setCosto(Double.parseDouble(valores[5]));
                        detalleVenta.setNombrePasajero(valores[2]);
                        detalleVenta.setNumeroAsiento(Integer.parseInt(valores[1]));
                        detalleVenta.setTipoBoleto(tipoBoleto);
                        detalleVenta.setStatusNotificacion(2);

                        detalleVenta.setFolio(folios.get(i + posicionFolio));

                        DetalleVentaMovil detalleDB = this.repoDetalle.save(detalleVenta);
                        listaDetalles.add(detalleDB);
                    }
                }
            }

            // guardar transaccion
            Transacciones transaccion = new Transacciones();
            transaccion.setMontoPago(Double.parseDouble(ticket.get("total").toString()));
            transaccion.setUsuarioFinal(Integer.parseInt(ticket.get("usuario").toString()));
            transaccion.setFechaHora(fechaHora.toString());
            transaccion.setNumAutorizacion(ticket.get("numAutorizacion").toString());
            transaccion.setStatusAprobado(1);
            transaccion.setNumTransaccion(ticket.get("idTransaccion").toString());
            transaccion.setIdventaViaje(viajeGuardado.getIdVentaViaje());

            repoTransaccion.save(transaccion);
            //

            // crear pdf
            BarcodeToPDF pdf = new BarcodeToPDF();
            // pdf.codigoBarrasPdf2(viajeGuardado, listaDetalles, iva);
            if (tipoVIaje == 1 && viajeRegresoGuardado != null) {
                pdf.boletoPDFRedondo(viajeGuardado, viajeRegresoGuardado, listaDetalles, iva, repoAutobus);
            } else {
                pdf.boletoPDF(viajeGuardado, listaDetalles, iva, repoAutobus);
            }
            // enviar correo
            emailService.sendEmailTicket(ticket.get("correo").toString(), "COMPRA DE BOLETOS",
                    ticket.get("total").toString(), listaDetalles, viajeGuardado, ticket.get("numTarjeta").toString(),
                    ticket.get("numAutorizacion").toString(), ticket.get("idTransaccion").toString(), tipoVIaje);
            System.out.println("ok");

            return "ok";
        } else {
            return "fail";
        }

    }

    @Module("CentralBus - Mis Boletos/Descargar")
    @GetMapping("/resendTicket/{idVenta}/{isTour}")
    public ResponseEntity<?> resendTicketCB(@PathVariable(value = "idVenta") Long idVenta,
            @PathVariable(value = "isTour") Boolean isTour) throws IOException {
        try {
            BarcodeToPDF pdf = new BarcodeToPDF();
            if (isTour == true) {
                System.err.println("Buscando Tour...");
                DetalleVentaTourMovil ticket = null;
                ticket = repoDetalleTour.findById(idVenta)
                        .orElseThrow(
                                () -> new ResourceNotFoundException("tNo se encontro el Ticket con ID: " + idVenta));

                List<DetalleVentaTourMovil> ticketList = new ArrayList<>();
                ticketList.add(ticket);
                pdf.boletoPDFTour(ticket.getVentaTour(), ticketList, IVA / 100);

                Path pdfPath = Paths.get("barcode_example.pdf");
                byte[] pdfByte = Files.readAllBytes(pdfPath);

                // Convertir el archivo PDF a una cadena Base64
                String base64Pdf = Base64.encodeBase64String(pdfByte);

                return ResponseEntity.status(HttpStatus.OK).body(base64Pdf);

            } else {
                // Obtener el detalle del ticket por ID
                DetalleVentaMovil detalleTicket = repoDetalle.findById(idVenta)
                        .orElseThrow(
                                () -> new ResourceNotFoundException("No se encontró el Ticket con ID: " + idVenta));

                // Obtener la lista de detalles por fecha de compra y usuario final
                List<DetalleVentaMovil> listaDetalles = repoDetalle.findByFechaCompraVentaViajes(
                        detalleTicket.getVentaViajes().getFechaCompra(), detalleTicket.getUsuarioFinal());

                // Mapa para almacenar los tickets por fecha de viaje, ordenado por fecha
                Map<String, List<DetalleVentaMovil>> ticketsPorFechaViaje = listaDetalles.stream()
                        .collect(Collectors.groupingBy(DetalleVentaMovil::getFechaViaje, TreeMap::new,
                                Collectors.toList()));

                // Mapa para almacenar los tickets con claves numéricas
                Map<Integer, List<DetalleVentaMovil>> ticketsPorNumero = new LinkedHashMap<>();

                // Asignar claves numéricas secuenciales
                int key = 1;
                for (List<DetalleVentaMovil> listaTickets : ticketsPorFechaViaje.values()) {
                    ticketsPorNumero.put(key++, listaTickets);
                }

                Boolean isRedondo = ticketsPorFechaViaje.size() > 1 ? true : false;
                List<DetalleVentaMovil> detallesFiltrados = new ArrayList<>();

                VentaViajesMovil viajeGuardado = detalleTicket.getVentaViajes();

                if (isRedondo == true) {
                    List<DetalleVentaMovil> viajesDeIda = ticketsPorNumero.get(1);
                    List<DetalleVentaMovil> viajesDeVuelta = ticketsPorNumero.get(2);
                    for (int i = 0; i < viajesDeIda.size(); i++) {
                        if (detalleTicket.getNumeroAsiento() != viajesDeIda.get(i).getNumeroAsiento()) {
                            continue;
                        }
                        detallesFiltrados.add(viajesDeIda.get(i));
                        detallesFiltrados.add(viajesDeVuelta.get(i));
                        pdf.boletoPDFRedondo(viajesDeIda.get(i).getVentaViajes(),
                                viajesDeVuelta.get(i).getVentaViajes(), detallesFiltrados, IVA / 100, repoAutobus);
                    }
                } else {
                    for (DetalleVentaMovil ticket : listaDetalles) {
                        if (ticket.getId_detalle_venta() != detalleTicket.getId_detalle_venta()) {
                            continue;
                        }
                        detallesFiltrados.add(ticket);
                        pdf.boletoPDF(ticket.getVentaViajes(), detallesFiltrados, IVA / 100, repoAutobus);
                    }
                }

                Double totalPagado = 0.0;
                for (DetalleVentaMovil detalle : detallesFiltrados) {
                    totalPagado += detalle.getCosto();
                }

                Path pdfPath = Paths.get("barcode_example.pdf");
                byte[] pdfByte = Files.readAllBytes(pdfPath);

                // Convertir el archivo PDF a una cadena Base64
                String base64Pdf = Base64.encodeBase64String(pdfByte);

                return ResponseEntity.status(HttpStatus.OK).body(base64Pdf);
            }

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontro el Ticket con ID: " + idVenta);
        }
    }

    @Module("CentralBus - Comprar Boleto/Generar Boleto")
    @PostMapping("guardarTicket2")
    @Transactional
    public JSONObject insertTicket2(@RequestBody JSONObject ticket) {
        double iva = IVA / 100;

        // generar id de consolidado
        VentaViajesMovil viaje = new VentaViajesMovil();
        Afiliado afiliado = repoAfiliado.findById(Long.parseLong(ticket.get("afiliado").toString()))
                .orElseThrow(
                        () -> new ResourceNotFoundException("afiliado not found with id: " + ticket.get("afiliado")));
        Terminales origen = repoTerminal.findById(Long.parseLong(ticket.get("origen").toString()))
                .orElseThrow(() -> new ResourceNotFoundException("origen not found with id: " + ticket.get("origen")));
        Terminales destino = repoTerminal.findById(Long.parseLong(ticket.get("destino").toString()))
                .orElseThrow(
                        () -> new ResourceNotFoundException("destino not found with id: " + ticket.get("destino")));

        List<String> folios = (List<String>) ticket.get("folios");
        System.out.println("numero de folios " + folios.size());

        viaje.setAfiliado(afiliado);

        LocalDateTime fechaHora = LocalDateTime.now();
        viaje.setFechaCompra(fechaHora.toString());
        viaje.setRuta(Long.parseLong(ticket.get("ruta").toString()));
        viaje.setTerminalOrigen(origen);
        viaje.setTerminalDestino(destino);
        viaje.setTotalPagado(Double.parseDouble(ticket.get("total").toString()));
        if (ticket.get("promocion") != null) {
            viaje.setPromocion(Long.parseLong(ticket.get("promocion").toString()));
        }

        System.out.println("funciona aqui");
        VentaViajesMovil viajeGuardado = this.repo.save(viaje);
        VentaViajesMovil viajeRegresoGuardado = new VentaViajesMovil();

        // Almacenar información para correo
        List<DetalleVentaMovil> listaDetalles = new ArrayList<>();
        // boolean seleccionAsiento = (boolean) ticket.get("seleccionAsiento");
        // boolean seleccionAsientoRegreso = (boolean)
        // ticket.get("seleccionAsientoRegreso");
        //
        if (viajeGuardado != null) {
            System.out.println("funciona aqui3");
            int tipoVIaje = (int) ticket.get("tipoViaje");
            if (tipoVIaje == 0) {
                System.out.println("Sencillo");
            } else {
                System.out.println("Redondo");
            }
            // System.out.println("funciona");
            List<String> detalles = (List<String>) ticket.get("detalles");
            int posicionFolio = 0;
            for (int i = 0; i < detalles.size(); i++) {

                System.out.println("elementos " + i);
                System.out.println(detalles.get(i));
                String[] valores = detalles.get(i).split("%");
                System.out.println("ida " + valores[0]);

                // cambio de ubicacion de detalleventa instancia
                DetalleVentaMovil detalleVenta = new DetalleVentaMovil();

                Long tipoBoletoJson = Long.parseLong(valores[3]);
                // Long tipoBoletoFinal = Long.parseLong( valores[3] );
                // asignacion de tipo de planta y tipo de boleto( nuevo )
                if (tipoBoletoJson == 4l) {
                    tipoBoletoJson = 1l;
                    detalleVenta.setTipoPlanta(1);
                } else if (tipoBoletoJson == 5l) {
                    tipoBoletoJson = 2l;
                    detalleVenta.setTipoPlanta(1);
                } else if (tipoBoletoJson == 6l) {
                    tipoBoletoJson = 3l;
                    detalleVenta.setTipoPlanta(1);
                } else {
                    detalleVenta.setTipoPlanta(2);
                }

                // TipoBoleto tipoBoleto = repoTipoBoleto.findById(Long.parseLong(valores[3]))
                TipoBoleto tipoBoleto = repoTipoBoleto.findById(tipoBoletoJson)
                        .orElseThrow(() -> new ResourceNotFoundException("boleto not found with id: " + valores[3]));

                // se comentó detlle venta para poder utilizarlo mas arriba
                // DetalleVentaMovil detalleVenta = new DetalleVentaMovil();
                detalleVenta.setAutobus(Double.parseDouble(ticket.get("autobus").toString()));
                detalleVenta.setUsuarioFinal(Long.parseLong(ticket.get("usuario").toString()));
                detalleVenta.setFechaViaje(ticket.get("fechaViaje").toString());
                detalleVenta.setStatusDisponible(1);
                detalleVenta.setVentaViajes(viaje);

                if (tipoVIaje == 0) {
                    detalleVenta.setCosto(Double.parseDouble(valores[4]));
                } else {
                    detalleVenta.setCosto(Double.parseDouble(valores[5]));// se cambió para poder recibir nuevos datos
                }
                detalleVenta.setNombrePasajero(valores[2]);
                detalleVenta.setNumeroAsiento(Integer.parseInt(valores[0]));
                detalleVenta.setTipoBoleto(tipoBoleto);
                detalleVenta.setStatusNotificacion(2);

                detalleVenta.setFolio(folios.get(i));
                // posicionFolio=+1;
                posicionFolio = i;

                DetalleVentaMovil detalleDB = this.repoDetalle.save(detalleVenta);

                listaDetalles.add(detalleDB);
            }
            if (tipoVIaje == 1) {
                // agregar viaje redondo regreso
                VentaViajesMovil viajeRegreso = new VentaViajesMovil();
                viajeRegreso.setAfiliado(afiliado);
                viajeRegreso.setFechaCompra(fechaHora.toString());
                viajeRegreso.setRuta(Long.parseLong(ticket.get("rutaVuelta").toString()));
                viajeRegreso.setTerminalOrigen(destino);
                viajeRegreso.setTerminalDestino(origen);
                viajeRegreso.setTotalPagado(Double.parseDouble(ticket.get("total").toString()));

                if (ticket.get("promocionVuelta") != null) {
                    viajeRegreso.setPromocion(Long.parseLong(ticket.get("promocionVuelta").toString()));
                }
                viajeRegresoGuardado = repo.save(viajeRegreso);
                if (viajeRegresoGuardado != null) {
                    for (int i = 0; i < detalles.size(); i++) {

                        System.out.println("elementosVuelta " + i);
                        System.out.println(detalles.get(i));
                        String[] valores = detalles.get(i).split("%");
                        System.out.println("Vuelta " + valores[1]);

                        DetalleVentaMovil detalleVenta = new DetalleVentaMovil(); // se agrega
                        // se agrega lo siguiente
                        Long tipoBoletoVueltaJson = Long.parseLong(valores[4]);// tipo boleto vuelta
                        // asignacion de tipo de planta y tipo de boleto( nuevo )
                        if (tipoBoletoVueltaJson == 4l) {
                            tipoBoletoVueltaJson = 1l;
                            detalleVenta.setTipoPlanta(1);
                        } else if (tipoBoletoVueltaJson == 5l) {
                            tipoBoletoVueltaJson = 2l;
                            detalleVenta.setTipoPlanta(1);
                        } else if (tipoBoletoVueltaJson == 6l) {
                            tipoBoletoVueltaJson = 3l;
                            detalleVenta.setTipoPlanta(1);
                        } else {
                            detalleVenta.setTipoPlanta(2);
                        }
                        // fin de lo nuevo, se comento el tipo de boleto siguiente

                        // TipoBoleto tipoBoleto = repoTipoBoleto.findById(Long.parseLong(valores[3]))
                        TipoBoleto tipoBoleto = repoTipoBoleto.findById(tipoBoletoVueltaJson)
                                .orElseThrow(
                                        () -> new ResourceNotFoundException("boleto not found with id: " + valores[4]));
                        // () -> new ResourceNotFoundException("boleto not found with id: " +
                        // valores[3]));

                        // DetalleVentaMovil detalleVenta = new DetalleVentaMovil();
                        detalleVenta.setAutobus(Double.parseDouble(ticket.get("autobusRegreso").toString()));
                        // detalleVenta.setAutobus(Double.parseDouble(ticket.get("autobus").toString()));
                        detalleVenta.setUsuarioFinal(Long.parseLong(ticket.get("usuario").toString()));
                        detalleVenta.setFechaViaje(ticket.get("fechaViajeVuelta").toString());
                        detalleVenta.setStatusDisponible(1);
                        detalleVenta.setVentaViajes(viajeRegreso);
                        // detalleVenta.setCosto(Double.parseDouble(valores[5]));
                        detalleVenta.setCosto(Double.parseDouble(valores[6]));// se cambio a 6 para recibir el nuevo
                                                                              // dato
                        detalleVenta.setNombrePasajero(valores[2]);
                        detalleVenta.setNumeroAsiento(Integer.parseInt(valores[1]));
                        detalleVenta.setTipoBoleto(tipoBoleto);
                        detalleVenta.setStatusNotificacion(2);

                        // detalleVenta.setFolio(folios.get(i+posicionFolio));
                        detalleVenta.setFolio(folios.get(i + posicionFolio + 1));

                        DetalleVentaMovil detalleDB = this.repoDetalle.save(detalleVenta);
                        listaDetalles.add(detalleDB);
                    }
                }
            }

            // guardar transaccion
            Transacciones transaccion = new Transacciones();
            transaccion.setMontoPago(Double.parseDouble(ticket.get("total").toString()));
            transaccion.setUsuarioFinal(Integer.parseInt(ticket.get("usuario").toString()));
            transaccion.setFechaHora(fechaHora.toString());
            transaccion.setNumAutorizacion(ticket.get("numAutorizacion").toString());
            transaccion.setStatusAprobado(1);
            transaccion.setNumTransaccion(ticket.get("idTransaccion").toString());
            transaccion.setIdventaViaje(viajeGuardado.getIdVentaViaje());

            repoTransaccion.save(transaccion);
            //

            // crear pdf

            BarcodeToPDF pdf = new BarcodeToPDF();
            // pdf.codigoBarrasPdf2(viajeGuardado, listaDetalles, iva);
            boolean pdfCreado = false;
            if (tipoVIaje == 1 && viajeRegresoGuardado != null) {
                pdfCreado = pdf.boletoPDFRedondo(viajeGuardado, viajeRegresoGuardado, listaDetalles, iva, repoAutobus);
            } else {
                pdfCreado = pdf.boletoPDF(viajeGuardado, listaDetalles, iva, repoAutobus);
            }
            // enviar correo
            emailService.sendEmailTicket(ticket.get("correo").toString(), "COMPRA DE BOLETOS",
                    ticket.get("total").toString(), listaDetalles, viajeGuardado, ticket.get("numTarjeta").toString(),
                    ticket.get("numAutorizacion").toString(), ticket.get("idTransaccion").toString(), tipoVIaje);
            System.out.println("ok");
            JSONObject resp = new JSONObject();

            if (pdfCreado) {
                try {
                    // enviar pdf en la request
                    Path pdfPath = Paths.get("barcode_example.pdf");
                    byte[] pdfByte = Files.readAllBytes(pdfPath);

                    // Convertir el archivo PDF a una cadena Base64
                    String base64Pdf = Base64.encodeBase64String(pdfByte);
                    resp.put("idVentaViaje", listaDetalles.get(0).getVentaViajes().getIdVentaViaje());
                    resp.put("ticketPdf", base64Pdf);

                } catch (Exception e) {
                    // TODO: handle exception
                    resp.put("idVentaViaje", listaDetalles.get(0).getVentaViajes().getIdVentaViaje());
                    resp.put("ticketPdf", null);

                }
            } else {
                resp.put("idVentaViaje", listaDetalles.get(0).getVentaViajes().getIdVentaViaje());
                resp.put("ticketPdf", null);
            }
            return resp;
        } else {
            return null;
        }

    }

    /*
     * ---------------metodo para monitorear la transaccion por id------------------
     */
    @GetMapping("/transaccion/{idTransaccion}")
    public Transacciones getTransaccion(@PathVariable(value = "idTransaccion") String idTransaccion) {
        System.out.println("Obteniendo registro transaccion para: " + idTransaccion);
        Transacciones transaccion = repoTransaccion.findByNumTransaccion(idTransaccion);
        if (transaccion != null) {
            System.out.println("Status de transaccion para: " + transaccion.getStatusAprobado());
        } else {
            System.out.println("No se ha encontrado un registro con el id: " + idTransaccion);
        }

        return transaccion;
    }

    @Module("CentralBus - Comprar Boleto/Generar Boleto-Pendiente")
    @PostMapping("guardarTicketPendiente")
    @Transactional
    public JSONObject insertTicketPendiente(@RequestBody JSONObject ticket) {
        double iva = IVA / 100;

        // generar id de consolidado
        VentaViajesMovil viaje = new VentaViajesMovil();
        Afiliado afiliado = repoAfiliado.findById(Long.parseLong(ticket.get("afiliado").toString()))
                .orElseThrow(
                        () -> new ResourceNotFoundException("afiliado not found with id: " + ticket.get("afiliado")));
        Terminales origen = repoTerminal.findById(Long.parseLong(ticket.get("origen").toString()))
                .orElseThrow(() -> new ResourceNotFoundException("origen not found with id: " + ticket.get("origen")));
        Terminales destino = repoTerminal.findById(Long.parseLong(ticket.get("destino").toString()))
                .orElseThrow(
                        () -> new ResourceNotFoundException("destino not found with id: " + ticket.get("destino")));

        List<String> folios = (List<String>) ticket.get("folios");
        System.out.println("numero de folios " + folios.size());

        viaje.setAfiliado(afiliado);

        LocalDateTime fechaHora = LocalDateTime.now();
        viaje.setFechaCompra(fechaHora.toString());
        viaje.setRuta(Long.parseLong(ticket.get("ruta").toString()));
        viaje.setTerminalOrigen(origen);
        viaje.setTerminalDestino(destino);
        viaje.setTotalPagado(Double.parseDouble(ticket.get("total").toString()));
        if (ticket.get("promocion") != null) {
            viaje.setPromocion(Long.parseLong(ticket.get("promocion").toString()));
        }

        System.out.println("funciona aqui");
        VentaViajesMovil viajeGuardado = this.repo.save(viaje);
        VentaViajesMovil viajeRegresoGuardado = new VentaViajesMovil();

        // Almacenar información para correo
        List<DetalleVentaMovil> listaDetalles = new ArrayList<>();
        //
        if (viajeGuardado != null) {
            System.out.println("funciona aqui3");
            int tipoVIaje = (int) ticket.get("tipoViaje");
            if (tipoVIaje == 0) {
                System.out.println("Sencillo");
            } else {
                System.out.println("Redondo");
            }
            // System.out.println("funciona");
            List<String> detalles = (List<String>) ticket.get("detalles");
            int posicionFolio = 0;
            for (int i = 0; i < detalles.size(); i++) {

                System.out.println("elementos " + i);
                System.out.println(detalles.get(i));
                String[] valores = detalles.get(i).split("%");
                System.out.println("ida " + valores[0]);

                TipoBoleto tipoBoleto = repoTipoBoleto.findById(Long.parseLong(valores[3]))
                        .orElseThrow(() -> new ResourceNotFoundException("boleto not found with id: " + valores[3]));

                DetalleVentaMovil detalleVenta = new DetalleVentaMovil();
                detalleVenta.setAutobus(Double.parseDouble(ticket.get("autobus").toString()));
                detalleVenta.setUsuarioFinal(Long.parseLong(ticket.get("usuario").toString()));
                detalleVenta.setFechaViaje(ticket.get("fechaViaje").toString());
                detalleVenta.setStatusDisponible(4);// se le asigna el 4 como pendiente
                detalleVenta.setVentaViajes(viaje);

                detalleVenta.setCosto(Double.parseDouble(valores[4]));
                detalleVenta.setNombrePasajero(valores[2]);
                detalleVenta.setNumeroAsiento(Integer.parseInt(valores[0]));
                detalleVenta.setTipoBoleto(tipoBoleto);
                detalleVenta.setStatusNotificacion(2);

                detalleVenta.setFolio(folios.get(i));
                // posicionFolio=+1;
                posicionFolio = i;

                DetalleVentaMovil detalleDB = this.repoDetalle.save(detalleVenta);

                listaDetalles.add(detalleDB);
            }
            if (tipoVIaje == 1) {
                // agregar viaje redondo regreso
                VentaViajesMovil viajeRegreso = new VentaViajesMovil();
                viajeRegreso.setAfiliado(afiliado);
                viajeRegreso.setFechaCompra(fechaHora.toString());
                viajeRegreso.setRuta(Long.parseLong(ticket.get("rutaVuelta").toString()));
                viajeRegreso.setTerminalOrigen(destino);
                viajeRegreso.setTerminalDestino(origen);
                viajeRegreso.setTotalPagado(Double.parseDouble(ticket.get("total").toString()));

                if (ticket.get("promocionVuelta") != null) {
                    viajeRegreso.setPromocion(Long.parseLong(ticket.get("promocionVuelta").toString()));
                }
                viajeRegresoGuardado = repo.save(viajeRegreso);
                if (viajeRegresoGuardado != null) {
                    for (int i = 0; i < detalles.size(); i++) {

                        System.out.println("elementosVuelta " + i);
                        System.out.println(detalles.get(i));
                        String[] valores = detalles.get(i).split("%");
                        System.out.println("Vuelta " + valores[1]);

                        TipoBoleto tipoBoleto = repoTipoBoleto.findById(Long.parseLong(valores[3]))
                                .orElseThrow(
                                        () -> new ResourceNotFoundException("boleto not found with id: " + valores[3]));

                        DetalleVentaMovil detalleVenta = new DetalleVentaMovil();
                        detalleVenta.setAutobus(Double.parseDouble(ticket.get("autobus").toString()));
                        detalleVenta.setUsuarioFinal(Long.parseLong(ticket.get("usuario").toString()));
                        detalleVenta.setFechaViaje(ticket.get("fechaViajeVuelta").toString());
                        detalleVenta.setStatusDisponible(4);// se asigna el 4 como pendiente
                        detalleVenta.setVentaViajes(viajeRegreso);
                        // se modificó el total, antes tenia 4, en valores, deberia de funcionar
                        detalleVenta.setCosto(Double.parseDouble(valores[5]));
                        detalleVenta.setNombrePasajero(valores[2]);
                        detalleVenta.setNumeroAsiento(Integer.parseInt(valores[1]));
                        detalleVenta.setTipoBoleto(tipoBoleto);
                        detalleVenta.setStatusNotificacion(2);

                        // detalleVenta.setFolio(folios.get(i+posicionFolio));
                        detalleVenta.setFolio(folios.get(i + posicionFolio + 1));

                        DetalleVentaMovil detalleDB = this.repoDetalle.save(detalleVenta);
                        listaDetalles.add(detalleDB);
                    }
                }
            }

            // guardar transaccion
            Transacciones transaccion = new Transacciones();
            transaccion.setMontoPago(Double.parseDouble(ticket.get("total").toString()));
            transaccion.setUsuarioFinal(Integer.parseInt(ticket.get("usuario").toString()));
            transaccion.setFechaHora(fechaHora.toString());
            // transaccion.setNumAutorizacion(ticket.get("numAutorizacion").toString());
            transaccion.setStatusAprobado(4);// pendiente
            transaccion.setNumTransaccion(ticket.get("idTransaccion").toString());
            transaccion.setIdventaViaje(viajeGuardado.getIdVentaViaje());

            repoTransaccion.save(transaccion);
            //

            /*
             * // crear pdf
             * BarcodeToPDF pdf = new BarcodeToPDF();
             * // pdf.codigoBarrasPdf2(viajeGuardado, listaDetalles, iva);
             * boolean pdfCreado = false;
             * if (tipoVIaje == 1 && viajeRegresoGuardado != null) {
             * pdfCreado = pdf.boletoPDFRedondo(viajeGuardado, viajeRegresoGuardado,
             * listaDetalles, iva);
             * } else {
             * pdfCreado = pdf.boletoPDF(viajeGuardado, listaDetalles, iva);
             * }
             * // enviar correo
             * emailService.sendEmailTicket(ticket.get("correo").toString(),
             * "COMPRA DE BOLETOS",
             * ticket.get("total").toString(), listaDetalles, viajeGuardado);
             * System.out.println("ok");
             */
            JSONObject resp = new JSONObject();
            /*
             * if (pdfCreado) {
             * try {
             * // enviar pdf en la request
             * Path pdfPath = Paths.get("barcode_example.pdf");
             * byte[] pdfByte = Files.readAllBytes(pdfPath);
             * 
             * // Convertir el archivo PDF a una cadena Base64
             * String base64Pdf = Base64.encodeBase64String(pdfByte);
             * resp.put("idVentaViaje",
             * listaDetalles.get(0).getVentaViajes().getIdVentaViaje());
             * resp.put("ticketPdf", base64Pdf);
             * 
             * } catch (Exception e) {
             * // TODO: handle exception
             * resp.put("idVentaViaje",
             * listaDetalles.get(0).getVentaViajes().getIdVentaViaje());
             * resp.put("ticketPdf", null);
             * 
             * }
             * } else {
             * resp.put("idVentaViaje",
             * listaDetalles.get(0).getVentaViajes().getIdVentaViaje());
             * resp.put("ticketPdf", null);
             * }
             */
            return resp;
        } else {
            return null;
        }

    }

    @Module("CentralBus - Comprar Boleto/Invitado/Generar Boleto")
    @PostMapping("guardarTicketInvitado")
    @Transactional
    public JSONObject insertTicketInvitado(@RequestBody JSONObject ticket) {
        double iva = IVA / 100;

        // generar id de consolidado
        VentaViajesMovil viaje = new VentaViajesMovil();
        Afiliado afiliado = repoAfiliado.findById(Long.parseLong(ticket.get("afiliado").toString()))
                .orElseThrow(
                        () -> new ResourceNotFoundException("afiliado not found with id: " + ticket.get("afiliado")));
        Terminales origen = repoTerminal.findById(Long.parseLong(ticket.get("origen").toString()))
                .orElseThrow(() -> new ResourceNotFoundException("origen not found with id: " + ticket.get("origen")));
        Terminales destino = repoTerminal.findById(Long.parseLong(ticket.get("destino").toString()))
                .orElseThrow(
                        () -> new ResourceNotFoundException("destino not found with id: " + ticket.get("destino")));

        List<String> folios = (List<String>) ticket.get("folios");
        System.out.println("numero de folios " + folios.size());

        viaje.setAfiliado(afiliado);

        LocalDateTime fechaHora = LocalDateTime.now();
        viaje.setFechaCompra(fechaHora.toString());
        viaje.setRuta(Long.parseLong(ticket.get("ruta").toString()));
        viaje.setTerminalOrigen(origen);
        viaje.setTerminalDestino(destino);
        viaje.setTotalPagado(Double.parseDouble(ticket.get("total").toString()));
        if (ticket.get("promocion") != null) {
            viaje.setPromocion(Long.parseLong(ticket.get("promocion").toString()));
        }

        System.out.println("funciona aqui");
        VentaViajesMovil viajeGuardado = this.repo.save(viaje);
        VentaViajesMovil viajeRegresoGuardado = new VentaViajesMovil();

        // Almacenar información para correo
        List<DetalleVentaMovil> listaDetalles = new ArrayList<>();
        //
        if (viajeGuardado != null) {
            System.out.println("funciona aqui3");
            int tipoVIaje = (int) ticket.get("tipoViaje");
            if (tipoVIaje == 0) {
                System.out.println("Sencillo");
            } else {
                System.out.println("Redondo");
            }
            // System.out.println("funciona");
            List<String> detalles = (List<String>) ticket.get("detalles");
            int posicionFolio = 0;
            for (int i = 0; i < detalles.size(); i++) {

                System.out.println("elementos " + i);
                System.out.println(detalles.get(i));
                String[] valores = detalles.get(i).split("%");
                System.out.println("ida " + valores[0]);

                // cambio de ubicacion de detalleventa instancia
                DetalleVentaMovil detalleVenta = new DetalleVentaMovil();

                Long tipoBoletoJson = Long.parseLong(valores[3]);
                // Long tipoBoletoFinal = Long.parseLong( valores[3] );
                // asignacion de tipo de planta y tipo de boleto( nuevo )
                if (tipoBoletoJson == 4l) {
                    tipoBoletoJson = 1l;
                    detalleVenta.setTipoPlanta(1);
                } else if (tipoBoletoJson == 5l) {
                    tipoBoletoJson = 2l;
                    detalleVenta.setTipoPlanta(1);
                } else if (tipoBoletoJson == 6l) {
                    tipoBoletoJson = 3l;
                    detalleVenta.setTipoPlanta(1);
                } else {
                    detalleVenta.setTipoPlanta(2);
                }

                // TipoBoleto tipoBoleto =
                // repoTipoBoleto.findById(Long.parseLong(valores[3]))//se comentó este dato
                TipoBoleto tipoBoleto = repoTipoBoleto.findById(tipoBoletoJson)
                        .orElseThrow(() -> new ResourceNotFoundException("boleto not found with id: " + valores[3]));

                // DetalleVentaMovil detalleVenta = new DetalleVentaMovil(); se comentó este
                // dato
                detalleVenta.setAutobus(Double.parseDouble(ticket.get("autobus").toString()));
                // detalleVenta.setUsuarioFinal(Long.parseLong(ticket.get("usuario").toString()));
                // se asignará el correo
                detalleVenta.setFechaViaje(ticket.get("fechaViaje").toString());
                detalleVenta.setStatusDisponible(1);
                detalleVenta.setVentaViajes(viaje);

                if (tipoVIaje == 0) {
                    detalleVenta.setCosto(Double.parseDouble(valores[4]));
                } else {
                    detalleVenta.setCosto(Double.parseDouble(valores[5]));// en espera de cambios
                }
                detalleVenta.setNombrePasajero(valores[2]);
                detalleVenta.setNumeroAsiento(Integer.parseInt(valores[0]));
                detalleVenta.setTipoBoleto(tipoBoleto);
                detalleVenta.setStatusNotificacion(2);

                detalleVenta.setFolio(folios.get(i));
                posicionFolio = +1;

                DetalleVentaMovil detalleDB = this.repoDetalle.save(detalleVenta);
                listaDetalles.add(detalleDB);
            }
            if (tipoVIaje == 1) {
                // agregar viaje redondo regreso
                VentaViajesMovil viajeRegreso = new VentaViajesMovil();
                viajeRegreso.setAfiliado(afiliado);
                viajeRegreso.setFechaCompra(fechaHora.toString());
                viajeRegreso.setRuta(Long.parseLong(ticket.get("rutaVuelta").toString()));
                viajeRegreso.setTerminalOrigen(destino);
                viajeRegreso.setTerminalDestino(origen);
                viajeRegreso.setTotalPagado(Double.parseDouble(ticket.get("total").toString()));

                if (ticket.get("promocionVuelta") != null) {
                    viajeRegreso.setPromocion(Long.parseLong(ticket.get("promocionVuelta").toString()));
                }
                viajeRegresoGuardado = repo.save(viajeRegreso);
                if (viajeRegresoGuardado != null) {
                    for (int i = 0; i < detalles.size(); i++) {

                        System.out.println("elementosVuelta " + i);
                        System.out.println(detalles.get(i));
                        String[] valores = detalles.get(i).split("%");
                        System.out.println("Vuelta " + valores[1]);

                        DetalleVentaMovil detalleVenta = new DetalleVentaMovil(); // se agrega
                        // se agrega lo siguiente
                        Long tipoBoletoVueltaJson = Long.parseLong(valores[4]);// tipo boleto vuelta
                        // asignacion de tipo de planta y tipo de boleto( nuevo )
                        if (tipoBoletoVueltaJson == 4l) {
                            tipoBoletoVueltaJson = 1l;
                            detalleVenta.setTipoPlanta(1);
                        } else if (tipoBoletoVueltaJson == 5l) {
                            tipoBoletoVueltaJson = 2l;
                            detalleVenta.setTipoPlanta(1);
                        } else if (tipoBoletoVueltaJson == 6l) {
                            tipoBoletoVueltaJson = 3l;
                            detalleVenta.setTipoPlanta(1);
                        } else {
                            detalleVenta.setTipoPlanta(2);
                        }
                        // fin de lo nuevo, se comento el tipo de boleto siguiente
                        // TipoBoleto tipoBoleto = repoTipoBoleto.findById(Long.parseLong(valores[3]))
                        TipoBoleto tipoBoleto = repoTipoBoleto.findById(tipoBoletoVueltaJson)
                                .orElseThrow(
                                        () -> new ResourceNotFoundException("boleto not found with id: " + valores[4]));
                        // () -> new ResourceNotFoundException("boleto not found with id: " +
                        // valores[3]));//se comentó esta linea

                        // DetalleVentaMovil detalleVenta = new DetalleVentaMovil(); //se comentó esta
                        // linea
                        detalleVenta.setAutobus(Double.parseDouble(ticket.get("autobusRegreso").toString()));
                        // detalleVenta.setAutobus(Double.parseDouble(ticket.get("autobus").toString()));
                        // se agregará el correo
                        // detalleVenta.setUsuarioFinal(Long.parseLong(ticket.get("usuario").toString()));
                        detalleVenta.setFechaViaje(ticket.get("fechaViajeVuelta").toString());
                        detalleVenta.setStatusDisponible(1);
                        detalleVenta.setVentaViajes(viajeRegreso);
                        // se modificó el total, antes tenia 5, en valores, deberia de funcionar
                        detalleVenta.setCosto(Double.parseDouble(valores[6]));
                        // detalleVenta.setCosto(Double.parseDouble(valores[5]));
                        detalleVenta.setNombrePasajero(valores[2]);
                        detalleVenta.setNumeroAsiento(Integer.parseInt(valores[1]));
                        detalleVenta.setTipoBoleto(tipoBoleto);
                        detalleVenta.setStatusNotificacion(2);

                        detalleVenta.setFolio(folios.get(i + posicionFolio));

                        DetalleVentaMovil detalleDB = this.repoDetalle.save(detalleVenta);
                        listaDetalles.add(detalleDB);
                    }
                }
            }

            // guardar transaccion
            Transacciones transaccion = new Transacciones();
            transaccion.setMontoPago(Double.parseDouble(ticket.get("total").toString()));
            // se agregará el correo
            // transaccion.setUsuarioFinal(
            // Integer.parseInt(ticket.get("usuario").toString()) );
            transaccion.setCorreo(ticket.get("correo").toString());
            transaccion.setFechaHora(fechaHora.toString());
            transaccion.setNumAutorizacion(ticket.get("numAutorizacion").toString());
            transaccion.setStatusAprobado(1);
            transaccion.setNumTransaccion(ticket.get("idTransaccion").toString());
            transaccion.setIdventaViaje(viajeGuardado.getIdVentaViaje());

            repoTransaccion.save(transaccion);
            //

            // crear pdf
            BarcodeToPDF pdf = new BarcodeToPDF();
            // pdf.codigoBarrasPdf2(viajeGuardado, listaDetalles, iva);
            boolean pdfCreado = false;
            if (tipoVIaje == 1 && viajeRegresoGuardado != null) {
                pdfCreado = pdf.boletoPDFRedondo(viajeGuardado, viajeRegresoGuardado, listaDetalles, iva, repoAutobus);
            } else {
                pdfCreado = pdf.boletoPDF(viajeGuardado, listaDetalles, iva, repoAutobus);
            }
            // enviar correo
            emailService.sendEmailTicket(ticket.get("correo").toString(), "COMPRA DE BOLETOS",
                    ticket.get("total").toString(), listaDetalles, viajeGuardado, ticket.get("numTarjeta").toString(),
                    ticket.get("numAutorizacion").toString(), ticket.get("idTransaccion").toString(), tipoVIaje);
            System.out.println("ok");
            JSONObject resp = new JSONObject();

            if (pdfCreado) {
                try {
                    // enviar pdf en la request
                    Path pdfPath = Paths.get("barcode_example.pdf");
                    byte[] pdfByte = Files.readAllBytes(pdfPath);

                    // Convertir el archivo PDF a una cadena Base64
                    String base64Pdf = Base64.encodeBase64String(pdfByte);
                    resp.put("idVentaViaje", listaDetalles.get(0).getVentaViajes().getIdVentaViaje());
                    resp.put("ticketPdf", base64Pdf);

                } catch (Exception e) {
                    // TODO: handle exception
                    resp.put("idVentaViaje", listaDetalles.get(0).getVentaViajes().getIdVentaViaje());
                    resp.put("ticketPdf", null);

                }
            } else {
                resp.put("idVentaViaje", listaDetalles.get(0).getVentaViajes().getIdVentaViaje());
                resp.put("ticketPdf", null);
            }
            return resp;
        } else {
            return null;
        }

    }

    // metodos para insertar boletos tour---------------------------------
    /**
     * Insertar boleto tour
     * 
     * 
     * {
     * "afiliado": 2,
     * "origen": 26,
     * "destino": 27,
     * "cajero": 2,
     * "tour": 7,
     * "total": 500,
     * "tipoBoleto":2,
     * "autobus": 25,
     * "costo":500,
     * "fechaViaje":"2023-09-09 14:00:00",
     * "folio":"1234567890123456",
     * "nombre":"Prueba postman",
     * "asiento": 2,
     * "usuario":11
     * }
     * 
     * @throws MessagingException
     */
    @Module("CentralBus - Mis Boletos/Generar Boleto")
    @PostMapping("guardarTicketTour")
    @Transactional
    public JSONObject insertTicketTour(@RequestBody JSONObject ticket) {
        double iva = IVA / 100;

        VentaTourMovil viaje = new VentaTourMovil();
        Afiliado afiliado = repoAfiliado.findById(Long.parseLong(ticket.get("afiliado").toString()))
                .orElseThrow(
                        () -> new ResourceNotFoundException("afiliado not found with id: " + ticket.get("afiliado")));

        TourMovil tour = repoTourMovil.findById(Long.parseLong(ticket.get("tour").toString()))
                .orElseThrow(() -> new ResourceNotFoundException("destino not found with id: " + ticket.get("tour")));

        List<String> folios = (List<String>) ticket.get("folios");
        System.out.println("numero de folios " + folios.size());

        viaje.setTotalPagado(Double.parseDouble(ticket.get("total").toString()));
        LocalDateTime fechaHora = LocalDateTime.now();
        viaje.setFechaCompra(fechaHora.toString());
        viaje.setTour(tour);
        viaje.setAfiliado(afiliado);

        if (ticket.get("promocion") != null) {
            viaje.setPromocion(Long.parseLong(ticket.get("promocion").toString()));
        }

        VentaTourMovil viajeGuardado = this.repoTour.save(viaje);

        // Almacenar información para correo
        List<DetalleVentaTourMovil> listaDetalles = new ArrayList<>();
        //
        if (viajeGuardado != null) {
            List<String> detalles = (List<String>) ticket.get("detalles");
            int posicionFolio = 0;
            for (int i = 0; i < detalles.size(); i++) {

                System.out.println("elementos " + i);
                System.out.println(detalles.get(i));
                String[] valores = detalles.get(i).split("%");
                System.out.println("ida " + valores[0]);

                TipoBoleto tipoBoleto = repoTipoBoleto.findById(Long.parseLong(valores[3]))
                        .orElseThrow(() -> new ResourceNotFoundException("boleto not found with id: " + valores[3]));

                DetalleVentaTourMovil detalleVenta = new DetalleVentaTourMovil();
                detalleVenta.setAutobus(Double.parseDouble(ticket.get("autobus").toString()));
                detalleVenta.setUsuarioFinal(Long.parseLong(ticket.get("usuario").toString()));
                detalleVenta.setFechaViaje(ticket.get("fechaViaje").toString());
                detalleVenta.setStatusDisponible(1);

                detalleVenta.setVentaTour(viajeGuardado);

                detalleVenta.setCosto(Double.parseDouble(valores[4]));
                detalleVenta.setNombrePasajero(valores[2]);
                detalleVenta.setNumeroAsiento(Integer.parseInt(valores[0]));
                detalleVenta.setTipoBoleto(tipoBoleto);
                detalleVenta.setFolio(folios.get(i));
                posicionFolio = +1;

                DetalleVentaTourMovil detalleDB = this.repoDetalleTour.save(detalleVenta);
                listaDetalles.add(detalleDB);
            }

            // guardar transaccion
            Transacciones transaccion = new Transacciones();
            transaccion.setMontoPago(Double.parseDouble(ticket.get("total").toString()));
            transaccion.setUsuarioFinal(Integer.parseInt(ticket.get("usuario").toString()));
            transaccion.setFechaHora(fechaHora.toString());
            transaccion.setNumAutorizacion(ticket.get("numAutorizacion").toString());
            transaccion.setStatusAprobado(1);
            transaccion.setNumTransaccion(ticket.get("idTransaccion").toString());
            transaccion.setIdVentaTour(viajeGuardado.getIdVentaTour());

            repoTransaccion.save(transaccion);
            //

            // crear pdf
            BarcodeToPDF pdf = new BarcodeToPDF();
            boolean pdfCreado = false;
            // pdf.codigoBarrasPdfTour2(viajeGuardado, listaDetalles, iva);
            pdfCreado = pdf.boletoPDFTour(viajeGuardado, listaDetalles, iva);
            // enviar correo
            // emailService.sendEmailTicketTour(ticket.get("correo").toString(), "COMPRA DE
            // BOLETOS",
            // ticket.get("total").toString(), listaDetalles, viajeGuardado);
            emailService.sendEmailTicketTour(ticket.get("correo").toString(), "COMPRA DE BOLETOS",
                    ticket.get("total").toString(), listaDetalles, viajeGuardado, ticket.get("numTarjeta").toString(),
                    ticket.get("numAutorizacion").toString(), ticket.get("idTransaccion").toString());

            // return "ok";
            System.out.println("ok");
            JSONObject resp = new JSONObject();

            if (pdfCreado) {
                try {
                    // enviar pdf en la request
                    Path pdfPath = Paths.get("barcode_example.pdf");
                    byte[] pdfByte = Files.readAllBytes(pdfPath);

                    // Convertir el archivo PDF a una cadena Base64
                    String base64Pdf = Base64.encodeBase64String(pdfByte);
                    resp.put("idVentaViaje", listaDetalles.get(0).getVentaTour().getIdVentaTour());
                    resp.put("ticketPdf", base64Pdf);

                } catch (Exception e) {
                    // TODO: handle exception
                    resp.put("idVentaViaje", listaDetalles.get(0).getVentaTour().getIdVentaTour());
                    resp.put("ticketPdf", null);

                }
            } else {
                resp.put("idVentaViaje", listaDetalles.get(0).getVentaTour().getIdVentaTour());
                resp.put("ticketPdf", null);
            }
            return resp;
        } else {
            return null;
        }

    }

    /**
     * Insertar boleto tour Invitado
     * 
     * 
     * {
     * "afiliado": 2,
     * "origen": 26,
     * "destino": 27,
     * "cajero": 2,
     * "tour": 7,
     * "total": 500,
     * "tipoBoleto":2,
     * "autobus": 25,
     * "costo":500,
     * "fechaViaje":"2023-09-09 14:00:00",
     * "folio":"1234567890123456",
     * "nombre":"Prueba postman",
     * "asiento": 2,
     * "usuario":11
     * }
     * 
     * @throws MessagingException
     */
    @Module("CentralBus - Mis Boletos/Invitado/Generar Boleto")
    @PostMapping("guardarTicketTourInvitado")
    @Transactional
    public JSONObject insertTicketTourInvitado(@RequestBody JSONObject ticket) {
        double iva = IVA / 100;

        VentaTourMovil viaje = new VentaTourMovil();
        Afiliado afiliado = repoAfiliado.findById(Long.parseLong(ticket.get("afiliado").toString()))
                .orElseThrow(
                        () -> new ResourceNotFoundException("afiliado not found with id: " + ticket.get("afiliado")));

        TourMovil tour = repoTourMovil.findById(Long.parseLong(ticket.get("tour").toString()))
                .orElseThrow(() -> new ResourceNotFoundException("destino not found with id: " + ticket.get("tour")));

        List<String> folios = (List<String>) ticket.get("folios");
        System.out.println("numero de folios " + folios.size());

        viaje.setTotalPagado(Double.parseDouble(ticket.get("total").toString()));
        LocalDateTime fechaHora = LocalDateTime.now();
        viaje.setFechaCompra(fechaHora.toString());
        viaje.setTour(tour);
        viaje.setAfiliado(afiliado);

        if (ticket.get("promocion") != null) {
            viaje.setPromocion(Long.parseLong(ticket.get("promocion").toString()));
        }

        VentaTourMovil viajeGuardado = this.repoTour.save(viaje);

        // Almacenar información para correo
        List<DetalleVentaTourMovil> listaDetalles = new ArrayList<>();
        //
        if (viajeGuardado != null) {
            List<String> detalles = (List<String>) ticket.get("detalles");
            int posicionFolio = 0;
            for (int i = 0; i < detalles.size(); i++) {

                System.out.println("elementos " + i);
                System.out.println(detalles.get(i));
                String[] valores = detalles.get(i).split("%");
                System.out.println("ida " + valores[0]);

                TipoBoleto tipoBoleto = repoTipoBoleto.findById(Long.parseLong(valores[3]))
                        .orElseThrow(() -> new ResourceNotFoundException("boleto not found with id: " + valores[3]));

                DetalleVentaTourMovil detalleVenta = new DetalleVentaTourMovil();
                detalleVenta.setAutobus(Double.parseDouble(ticket.get("autobus").toString()));
                // el correo es el que se almacenará
                // detalleVenta.setUsuarioFinal(Long.parseLong(ticket.get("usuario").toString()));
                detalleVenta.setFechaViaje(ticket.get("fechaViaje").toString());
                detalleVenta.setStatusDisponible(1);

                detalleVenta.setVentaTour(viajeGuardado);

                detalleVenta.setCosto(Double.parseDouble(valores[4]));
                detalleVenta.setNombrePasajero(valores[2]);
                detalleVenta.setNumeroAsiento(Integer.parseInt(valores[0]));
                detalleVenta.setTipoBoleto(tipoBoleto);
                detalleVenta.setFolio(folios.get(i));
                posicionFolio = +1;

                DetalleVentaTourMovil detalleDB = this.repoDetalleTour.save(detalleVenta);
                listaDetalles.add(detalleDB);
            }

            // guardar transaccion
            Transacciones transaccion = new Transacciones();
            transaccion.setMontoPago(Double.parseDouble(ticket.get("total").toString()));
            // transaccion.setUsuarioFinal(
            // Integer.parseInt(ticket.get("usuario").toString()) );
            transaccion.setCorreo(ticket.get("correo").toString());
            transaccion.setFechaHora(fechaHora.toString());
            transaccion.setNumAutorizacion(ticket.get("numAutorizacion").toString());
            transaccion.setStatusAprobado(1);
            transaccion.setNumTransaccion(ticket.get("idTransaccion").toString());
            transaccion.setIdVentaTour(viajeGuardado.getIdVentaTour());

            repoTransaccion.save(transaccion);
            //

            // crear pdf
            BarcodeToPDF pdf = new BarcodeToPDF();
            boolean pdfCreado = false;
            // pdf.codigoBarrasPdfTour2(viajeGuardado, listaDetalles, iva);
            pdfCreado = pdf.boletoPDFTour(viajeGuardado, listaDetalles, iva);
            // enviar correo
            // emailService.sendEmailTicketTour(ticket.get("correo").toString(), "COMPRA DE
            // BOLETOS",
            // ticket.get("total").toString(), listaDetalles, viajeGuardado);
            emailService.sendEmailTicketTour(ticket.get("correo").toString(), "COMPRA DE BOLETOS",
                    ticket.get("total").toString(), listaDetalles, viajeGuardado, ticket.get("numTarjeta").toString(),
                    ticket.get("numAutorizacion").toString(), ticket.get("idTransaccion").toString());

            // return "ok";
            System.out.println("ok");
            JSONObject resp = new JSONObject();

            if (pdfCreado) {
                try {
                    // enviar pdf en la request
                    Path pdfPath = Paths.get("barcode_example.pdf");
                    byte[] pdfByte = Files.readAllBytes(pdfPath);

                    // Convertir el archivo PDF a una cadena Base64
                    String base64Pdf = Base64.encodeBase64String(pdfByte);
                    resp.put("idVentaViaje", listaDetalles.get(0).getVentaTour().getIdVentaTour());
                    resp.put("ticketPdf", base64Pdf);

                } catch (Exception e) {
                    // TODO: handle exception
                    resp.put("idVentaViaje", listaDetalles.get(0).getVentaTour().getIdVentaTour());
                    resp.put("ticketPdf", null);

                }
            } else {
                resp.put("idVentaViaje", listaDetalles.get(0).getVentaTour().getIdVentaTour());
                resp.put("ticketPdf", null);
            }
            return resp;
        } else {
            return null;
        }

    }

    // fin de metodo para insertar boletos tour

    @PostMapping("guardarVentaTicket")
    public VentaViajesMovil insertVentaTicket(@RequestBody JSONObject ticket) {
        VentaViajesMovil viaje = new VentaViajesMovil();
        Afiliado afiliado = repoAfiliado.findById(Long.parseLong(ticket.get("afiliado").toString()))
                .orElseThrow(
                        () -> new ResourceNotFoundException("afiliado not found with id: " + ticket.get("afiliado")));
        Terminales origen = repoTerminal.findById(Long.parseLong(ticket.get("origen").toString()))
                .orElseThrow(() -> new ResourceNotFoundException("origen not found with id: " + ticket.get("origen")));
        Terminales destino = repoTerminal.findById(Long.parseLong(ticket.get("destino").toString()))
                .orElseThrow(
                        () -> new ResourceNotFoundException("destino not found with id: " + ticket.get("destino")));

        viaje.setAfiliado(afiliado);
        // viaje.setCajero(Long.parseLong(ticket.get("cajero").toString()));
        LocalDateTime fechaHora = LocalDateTime.now();
        viaje.setFechaCompra(fechaHora.toString());
        viaje.setRuta(Long.parseLong(ticket.get("ruta").toString()));
        viaje.setTerminalOrigen(origen);
        viaje.setTerminalDestino(destino);
        viaje.setTotalPagado(Double.parseDouble(ticket.get("total").toString()));

        return repo.save(viaje);
    }

    @PostMapping("guardarDetalleTicket")
    public String insertDetalleTicket(@RequestBody DetalleVentaMovil detalles) {

        VentaViajesMovil ventaViajesMovil = repo.findById(detalles.getVentaViajes().getIdVentaViaje())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "venta not found with id: " + detalles.getVentaViajes().getIdVentaViaje()));

        TipoBoleto tipoBoleto = repoTipoBoleto.findById(detalles.getTipoBoleto().getIdc_tipoBoleto())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "tipoBoleto not found with id: " + detalles.getTipoBoleto().getIdc_tipoBoleto()));

        detalles.setTipoBoleto(tipoBoleto);
        detalles.setVentaViajes(ventaViajesMovil);
        detalles.setStatusDisponible(1);

        this.repoDetalle.save(detalles);

        return "ok";

    }

    @PutMapping("actualizarNotificacionTicket")
    @Transactional
    public String notificacionTicket(@RequestBody JSONObject viaje) {
        System.out.println(viaje.get("idViaje"));
        List<DetalleVentaMovil> detallesMovil = repoDetalle
                .findByVentaViajesIdVentaViaje(Long.parseLong(viaje.get("idViaje").toString()));

        for (int i = 0; i < detallesMovil.size(); i++) {
            detallesMovil.get(i).setStatusNotificacion(Integer.parseInt(viaje.get("statusNotificacion").toString()));
            repoDetalle.save(detallesMovil.get(i));
        }

        return "ok";

    }

    @PutMapping("actualizarNotificacionTicketTour")
    @Transactional
    public String notificacionTicketTour(@RequestBody JSONObject viaje) {
        System.out.println(viaje.get("idViaje"));
        List<DetalleVentaTourMovil> detallesMovil = repoDetalleTour
                .findByVentaTourIdVentaTour(Long.parseLong(viaje.get("idViaje").toString()));

        for (int i = 0; i < detallesMovil.size(); i++) {
            detallesMovil.get(i).setStatusNotificacion(Integer.parseInt(viaje.get("statusNotificacion").toString()));
            repoDetalleTour.save(detallesMovil.get(i));
        }

        return "ok";

    }

}
