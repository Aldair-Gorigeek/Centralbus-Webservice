package com.gorigeek.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import com.gorigeek.springboot.Module;
import com.gorigeek.springboot.entity.DestinosPopulares;
import com.gorigeek.springboot.repository.DestinosPopularesRepository;

@RestController
@RequestMapping("/api/destinos")
public class DestinosPopularesController {
    @Autowired
    private DestinosPopularesRepository destinosRepo;
    
    @Module("CentralBus - Comprar Boleto/Obtener Destinos Populares")
    @GetMapping(value = "/allPopulares", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DestinosPopulares>> getAllDestinosPopulares() {
        List<DestinosPopulares> destinosPopulares = destinosRepo.findAll();
        if (!destinosPopulares.isEmpty()) {            
            return ResponseEntity.ok().body(destinosPopulares);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }
}
