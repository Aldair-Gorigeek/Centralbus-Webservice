package com.gorigeek.springboot.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gorigeek.springboot.Module;
import com.gorigeek.springboot.entity.VersionApp;
import com.gorigeek.springboot.service.VersionAppService;

@RestController
public class VersionAppController {
    
    @Autowired
    private VersionAppService service;
    @Module("CentralBus - Principal/Version App")
    @PostMapping("/versionApp")
    public ResponseEntity<?> selectVersionApp(@Param("id") String id) {
        try {
            List<VersionApp> datos = service.obtenerVersion(id);
            if (datos.size() > 0) {
                Map<String, List<VersionApp>> response = new HashMap<>();
                response.put("condicional", datos);
                return new ResponseEntity<Map<String, ?>>(response, HttpStatus.ACCEPTED);
            } else {
                Map<String, String> responseJson = new HashMap<>();
                responseJson.put("condicional", "Error Api");
                return new ResponseEntity<Map<String, ?>>(responseJson, HttpStatus.ACCEPTED);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("ERROR Controller: " + e.getMessage()));
        }
    }

}
