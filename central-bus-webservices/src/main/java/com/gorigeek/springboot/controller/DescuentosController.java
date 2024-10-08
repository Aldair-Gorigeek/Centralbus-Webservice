package com.gorigeek.springboot.controller;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gorigeek.springboot.Module;
import com.gorigeek.springboot.entity.TBoletosDescuentos;
import com.gorigeek.springboot.repository.TBoletosDescuentosService;

@RestController
@RequestMapping("/api/discount")
public class DescuentosController {
    @Autowired
    private TBoletosDescuentosService boletosRepository;

    @Transactional
    @Module("CentralBus - Comprar Boleto/Reserva Promo99")
    @PostMapping("/reservation/promo99")
    public ResponseEntity<String> saveNew(@RequestBody TBoletosDescuentos body) {
//        System.err.println("Quantity to register " + body.getQuantityToRegister());
        List<TBoletosDescuentos> registered = boletosRepository.findByLineaOrigenDestinoAndFechaSalidaPessimistic(body.getLinea(),
                body.getOrigen(), body.getDestino(), body.getFechaSalida());
//        System.err.println("Registered size: " + registered.size());
        LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);
        Date tenMinutesAgoDate = Date.from(tenMinutesAgo.atZone(ZoneId.systemDefault()).toInstant());
        // Comprobar que aun sigan disponibles los lugares con descuento
        int quantitySeatsDiscountUnavailable = 0;
        for (TBoletosDescuentos boleto : registered) {
            // Excluir los que van a ser borrados por deleteOldRecords()
            if (boleto.getEstatus() == 2) {
                quantitySeatsDiscountUnavailable += 1;
            } else if (boleto.getFechaCreacion().after(tenMinutesAgoDate) && boleto.getEstatus() == 1) {
                quantitySeatsDiscountUnavailable += 1;
            }
        }
//        System.err.println("Seats with discount available: " + (2 - quantitySeatsDiscountUnavailable) + ", " +  body.getQuantityToRegister());
        if ((2 - quantitySeatsDiscountUnavailable) >= body.getQuantityToRegister() && body.getQuantityToRegister() > 0) {
            for (int i = 0; i < body.getQuantityToRegister(); i++) {
                LocalDateTime localDateTime = LocalDateTime.now();
                Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()); 
                TBoletosDescuentos temp = new TBoletosDescuentos();
                temp.setLinea(body.getLinea());
                temp.setOrigen(body.getOrigen());
                temp.setDestino(body.getDestino());
                temp.setFechaSalida(body.getFechaSalida());
                temp.setFechaCreacion(date);
                temp.setEstatus(1);

                boletosRepository.save(temp);
            }
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
//            System.err.println("Ya no hay lugares");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }
        
    @Module("CentralBus - Comprar Boleto/Eliminar Reserva Promo99")
    @PutMapping("/reservation/delete/promo99")
    public ResponseEntity<String> deleteLastOnes(@RequestBody TBoletosDescuentos body) {
        List<TBoletosDescuentos> registered = boletosRepository.findByLineaOrigenDestinoAndFechaSalida(body.getLinea(),
                body.getOrigen(), body.getDestino(), body.getFechaSalida());
        //Ordenar por Id, el primero es el mas viejo
        int eliminated = 0;
        registered.sort(Comparator.comparing(TBoletosDescuentos::getId));
        
//        System.err.println("Registered size: " + registered.size());
        LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);
        Date tenMinutesAgoDate = Date.from(tenMinutesAgo.atZone(ZoneId.systemDefault()).toInstant());
        // Comprobar que aun sigan disponibles los lugares con descuento        
        for (TBoletosDescuentos boleto : registered) {
            // Excluir los que van a ser borrados por deleteOldRecords()
            if (boleto.getEstatus() == 2) {                
            } else if (boleto.getFechaCreacion().after(tenMinutesAgoDate) && boleto.getEstatus() == 1) {
                if(eliminated <= body.getQuantityToDelete());
                boletosRepository.deleteById(boleto.getId());
                eliminated +=1;
                
            }
        }
        
        return null;
        
    }
    
       
    @Module("CentralBus - Comprar Boleto/Eliminar Reserva Promo99")
    @PutMapping("/reservation/update/promo99")
    public ResponseEntity<String> updateLastOnes(@RequestBody TBoletosDescuentos body) {
        List<TBoletosDescuentos> registered = boletosRepository.findByLineaOrigenDestinoAndFechaSalida(body.getLinea(),
                body.getOrigen(), body.getDestino(), body.getFechaSalida());
        //Ordenar por Id, el primero es el mas viejo
        int updated = 0;
        registered.sort(Comparator.comparing(TBoletosDescuentos::getId));
        
//        System.err.println("Registered size: " + registered.size());
        LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);
        Date tenMinutesAgoDate = Date.from(tenMinutesAgo.atZone(ZoneId.systemDefault()).toInstant());
        // Confirmar los que tengan el estatus 1, pasar a 2        
        for (TBoletosDescuentos boleto : registered) {
            if (boleto.getEstatus() == 2) {                
            } else if (boleto.getFechaCreacion().after(tenMinutesAgoDate) && boleto.getEstatus() == 1) {
                if(updated <= body.getQuantityToUpdate());                
                boletosRepository.updateEstatusToTwo(boleto.getId());
                updated +=1;
                
            }
        }
        
        return null;
        
    }
}
