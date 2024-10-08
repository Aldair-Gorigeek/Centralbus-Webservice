package com.gorigeek.springboot.controller;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gorigeek.springboot.Module;
import com.gorigeek.springboot.entity.UpdateCodigoInivtarAmigo;
import com.gorigeek.springboot.service.InvitarAmigoService;

@RestController
public class InvitarAmigoController {
    @Autowired
    private InvitarAmigoService service;

    @Module("CentralBus - Perfil/Generar Codigo")
    @PostMapping("/generarCodigo")
    public Map<String, String> insertCodigo(@Param("idUser") String idUser) {
        String json = "";
        SecureRandom codigoRandom;
        Integer codigo = 0;
        boolean bandera = true;        
        try {
            List<UpdateCodigoInivtarAmigo> datosUsuario = service.buscarCodigoUsuario(idUser);            
            if (datosUsuario.size() > 0) {                
                if (datosUsuario.get(0).getCodigo_amigo() != null &&
                        datosUsuario.get(0).getCodigo_amigo() != "" &&
                        datosUsuario.get(0).getCodigo_amigo().length()>0) {
                    json = datosUsuario.get(0).getCodigo_amigo();

                } else {
                    while (bandera) {
                        codigoRandom = SecureRandom.getInstance("SHA1PRNG", "SUN");
                        codigo = codigoRandom.nextInt(999999);
                        List<UpdateCodigoInivtarAmigo> datos = service.buscarCodigo(codigo + "");
                        if (datos.size() == 0) {
                            bandera = false;
                            break;
                        }
                    }
                    if (bandera == false) {

                        int update = service.updateCodigo(codigo + "", idUser);
                        if (update != 0) {
                            json = codigo + "";
                        } else {
                            json = "false";
                        }
                    }

                }
            } else {
                json = "Usuario Invalido";
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

    @Module("CentralBus - Registro/Verificar Codigo Amigo")
    @PostMapping("/verificarCodigo")
    public Map<String, String> verificarCodigo(@Param("codigo") String codigo) {
        String json = "";

        try {

            List<UpdateCodigoInivtarAmigo> datos = service.buscarCodigo(codigo);
            System.err.println(datos.get(0).getCodigo_amigo());
            if (datos.size() > 0) {
                json = "true";
            } else {
                json = "false";
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

    @Module("CentralBus - Registro/Registrar Referido")
    @PostMapping("/registrarReferidos")
    public Map<String, String> insertReferidos(@Param("idUserInvitado") String idUserInvitado,
            @Param("codigo") String codigo) {
        String json = "";
        Date datetime = new Date();
        String fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(datetime);

        try {
            List<UpdateCodigoInivtarAmigo> datos = service.buscarCodigo(codigo);
            if (datos.size() > 0) {
                String idUserReferido = datos.get(0).getIdt_usuarios_final();

                int insert = service.insertFolio(Integer.parseInt(idUserReferido), Integer.parseInt(idUserInvitado),
                        fecha);
                if (insert == 1) {
                    json = "true";
                } else {
                    json = "Error Api";
                }

            } else {
                json = "EL codigo no existe";
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
