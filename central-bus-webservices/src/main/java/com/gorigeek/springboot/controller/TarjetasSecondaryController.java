package com.gorigeek.springboot.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gorigeek.springboot.entity.DTO.Tarjetas_secondaryDTO;
import com.gorigeek.springboot.repository.TarjetasSecondaryDAO;

@RestController
public class TarjetasSecondaryController {

    
    
    @PostMapping("/tarjetasDos")
    public List<?> tarjetas(String id_principal) {

        List<Tarjetas_secondaryDTO> json = new ArrayList();
        
        TarjetasSecondaryDAO tarjetas= new TarjetasSecondaryDAO();
        
        try {
            return json= tarjetas.tarjetas(id_principal);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return json;
        
    }
    
    @GetMapping("/tarjetasDos/{id_principal}")
    public List<Tarjetas_secondaryDTO> obtenerTarjetas(@PathVariable String id_principal) {
        TarjetasSecondaryDAO tarjetasDAO = new TarjetasSecondaryDAO();

        try {
            return tarjetasDAO.tarjetas(id_principal);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    
    @PostMapping("/guardarTarjeta")
    public List<Tarjetas_secondaryDTO> guardarTarjeta(@RequestParam String id_principal, @RequestParam String key_tarjeta) {
        TarjetasSecondaryDAO tarjetasDAO = new TarjetasSecondaryDAO();

        try {
            // método para guardar la tarjeta
            tarjetasDAO.guardar(id_principal, key_tarjeta);

            
            return tarjetasDAO.tarjetas(id_principal);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    
}
