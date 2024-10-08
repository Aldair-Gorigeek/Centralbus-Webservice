package com.gorigeek.springboot.distribution.controller;

import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gorigeek.springboot.Module;
import com.gorigeek.springboot.distribution.entity.VentasDistribution;
import com.gorigeek.springboot.distribution.service.VentasDistributionService;
import com.gorigeek.springboot.entity.DTO.DetallesTicketDTO;

@RestController
@RequestMapping("/api/distribution")
public class DistributionTicketsController {    
    @Autowired
    private VentasDistributionService ventasService;
    
    @Module("Distribution - Mis Boletos/Todos los Boletos")
    @GetMapping("/tickets/{idUsuario}")
    public ResponseEntity<?> getAllTicketsDistribution(@PathVariable(value = "idUsuario") Long idUsuario) {        
        List<VentasDistribution> tickets = this.ventasService.getAllTickets(idUsuario);
        for(VentasDistribution ticket: tickets) {
            ticket.setFechaViaje(this.ventasService.getFechaDetalle(ticket.getFechaHoraViaje()));
            ticket.setHoraViaje(this.ventasService.getHoraDetalle(ticket.getFechaHoraViaje()));
        }
        if (tickets == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(tickets);
        }        
        return ResponseEntity.status(HttpStatus.OK).body(tickets);

    }

    @Module("Distribution - Mis Boletos/Detalle")
    @GetMapping("/tickets/detail/{idVenta}")
    public ResponseEntity<?> getDetailsTicket(@PathVariable(value = "idVenta") Long idVenta) {        
        DetallesTicketDTO ticket = this.ventasService.getDetails(idVenta);        
        if (ticket == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ticket);
        }                
        return ResponseEntity.status(HttpStatus.OK).body(ticket);

    }

    @Module("Distribution - Mis Boletos/Eliminar")
    @PutMapping("/tickets/updateStatus/{idVenta}")
    public ResponseEntity<String> updateStatusTicket(@PathVariable(value = "idVenta") Long idDetalle) {
        Optional<VentasDistribution> ventaOptional = ventasService.findById(idDetalle);

        if (ventaOptional.isPresent()) {
            VentasDistribution venta = ventaOptional.get();
            venta.setEliminado(1);
            ventasService.save(venta);
            return ResponseEntity.ok("ok");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Venta no encontrada con ID: " + idDetalle);
        }
    }
    
    @Module("Distribution - Mis Boletos/Descargar")
    @GetMapping("/tickets/resend/{idVenta}")
    public ResponseEntity<?> resendDistributionTicket(@PathVariable(value = "idVenta") Long idVenta) throws MessagingException {
        String ticket = this.ventasService.resendTicket(idVenta);
        if (ticket == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ticket);
        }
        return ResponseEntity.status(HttpStatus.OK).body(ticket);

    }
}
