package com.gorigeek.springboot.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Base64;
import java.util.Date;

import javax.activation.DataSource;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.gorigeek.springboot.distribution.entity.VentasDistribution;
import com.gorigeek.springboot.entity.DetalleVentaMovil;
import com.gorigeek.springboot.entity.DetalleVentaTourMovil;
import com.gorigeek.springboot.entity.VentaTourMovil;
import com.gorigeek.springboot.entity.VentaViajesMovil;

@Service
public class EmailService {

    private JavaMailSender mailSender;
    private TemplateEngine templateEngine;

    @Autowired
    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendEmail(String to, String subject, String name) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        // MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);

        Context context = new Context();
        context.setVariable("name", name);
        String emailContent = templateEngine.process("test", context);

        helper.setText(emailContent, true);

        mailSender.send(message);
    }

    @Async
    public void sendEmailCode(String to, String subject, String correo, String codigo) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(message, true, "UTF-8");
            // MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);

            Context context = new Context();
            context.setVariable("correo", correo);
            context.setVariable("codigo", codigo);
            String emailContent = templateEngine.process("emailCode", context);

            helper.setText(emailContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            System.err.println("Ocurrio un error al enviar el código de verificación");
            e.printStackTrace();
        }
    }

    public void sendEmailConfirm(String to, String subject, String correo, String codigo) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        // imagenes-----------------------------------------------

        // -------------------------------------------------------

        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        // MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);

        Context context = new Context();
        context.setVariable("correo", correo);
        context.setVariable("codigo", codigo);
        String emailContent = templateEngine.process("emailConfim", context);

        helper.setText(emailContent, true);

        mailSender.send(message);
    }

    // correo de venta de boleto
    @Async
    public void sendEmailTicket(String to, String subject, String total, List<DetalleVentaMovil> listaDetalles,
            VentaViajesMovil viaje, String numTarjeta, String numAut, String idTrans, int tipoViaje) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime fechaHora = LocalDateTime.parse(listaDetalles.get(0).getFechaViaje(), formatter);
            String fechaViaje = fechaHora.format(DateTimeFormatter.ofPattern("dd MMM yyyy")).toUpperCase().replace(".",
                    "");
            String horaViaje = fechaHora.format(DateTimeFormatter.ofPattern("hh:mm a")).replace(".", "").trim();

            int cantAdulto = 0;
            int cantNino = 0;
            int cantInapam = 0;
            for (int i = 0; i < listaDetalles.size(); i++) {
                if (listaDetalles.get(i).getTipoBoleto().getIdc_tipoBoleto() == 1l) {
                    cantAdulto++;
                }
                if (listaDetalles.get(i).getTipoBoleto().getIdc_tipoBoleto() == 2l) {
                    cantNino++;
                }
                if (listaDetalles.get(i).getTipoBoleto().getIdc_tipoBoleto() == 3l) {
                    cantInapam++;
                }
            }

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            // MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);

            //
            double total1 = Double.parseDouble(total);
            double totalIva = total1 * 0.16;
            double totalSubtotal = total1 - totalIva;
            double costo = listaDetalles.get(0).getCosto();
            double costoIva = costo * 0.16;
            double costoSubtotal = costo - costoIva;
            //
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            decimalFormat.setDecimalSeparatorAlwaysShown(true);
            decimalFormat.setMinimumFractionDigits(2);
            String formattedNumber = decimalFormat.format(total1);
            String pattern = "dd/MM/yyyy";
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            String formattedDate = format.format(new Date());

            String titulo = "Viaje Sencillo";
            if (tipoViaje == 1) {
                titulo = "Viaje Redondo";
            }
            String fechayhora = fechaViaje + " - " + horaViaje;
            String totalmxn = decimalFormat.format(total1) + " MXN";
            Context context = new Context();
            context.setVariable("total", totalmxn);
            context.setVariable("titulo", titulo);
            context.setVariable("totalIva", decimalFormat.format(totalIva));
            context.setVariable("totalSubtotal", decimalFormat.format(totalSubtotal));
            context.setVariable("pasajero", listaDetalles.get(0).getNombrePasajero());
            context.setVariable("asiento", listaDetalles.get(0).getNumeroAsiento());
            context.setVariable("costo", decimalFormat.format(listaDetalles.get(0).getCosto()));
            context.setVariable("costoSubtotal", decimalFormat.format(costoSubtotal));
            context.setVariable("costoIva", decimalFormat.format(costoIva));
            context.setVariable("folio", listaDetalles.get(0).getFolio());
            context.setVariable("fecha", fechayhora);
            context.setVariable("nowfecha", formattedDate);
            context.setVariable("hora", horaViaje);
            context.setVariable("transaccion", listaDetalles.get(0).getId_detalle_venta());
            context.setVariable("tipoBoleto", listaDetalles.get(0).getTipoBoleto().getDescripcion());
            context.setVariable("adulto", cantAdulto);
            context.setVariable("nino", cantNino);
            context.setVariable("numTarjeta", numTarjeta);
            context.setVariable("numAut", numAut);
            context.setVariable("idTrans", idTrans);
            context.setVariable("inapam", cantInapam);
            context.setVariable("origen", viaje.getTerminalOrigen().getCiudad().getDescripcion() + ", "
                    + viaje.getTerminalOrigen().getEstados().getDescripcion());
            context.setVariable("destino", viaje.getTerminalDestino().getCiudad().getDescripcion() + ", "
                    + viaje.getTerminalDestino().getEstados().getDescripcion());

            context.setVariable("inapam", cantInapam);

            String emailContent = templateEngine.process("emailVentaV2", context);

            helper.setText(emailContent, true);

            // adjuntar pdf
            FileSystemResource pdfFile = new FileSystemResource("barcode_example.pdf");

            helper.addAttachment("Boleto(s).pdf", pdfFile);

            mailSender.send(message);
            System.out.println("Correo enviado");
        } catch (MessagingException e) {
            System.err.println("Ocurrió un error al enviar el boleto de CentralBus");
            e.printStackTrace();
        }
    }

    // correo de venta de boleto Distribution
    @Async
    public void sendEmailTicketDistribution(String to, String subject, String total, List<VentasDistribution> details,
            Integer totalPint, Integer totalPcil, Integer totalPypo, Integer totalPnos,
            Integer totalPsoe, Boolean isRedondo, String numTarjeta, String numAut, String idTrans)
            throws MessagingException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        String fechaHoraViajeDate = details.get(0).getFechaHoraViaje();
        LocalDateTime fechaHora = LocalDateTime.parse(fechaHoraViajeDate, formatter);
        String fechaViaje = fechaHora.format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault()))
                .toUpperCase().replace(".", "");
        String horaViaje = fechaHora.format(DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault()))
                .replace(".", "").trim();

        int cantInfante = totalPint;
        int cantNino = totalPcil;
        int cantJoven = totalPypo;
        int cantAdulto = totalPnos;
        int cantInapam = totalPsoe;

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        // MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);

        //
        double total1 = Double.parseDouble(total);
        double totalIva = total1 * 0.16;
        double totalSubtotal = total1 - totalIva;
        double costo = details.get(0).getPrecioBoleto();
        double costoIva = costo * 0.16;
        double costoSubtotal = costo - costoIva;
        //
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        decimalFormat.setDecimalSeparatorAlwaysShown(true);
        decimalFormat.setMinimumFractionDigits(2);
        String formattedNumber = decimalFormat.format(total1);
        String pattern = "dd/MM/yyyy";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String formattedDate = format.format(new Date());

        String totalmxn = decimalFormat.format(total1) + " MXN";
        Context context = new Context();
        context.setVariable("total", decimalFormat.format(totalmxn));
        context.setVariable("totalIva", decimalFormat.format(totalIva));
        context.setVariable("totalSubtotal", decimalFormat.format(totalSubtotal));
        context.setVariable("pasajero", details.get(0).getNombrePasajero());
        context.setVariable("asiento", details.get(0).getNumAsiento());
        context.setVariable("costo", decimalFormat.format(details.get(0).getPrecioBoleto()));
        context.setVariable("costoSubtotal", decimalFormat.format(costoSubtotal));
        context.setVariable("costoIva", decimalFormat.format(costoIva));
        context.setVariable("folio", details.get(0).getPrecioBoleto());
        context.setVariable("fecha", fechaViaje);
        context.setVariable("hora", horaViaje);
        context.setVariable("transaccion", details.get(0).getIdReservacion());
        if (isRedondo) {
            context.setVariable("tipoBoleto", "redondo");
        } else {
            context.setVariable("tipoBoleto", "sencillo");
        }
        context.setVariable("infante", cantInfante);
        context.setVariable("nino", cantNino);
        context.setVariable("joven", cantJoven);
        context.setVariable("adulto", cantAdulto);
        context.setVariable("inapam", cantInapam);
        context.setVariable("origen", details.get(0).getOrigen() + ", "
                + details.get(0).getOrigen());
        context.setVariable("destino", details.get(0).getDestino() + ", "
                + details.get(0).getOrigen());
        context.setVariable("numTarjeta", numTarjeta);
        context.setVariable("numAut", numAut);
        context.setVariable("idTrans", idTrans);
        String emailContent = templateEngine.process("emailVentaV2", context);

        helper.setText(emailContent, true);

        // adjuntar pdf
        FileSystemResource pdfFile = new FileSystemResource("barcode_example.pdf");
        helper.addAttachment("Boleto(s).pdf", pdfFile);

        mailSender.send(message);
        System.out.println("Correo enviado");
    }

    // correo de venta de boleto Tour ------------------
    /*
     * @Async
     * public void sendEmailTicketTour(String to, String subject, String total,
     * List<DetalleVentaTourMovil> listaDetalles,
     * VentaTourMovil viaje) throws MessagingException {
     * DateTimeFormatter formatter =
     * DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
     * LocalDateTime fechaHora =
     * LocalDateTime.parse(listaDetalles.get(0).getFechaViaje(), formatter);
     * String fechaViaje =
     * fechaHora.format(DateTimeFormatter.ofPattern("dd MMM yyyy")).toUpperCase().
     * replace(".", "");
     * String horaViaje =
     * fechaHora.format(DateTimeFormatter.ofPattern("hh:mm a")).replace(".",
     * "").trim();
     * 
     * int cantAdulto = 0;
     * int cantNino = 0;
     * int cantInapam = 0;
     * for (int i = 0; i < listaDetalles.size(); i++) {
     * if (listaDetalles.get(i).getTipoBoleto().getIdc_tipoBoleto() == 1l) {
     * cantAdulto++;
     * }
     * if (listaDetalles.get(i).getTipoBoleto().getIdc_tipoBoleto() == 2l) {
     * cantNino++;
     * }
     * if (listaDetalles.get(i).getTipoBoleto().getIdc_tipoBoleto() == 3l) {
     * cantInapam++;
     * }
     * }
     * 
     * MimeMessage message = mailSender.createMimeMessage();
     * 
     * MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
     * // MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
     * helper.setTo(to);
     * helper.setSubject(subject);
     * 
     * //
     * double total1 = Double.parseDouble(total);
     * double totalIva = total1 * 0.16;
     * double totalSubtotal = total1 - totalIva;
     * double costo = listaDetalles.get(0).getCosto();
     * double costoIva = costo * 0.16;
     * double costoSubtotal = costo - costoIva;
     * //
     * DecimalFormat decimalFormat = new DecimalFormat("#.##");
     * decimalFormat.setDecimalSeparatorAlwaysShown(true);
     * decimalFormat.setMinimumFractionDigits(2);
     * 
     * Context context = new Context();
     * context.setVariable("total", decimalFormat.format(total1));
     * context.setVariable("totalIva", decimalFormat.format(totalIva));
     * context.setVariable("totalSubtotal", decimalFormat.format(totalSubtotal));
     * context.setVariable("pasajero", listaDetalles.get(0).getNombrePasajero());
     * context.setVariable("asiento", listaDetalles.get(0).getNumeroAsiento());
     * context.setVariable("costo",
     * decimalFormat.format(listaDetalles.get(0).getCosto()));
     * context.setVariable("costoSubtotal", decimalFormat.format(costoSubtotal));
     * context.setVariable("costoIva", decimalFormat.format(costoIva));
     * context.setVariable("folio", listaDetalles.get(0).getFolio());
     * context.setVariable("fecha", fechaViaje);
     * context.setVariable("hora", horaViaje);
     * context.setVariable("transaccion",
     * listaDetalles.get(0).getId_detalle_venta_tour());
     * context.setVariable("tipoBoleto",
     * listaDetalles.get(0).getTipoBoleto().getDescripcion());
     * context.setVariable("adulto", cantAdulto);
     * context.setVariable("nino", cantNino);
     * context.setVariable("inapam", cantInapam);
     * context.setVariable("origen",
     * viaje.getTour().getCiudadesOrigen().getDescripcion() + ", "
     * + viaje.getTour().getEstadoOrigen().getDescripcion());
     * context.setVariable("destino",
     * viaje.getTour().getCiudadesDestino().getDescripcion() + ", "
     * + viaje.getTour().getEstadoDestino().getDescripcion());
     * 
     * context.setVariable("inapam", cantInapam);
     * 
     * String emailContent = templateEngine.process("emailVenta", context);
     * 
     * helper.setText(emailContent, true);
     * 
     * // adjuntar pdf
     * FileSystemResource pdfFile = new FileSystemResource("barcode_example.pdf");
     * helper.addAttachment("Ticket.pdf", pdfFile);
     * 
     * mailSender.send(message);
     * System.out.println("Correo enviado");
     * }
     */
    // nueva version de diseño de correo tour
    @Async
    public void sendEmailTicketTour(String to, String subject, String total, List<DetalleVentaTourMovil> listaDetalles,
            VentaTourMovil viaje, String numTarjeta, String numAut, String idTrans) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime fechaHora = LocalDateTime.parse(listaDetalles.get(0).getFechaViaje(), formatter);
            String fechaViaje = fechaHora.format(DateTimeFormatter.ofPattern("dd MMM yyyy")).toUpperCase().replace(".",
                    "");
            String horaViaje = fechaHora.format(DateTimeFormatter.ofPattern("hh:mm a")).replace(".", "").trim();

            int cantAdulto = 0;
            int cantNino = 0;
            int cantInapam = 0;
            for (int i = 0; i < listaDetalles.size(); i++) {
                if (listaDetalles.get(i).getTipoBoleto().getIdc_tipoBoleto() == 1l) {
                    cantAdulto++;
                }
                if (listaDetalles.get(i).getTipoBoleto().getIdc_tipoBoleto() == 2l) {
                    cantNino++;
                }
                if (listaDetalles.get(i).getTipoBoleto().getIdc_tipoBoleto() == 3l) {
                    cantInapam++;
                }
            }

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            // MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);

            //
            double total1 = Double.parseDouble(total);
            double totalIva = total1 * 0.16;
            double totalSubtotal = total1 - totalIva;
            double costo = listaDetalles.get(0).getCosto();
            double costoIva = costo * 0.16;
            double costoSubtotal = costo - costoIva;
            //
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            decimalFormat.setDecimalSeparatorAlwaysShown(true);
            decimalFormat.setMinimumFractionDigits(2);
            String formattedNumber = decimalFormat.format(total1);
            String pattern = "dd/MM/yyyy";
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            String formattedDate = format.format(new Date());

            String titulo = "Viaje Redondo";
            // if(tipoViaje==1) {
            // titulo = "Viaje Redondo";
            // }
            String fechayhora = fechaViaje + " - " + horaViaje;
            String totalmxn = decimalFormat.format(total1) + " MXN";
            Context context = new Context();
            context.setVariable("total", totalmxn);
            context.setVariable("titulo", titulo);
            context.setVariable("totalIva", decimalFormat.format(totalIva));
            context.setVariable("totalSubtotal", decimalFormat.format(totalSubtotal));
            context.setVariable("pasajero", listaDetalles.get(0).getNombrePasajero());
            context.setVariable("asiento", listaDetalles.get(0).getNumeroAsiento());
            context.setVariable("costo", decimalFormat.format(listaDetalles.get(0).getCosto()));
            context.setVariable("costoSubtotal", decimalFormat.format(costoSubtotal));
            context.setVariable("costoIva", decimalFormat.format(costoIva));
            context.setVariable("folio", listaDetalles.get(0).getFolio());
            context.setVariable("fecha", fechayhora);
            context.setVariable("nowfecha", formattedDate);
            context.setVariable("hora", horaViaje);
            // context.setVariable("transaccion",
            // listaDetalles.get(0).getId_detalle_venta());
            context.setVariable("transaccion", listaDetalles.get(0).getId_detalle_venta_tour());
            context.setVariable("tipoBoleto", listaDetalles.get(0).getTipoBoleto().getDescripcion());
            context.setVariable("adulto", cantAdulto);
            context.setVariable("nino", cantNino);
            context.setVariable("numTarjeta", numTarjeta);
            context.setVariable("numAut", numAut);
            context.setVariable("idTrans", idTrans);
            context.setVariable("inapam", cantInapam);
            context.setVariable("origen", viaje.getTour().getCiudadesOrigen().getDescripcion() + ", "
                    + viaje.getTour().getEstadoOrigen().getDescripcion());
            // context.setVariable("origen",
            // viaje.getTerminalOrigen().getCiudad().getDescripcion() + ", "
            // + viaje.getTerminalOrigen().getEstados().getDescripcion());
            context.setVariable("destino", viaje.getTour().getCiudadesDestino().getDescripcion() + ", "
                    + viaje.getTour().getEstadoDestino().getDescripcion());
            // context.setVariable("destino",
            // viaje.getTerminalDestino().getCiudad().getDescripcion() + ", "
            // + viaje.getTerminalDestino().getEstados().getDescripcion());

            context.setVariable("inapam", cantInapam);

            String emailContent = templateEngine.process("emailVentaV2", context);

            helper.setText(emailContent, true);

            // adjuntar pdf
            FileSystemResource pdfFile = new FileSystemResource("barcode_example.pdf");
            helper.addAttachment("Boleto(s).pdf", pdfFile);
            mailSender.send(message);
            System.out.println("Correo enviado");
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // reenviar PDF de Distribution ------------------
    @Async
    public void resendEmailTicketDistribution(String to, String subject, String total, String base64Pdf,
            List<VentasDistribution> ventas, String numTarjeta, String numAut, String idTrans, Boolean isRedondo) {
        try {
            // Obtener fecha y hora del viaje de ida
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime fechaHora = LocalDateTime.parse(ventas.get(0).getFechaHoraViaje(), formatter);
            String fechaViaje = fechaHora.format(DateTimeFormatter.ofPattern("dd MMM yyyy")).toUpperCase().replace(".",
                    "");
            String horaViaje = fechaHora.format(DateTimeFormatter.ofPattern("hh:mm a")).replace(".", "").trim();

            // Fecha de operacion
            String pattern = "dd/MM/yyyy";
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            String formattedDate = format.format(new Date());
            MimeMessage message = mailSender.createMimeMessage();
            // Fin fecha operacion

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);

            helper.setSubject(subject);

            double total1 = Double.parseDouble(total);

            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            decimalFormat.setDecimalSeparatorAlwaysShown(true);
            decimalFormat.setMinimumFractionDigits(2);
            String totalmxn = decimalFormat.format(total1) + " MXN";

            Context context = new Context();
            context.setVariable("fecha", fechaViaje);
            context.setVariable("hora", horaViaje);
            context.setVariable("origen", ventas.get(0).getOrigen());
            context.setVariable("destino", ventas.get(0).getDestino());

            context.setVariable("idTrans", idTrans);
            context.setVariable("total", totalmxn);
            context.setVariable("numAut", numAut);
            context.setVariable("nowfecha", formattedDate);
            context.setVariable("numTarjeta", numTarjeta);
            if (isRedondo) {
                context.setVariable("tipoViaje", "Viaje Redondo");
            } else {
                context.setVariable("tipoViaje", "Viaje Sencillo");
            }

            String emailContent = templateEngine.process("emailVentaDistribution", context);

            helper.setText(emailContent, true);

            // adjuntar pdf
            byte[] pdfBytes = Base64.getDecoder().decode(base64Pdf);
            DataSource dataSource = new ByteArrayDataSource(pdfBytes, "application/pdf");
            helper.addAttachment("Boleto(s).pdf", dataSource);

            mailSender.send(message);
            System.out.println("Correo enviado");
        } catch (MessagingException e) {
            System.err.println("Ocurrió un error al enviar el boleto de distribution");
            e.printStackTrace();
        }
    }

    /** Correo para enviar recibo de paynet */
    @Async
    public void sendEmailReciboPaynet(String to, String subject, String url) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        // MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);

        Context context = new Context();
        // context.setVariable("correo", correo);
        // context.setVariable("codigo", url);
        context.setVariable("urlRecibo", url);
        String emailContent = templateEngine.process("emailPaynet", context);

        helper.setText(emailContent, true);
        System.out.println("Enviando correo Paynet...");
        mailSender.send(message);
    }

}
