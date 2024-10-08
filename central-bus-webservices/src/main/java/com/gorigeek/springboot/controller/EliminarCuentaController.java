package com.gorigeek.springboot.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gorigeek.springboot.service.EliminarCuentaService;

@RestController
public class EliminarCuentaController {

    @Autowired
    private EliminarCuentaService service;

    @PostMapping("/deleteAccount")
    public Map<String, String> updateContrasena(@Param("idUser") String idUser) {
        String json = "";
        try {
            int response = service.updateEstatusCuenta(Long.parseLong(idUser));
            if (response == 1) {
                json = "true";
            } else {
                json = "Error Api";
            }
        } catch (Exception e) {
            json = "Error Api";
            System.err.println(e.getMessage());
        }
        Map<String, String> responseJson = new HashMap<>();
        responseJson.clear();
        responseJson.put("condicional", json);
        return responseJson;
    }

}
