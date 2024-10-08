package com.gorigeek.springboot.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gorigeek.springboot.Module;
import com.gorigeek.springboot.entity.TourHasTransporte;
import com.gorigeek.springboot.entity.TourMovil;
import com.gorigeek.springboot.exception.ResourceNotFoundException;
import com.gorigeek.springboot.repository.AfiliadoRepository;
import com.gorigeek.springboot.repository.HastransporteTourRepository;
import com.gorigeek.springboot.repository.TourMovilRepository;

@RestController
@RequestMapping("/api/tour")
public class TourController {

    @Autowired // Inyecta el repositorio
    private HastransporteTourRepository repo;

    @Autowired
    private TourMovilRepository repoTour;

    // obtener todos los boletos
    @GetMapping
    public List<TourHasTransporte> getAllTour() {

        return repo.findAll();

    }

    // @GetMapping("/tourHorario1/{ciudadOrigen}/{ciudadDestino}")
    // public List<TourHasTransporte> getByTourHorario1(@PathVariable(value =
    // "ciudadOrigen")Long idOrigen, @PathVariable(value = "ciudadDestino")Long
    // idDestino ){
    // List<TourHasTransporte> listaTransporte= repo.findByIdtours(idOrigen,
    // idDestino);
    // return listaTransporte;
    // }
    
    @Module("CentralBus - Comprar Tour/Obtener Horarios")
    @GetMapping("/tourHorario2/{idTour}")
    public List<TourHasTransporte> getByTourHorario2(@PathVariable(value = "idTour") Long idTour) throws Exception {
        try {
            TourMovil tourMovil = repoTour.findById(idTour)
                    .orElseThrow(() -> new ResourceNotFoundException("idTour not found with id: " + idTour));

            List<TourHasTransporte> listaTransporte = repo.findByIdtour(tourMovil);
            // Comprobar descuento es valido o no
            // Fecha de hoy en el servidor
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateObj = LocalDate.now();
            String fechaServidor = dateObj.format(formatter);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (TourHasTransporte tour : listaTransporte) {
                Date firstDate = sdf.parse(tourMovil.getFechaHoraSalida());
                Date secondDate = sdf.parse(fechaServidor);
                long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
                long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                if (diff >= Integer
                        .parseInt(tourMovil.getDiasAnticipado().isEmpty() ? "0" : tourMovil.getDiasAnticipado())
                        && !tourMovil.getDiasAnticipado().equals("0") && !tourMovil.getDescuento().isEmpty()
                        && !tourMovil.getDescuento().equals("0")) {
                    tour.setDiscountActivated(true);
                } else {
                    tour.setDiscountActivated(false);
                }
            }

            return listaTransporte;
        } catch (Exception e) {
            // TODO: handle exception
            System.err.println("Error al obtener los datos del tour: " + e.getMessage());
            throw e;
        }

    }
}
