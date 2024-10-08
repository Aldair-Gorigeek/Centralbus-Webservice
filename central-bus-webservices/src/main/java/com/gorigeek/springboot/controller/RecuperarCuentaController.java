package com.gorigeek.springboot.controller;

import java.io.IOException;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gorigeek.springboot.Module;
import com.gorigeek.springboot.entity.UserFinal;
import com.gorigeek.springboot.repository.RecuperarCuentaRepository;
import com.gorigeek.springboot.repository.jdbc.UserJdbcRepository;
import com.gorigeek.springboot.util.EmailService;

@RestController
@RequestMapping("/api/recuperarPass")
public class RecuperarCuentaController {
    @Autowired
    private RecuperarCuentaRepository recuperarRepository;
    private final UserJdbcRepository userRepositoryJDBC;

    private EmailService emailService;

    @Autowired
    public void ControllerEmail(EmailService emailService) {
        this.emailService = emailService;
    }

    public RecuperarCuentaController(UserJdbcRepository userRepositoryJDBC) {
        this.userRepositoryJDBC = userRepositoryJDBC;
    }

    /** Corroborar que exista el correo */
    @Module("CentralBus - General/Comprobar Correo y Enviar Codigo")
    @GetMapping("email/{correo}/{codigo}")
    public String existUserByEmail(@PathVariable("correo") String correo, @PathVariable("codigo") String codigo)
            throws IOException {
        UserFinal exist = this.userRepositoryJDBC.findFirstByEmail(correo);
        if (exist == null) {
            return "null-User";
        } else {
            exist.setCodigoVerificacion(codigo);
            this.userRepositoryJDBC.updateCodigoVerificacion(exist);
            emailService.sendEmailCode(correo, "Código de Verificación - CentralBus", correo, codigo);

        }

        if (exist.getEstatusActivo() != 1) {
            if (exist.getEstatusActivo() == 0) {
                return "registro";
            } else {
                return "invalid";
            }

        }
        return "exist";
    }

    /** corroborar si coincide el codigo */
    @Module("CentralBus - General/Verificar Codigo")
    @GetMapping("codigoVerificacion/{correo}/{codigo}")
    public String verifyCode(@PathVariable("correo") String correo, @PathVariable("codigo") String codigo)
            throws IOException {
        UserFinal exist = this.recuperarRepository.findFirstByEmail(correo);
        if (exist == null) {
            return "null-User";
        } else if (exist.getCodigoVerificacion().equals(codigo)) {
            return "match";
        } else {
            return "fail";
        }
    }

    /** corroborar si coincide el codigo */
    @Module("CentralBus - Registro/Confirmar Correo")
    @GetMapping("confirmAccount/{correo}/{codigo}")
    public String confirmAccount(@PathVariable("correo") String correo, @PathVariable("codigo") String codigo)
            throws IOException {
        UserFinal exist = this.userRepositoryJDBC.findFirstByEmail(correo);
        if (exist == null) {
            return "null-User";
        } else if (exist.getCodigoVerificacion().equals(codigo)) {
            exist.setEstatusActivo(1);
            this.userRepositoryJDBC.updateStatusActivo(exist);
            return "match";
        } else {
            return "fail";
        }
    }

    /** Actualizar contraseñas de usuario */
    @Module("CentralBus - General/Actualizar Password")
    @PutMapping("/{correo}")
    public ResponseEntity<String> updateUser(@RequestBody UserFinal userFinal, @PathVariable("correo") String correo) {
        UserFinal existingUserFinal = this.userRepositoryJDBC.findFirstByEmail(correo);
        if (BCrypt.checkpw(userFinal.getPass(), existingUserFinal.getPass())) {
            // Contraseña nueva y anterior son las mismas
            return ResponseEntity.status(HttpStatus.CONFLICT).body("coinciden");
        } else {
            existingUserFinal.setPass(BCrypt.hashpw(userFinal.getPass(), BCrypt.gensalt(10)));
            boolean updatePass = this.userRepositoryJDBC.updatePassword(existingUserFinal);
            if (updatePass) {
                return ResponseEntity.ok("ok");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Password update failed");
            }

        }

    }

}
