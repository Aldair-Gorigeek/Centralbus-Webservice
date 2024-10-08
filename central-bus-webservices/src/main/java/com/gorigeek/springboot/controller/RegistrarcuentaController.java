package com.gorigeek.springboot.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.gorigeek.springboot.Module;
import com.gorigeek.springboot.entity.Afiliado;
import com.gorigeek.springboot.entity.User;
import com.gorigeek.springboot.entity.UserFinal;
import com.gorigeek.springboot.repository.RegistrarCuentaRepository;
import com.gorigeek.springboot.repository.jdbc.UserJdbcRepository;

@RestController
@RequestMapping("/api/register")
public class RegistrarcuentaController {
    @Autowired
    private RegistrarCuentaRepository register;

    private final UserJdbcRepository userRepositoryJDBC;

    public RegistrarcuentaController(UserJdbcRepository repositoryJdbc) {
        this.userRepositoryJDBC = repositoryJdbc;
    }

    @Module("CentralBus - Registro/Guardar Datos 0_0")
    @PostMapping("/registro")
    public String createUser(@RequestBody UserFinal user) {
        // Verificar disponibilidad y estado de cuenta en caso de estar registrada
        UserFinal UserFromCB = this.userRepositoryJDBC.findFirstByEmail(user.getEmail());
        Boolean ValiEmail = UserFromCB != null ? false : true;
        Boolean ValiActive = (UserFromCB != null && UserFromCB.getEstatusActivo() == 0) ? false : true;

        if (ValiEmail == false) {
            if (ValiActive)
                return "userOcupado";
            else
                return "userDesactivado";
        } else {
            if (ValiEmail == false) {
                if (ValiActive == false) {
                    return "userOcupado";
                } else {
                    return "userDesactivado";
                }
            } else {
                UserFinal userFinal = new UserFinal();
                userFinal.setEmail(user.getEmail());
                userFinal.setPass(BCrypt.hashpw(user.getPass(), BCrypt.gensalt(10)));
                LocalDate todaysDate = LocalDate.now();
                userFinal.setFechaRegistro(todaysDate.toString());
                userFinal.setEstatusActivo(0);
                userFinal.setNombre(user.getNombre());
                userFinal.setTelefono(user.getTelefono());
                UserFinal reg = this.register.save(userFinal);
                if (reg == null) {
                    System.err.println("REG NULL");
                    return "fail";
                }

                return reg.getIdtUsuariosfinal().toString();
            }

        }

    }
}
