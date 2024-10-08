package com.gorigeek.springboot.controller;


import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.gorigeek.springboot.Module;
import com.gorigeek.springboot.entity.UserFinal;
import com.gorigeek.springboot.repository.RecuperarCuentaRepository;
import com.gorigeek.springboot.util.EmailService;


@RestController
public class EmailController {
    private EmailService emailService;
    @Autowired
    private RecuperarCuentaRepository recuperarRepository;

    @Autowired
    public void ControllerEmail(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/send")
    public String sendEmail() throws MessagingException {
        emailService.sendEmail("romeo.trujillo@gorigeek.com", "Sample Email", "Romeo Trujillo");
        return "email-sent";
    }
    
    /**Envío de correo para enviar codigo de verificación*/
    @Module("CentralBus - General/Enviar Codigo Verificacion")
    @GetMapping("/sendCode/{correo}/{codigo}")
    public String sendEmailCode(@PathVariable ("correo") String correo, @PathVariable ("codigo") String codigo) throws MessagingException {
        emailService.sendEmailCode(correo, "Código de Verificación - CentralBus", correo, codigo);
        return "email-sent";
    }
    
    /**Envío de correo para enviar codigo de verificación*/
    @Module("CentralBus - General/Enviar Codigo Verificacion")
    @GetMapping("/sendCodeConfirm/{correo}/{codigo}")
    public String sendEmailConfirm(@PathVariable ("correo") String correo, @PathVariable ("codigo") String codigo) throws MessagingException {
        UserFinal exist = this.recuperarRepository.findFirstByEmail(correo);
        exist.setCodigoVerificacion(codigo);
        this.recuperarRepository.save(exist);
        emailService.sendEmailConfirm(correo, "Código de Verificación - CentralBus", correo, codigo);
        return "email-sent";
    }

}
