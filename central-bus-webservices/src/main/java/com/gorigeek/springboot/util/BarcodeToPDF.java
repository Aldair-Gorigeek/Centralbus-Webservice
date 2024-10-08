package com.gorigeek.springboot.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Writer;
import com.google.zxing.common.BitMatrix;
import com.gorigeek.springboot.entity.Autobus;
import com.gorigeek.springboot.entity.DetalleVentaMovil;
import com.gorigeek.springboot.entity.DetalleVentaTourMovil;
import com.gorigeek.springboot.entity.VentaTourMovil;
import com.gorigeek.springboot.entity.VentaViajesMovil;
import com.gorigeek.springboot.repository.AutobusRepository;
import com.itextpdf.text.BaseColor;
//import com.google.zxing.client.j2se.MatrixToImageWriter;
//import com.google.zxing.client.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.DeviceNColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImage;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.codec.Base64;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.qrcode.QRCode;
import com.itextpdf.text.pdf.qrcode.QRCodeWriter;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.RectangleReadOnly;
import com.itextpdf.text.io.*;

import java.awt.Color;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.border.Border;

import org.hibernate.cfg.beanvalidation.ActivationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

public class BarcodeToPDF {
    // boleto sencillo
    @Async
    public boolean boletoPDF(VentaViajesMovil ventaViaje, List<DetalleVentaMovil> detalles, double ivaReal,
            AutobusRepository repoAutobus) {
        try {
            // Crear un objeto Document
            Document document = new Document();

            // Especificar la ubicación del archivo PDF
            String pdfFilePath = "barcode_example.pdf";
            PdfWriter archivo = PdfWriter.getInstance(document, new FileOutputStream(pdfFilePath));

            // Abrir el documento
            document.open();

            // ---------------variables generales---------------------------
            // imagen de fondo
            Image img = Image.getInstance("src/main/resources/templates/plantilla-viaje-sencillo.png");
            // img.scalePercent(47);
            img.scalePercent(23.5f);
            img.setAbsolutePosition(0, 0);
            // img.setAbsolutePosition(40, 150);

            // estilos de letra
            BaseFont baseFont = BaseFont.createFont();
            BaseColor colorBlueCustom = new BaseColor(3, 66, 140);
            // BaseColor colorBlueCustom = new BaseColor(37, 125, 192);
            BaseColor colorGrayCustom = new BaseColor(100, 100, 100);
            Font fontBlue = new Font(baseFont, 12, Font.BOLD, colorBlueCustom);
            Font fontBlueLight = new Font(baseFont, 10, Font.NORMAL, colorBlueCustom);
            Font fontBlueBig = new Font(baseFont, 30, Font.BOLD, colorBlueCustom);
            Font fontBlueBig2 = new Font(baseFont, 40, Font.BOLD, colorBlueCustom);
            Font fontGray = new Font(baseFont, 12, Font.NORMAL, colorGrayCustom);

            // fechas de viaje
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime fechaHora = LocalDateTime.parse(detalles.get(0).getFechaViaje(), formatter);
            String fechaViaje = fechaHora.format(DateTimeFormatter.ofPattern("dd MMM yyyy")).toUpperCase().replace(".",
                    "");
            String horaViaje = fechaHora.format(DateTimeFormatter.ofPattern("hh:mm a")).replace(".", "").trim();

            // rellenar los boletos
            for (int i = 0; i < detalles.size(); i++) {
                double ivaD = ivaReal;
                double totalD = detalles.get(i).getCosto();
                double ivaTotal = totalD * ivaD;
                double subtotalD = totalD - ivaTotal;
                boolean isDoubleDecker = false;
                Optional<Autobus> autobusIda = repoAutobus.findById(Math.round(detalles.get(i).getAutobus()));

                if (autobusIda.isPresent()) {
                    Autobus autobus = autobusIda.get();
                    if (Arrays.asList(7L, 8L, 9L).contains(autobus.getTipoTransporte().getIdTipoTransporte())) {
                        isDoubleDecker = true;
                    } else {
                        isDoubleDecker = false;
                    }
                } else {
                    isDoubleDecker = false;
                }
                // -----------------------------------
                // formateo de numeros
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                decimalFormat.setDecimalSeparatorAlwaysShown(true);
                decimalFormat.setMinimumFractionDigits(2);

                // imagen de fondo boleto---------------------
                document.add(img);

                // cuadro de pasajero -----------------
                Paragraph nombre = new Paragraph(detalles.get(i).getNombrePasajero(), fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, nombre, 30, 700, 0);
                Paragraph origenp = new Paragraph(ventaViaje.getTerminalOrigen().getCiudad().getDescripcion() + ", "
                        + ventaViaje.getTerminalOrigen().getEstados().getDescripcion(), fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, origenp, 30, 660, 0);
                Paragraph origenDireccion = new Paragraph("(" + ventaViaje.getTerminalOrigen().getDireccion() + ")",
                        fontBlueLight);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, origenDireccion, 30, 648, 0);
                Paragraph destinop = new Paragraph(ventaViaje.getTerminalDestino().getCiudad().getDescripcion() + ", "
                        + ventaViaje.getTerminalDestino().getEstados().getDescripcion(), fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, destinop, 30, 620, 0);
                Paragraph destinoDireccion = new Paragraph("(" + ventaViaje.getTerminalDestino().getDireccion() + ")",
                        fontBlueLight);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, destinoDireccion, 30, 608,
                        0);

                Paragraph nAsientop = new Paragraph(
                        (detalles.get(i).getNumeroAsiento() == null || detalles.get(i).getNumeroAsiento() == 0) ? "S/N"
                                : detalles.get(i).getNumeroAsiento().toString(),
                        fontBlueBig2);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_CENTER, nAsientop, 70, 540, 0);
                if (isDoubleDecker) {
                    String planta = "Planta baja";
                    if (detalles.get(i).getTipoPlanta() == null || detalles.get(i).getTipoPlanta() == 1) {
                        planta = "Planta baja";
                    } else {
                        planta = "Planta alta";
                    }
                    Paragraph tipoPlanta = new Paragraph(planta, fontBlueLight);
                    ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_CENTER, tipoPlanta, 70, 530,
                            0);
                }

                Paragraph tipoBoleto = new Paragraph(detalles.get(i).getTipoBoleto().getDescripcion(), fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_CENTER, tipoBoleto, 70, 519, 0);

                // seccion fecha hora
                Paragraph fechap = new Paragraph(fechaViaje, fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, fechap, 140, 560, 0);
                Paragraph horap = new Paragraph(horaViaje, fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, horap, 140, 520, 0);

                // cuadro precios
                Paragraph subtotald = new Paragraph("$" + decimalFormat.format(subtotalD) + " MXN", fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, subtotald, 450, 590, 0);

                Paragraph ivad = new Paragraph("$" + decimalFormat.format(ivaTotal) + " MXN", fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, ivad, 450, 575, 0);

                Paragraph totald = new Paragraph("$" + decimalFormat.format(detalles.get(i).getCosto()) + " MXN",
                        fontBlueBig);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_CENTER, totald, 440, 520, 0);

                // apartado folio-transaccion
                Paragraph nTransaccionp = new Paragraph(detalles.get(i).getId_detalle_venta() + "", fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, nTransaccionp, 350, 620, 0);

                Paragraph nFoliop = new Paragraph(detalles.get(i).getFolio(), fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, nFoliop, 450, 620, 0);

                // Generar el código qr
                BarcodeQRCode qrDato = new BarcodeQRCode(detalles.get(i).getFolio(), 70, 70, null);
                Image imagenQr = qrDato.getImage();

                imagenQr.setAbsolutePosition(250, 530);
                document.add(imagenQr);

                // codigo para agregar logotipo afiliado------------------
                String logo = detalles.get(i).getVentaViajes().getAfiliado().getLogotipo();
                String logo2 = ventaViaje.getAfiliado().getLogotipo();

                // Quita la parte inicial de la cadena base64
                String imageDataBytes = logo2.substring(logo2.indexOf(",") + 1);

                byte[] logoBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(imageDataBytes);

                // Crea una imagen con los bytes decodificados
                Image logotipo = Image.getInstance(logoBytes);
                logotipo.scaleToFit(100, 50);
                logotipo.setAbsolutePosition(380, 690);

                document.add(logotipo);

                // Generar el código de barras--------------------------
                Barcode128 codigo128 = new Barcode128();
                codigo128.setCode(detalles.get(i).getFolio());
                Image imagen = codigo128.createImageWithBarcode(archivo.getDirectContent(), BaseColor.BLACK,
                        BaseColor.BLACK);
                imagen.scalePercent(100);

                imagen.setAbsolutePosition(380, 650);
                document.add(imagen);

                // ------------------------------------

                if (detalles.size() > 1) {
                    document.newPage();
                }

            }

            // Cerrar el documento
            document.close();

            System.out.println("El archivo PDF con el código de barras se ha creado correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // boleto redondo
    @Async
    public boolean boletoPDFRedondo(VentaViajesMovil ventaViaje, VentaViajesMovil ventaViajeRegreso,
            List<DetalleVentaMovil> detalles, double ivaReal, AutobusRepository repoAutobus) {
        try {
            Rectangle personalizado = new RectangleReadOnly(595, 1108);
            // Crear un objeto Document
            Document document = new Document();
            document.setPageSize(personalizado);

            // Especificar la ubicación del archivo PDF
            String pdfFilePath = "barcode_example.pdf";
            PdfWriter archivo = PdfWriter.getInstance(document, new FileOutputStream(pdfFilePath));

            // Abrir el documento
            document.open();

            // ---------------variables generales---------------------------
            // imagen de fondo
            Image img = Image.getInstance("src/main/resources/templates/plantilla-viaje-redondo.png");
            // img.scalePercent(47);
            img.scalePercent(23.5f);
            img.setAbsolutePosition(0, 0);
            // img.setAbsolutePosition(40, 150);

            // estilos de letra
            BaseFont baseFont = BaseFont.createFont();
            BaseColor colorBlueCustom = new BaseColor(3, 66, 140);
            // BaseColor colorBlueCustom = new BaseColor(37, 125, 192);
            BaseColor colorGrayCustom = new BaseColor(100, 100, 100);
            Font fontBlue = new Font(baseFont, 12, Font.BOLD, colorBlueCustom);
            Font fontBlueLight = new Font(baseFont, 10, Font.NORMAL, colorBlueCustom);
            Font fontBlueBig = new Font(baseFont, 30, Font.BOLD, colorBlueCustom);
            Font fontBlueBig2 = new Font(baseFont, 40, Font.BOLD, colorBlueCustom);
            Font fontGray = new Font(baseFont, 12, Font.NORMAL, colorGrayCustom);

            // marcar limite para viaje redondo
            System.out.println("el tamaño de los datos son: " + detalles.size());
            int limite = detalles.size() / 2;

            // fechas de viaje
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime fechaHora = LocalDateTime.parse(detalles.get(0).getFechaViaje(), formatter);
            String fechaViaje = fechaHora.format(DateTimeFormatter.ofPattern("dd MMM yyyy")).toUpperCase().replace(".",
                    "");
            String horaViaje = fechaHora.format(DateTimeFormatter.ofPattern("hh:mm a")).replace(".", "").trim();
            // vuelta
            LocalDateTime fechaHoraVuelta = LocalDateTime.parse(detalles.get(limite).getFechaViaje(), formatter);
            String fechaViajeVuelta = fechaHoraVuelta.format(DateTimeFormatter.ofPattern("dd MMM yyyy")).toUpperCase()
                    .replace(".", "");
            String horaViajeVuelta = fechaHoraVuelta.format(DateTimeFormatter.ofPattern("hh:mm a")).replace(".", "")
                    .trim();

            // rellenar los boletos
            // for(int i=0; i<detalles.size(); i++) {
            for (int i = 0; i < limite; i++) {
                double ivaD = ivaReal;
                double totalD = detalles.get(i).getCosto();
                double ivaTotal = totalD * ivaD;
                double subtotalD = totalD - ivaTotal;

                double totalDVuelta = detalles.get(i + limite).getCosto();
                double ivaTotalVuelta = totalDVuelta * ivaD;
                double subtotalDVuelta = totalDVuelta - ivaTotalVuelta;

                boolean isDoubleDeckerIda = false;
                boolean isDoubleDeckerVuelta = false;
                Optional<Autobus> autobusIda = repoAutobus.findById(Math.round(detalles.get(i).getAutobus()));
                Optional<Autobus> autobusVuelta = repoAutobus
                        .findById(Math.round(detalles.get(i + limite).getAutobus()));

                if (autobusIda.isPresent()) {
                    Autobus autobus = autobusIda.get();
                    if (Arrays.asList(7L, 8L, 9L).contains(autobus.getTipoTransporte().getIdTipoTransporte())) {
                        isDoubleDeckerIda = true;
                    } else {
                        isDoubleDeckerIda = false;
                    }
                } else {
                    isDoubleDeckerIda = false;
                }

                if (autobusVuelta.isPresent()) {
                    Autobus autobus = autobusVuelta.get();
                    if (Arrays.asList(7L, 8L, 9L).contains(autobus.getTipoTransporte().getIdTipoTransporte())) {
                        isDoubleDeckerVuelta = true;
                    } else {
                        isDoubleDeckerVuelta = false;
                    }
                } else {
                    isDoubleDeckerVuelta = false;
                }
                // -----------------------------------
                // formateo de numeros
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                decimalFormat.setDecimalSeparatorAlwaysShown(true);
                decimalFormat.setMinimumFractionDigits(2);

                // imagen de fondo boleto---------------------
                document.add(img);

                // cuadro de pasajero -----------------
                Paragraph nombre = new Paragraph(detalles.get(i).getNombrePasajero(), fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, nombre, 30, 950, 0);
                Paragraph origenp = new Paragraph(ventaViaje.getTerminalOrigen().getCiudad().getDescripcion() + ", "
                        + ventaViaje.getTerminalOrigen().getEstados().getDescripcion(), fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, origenp, 30, 905, 0);
                Paragraph origenDireccionIda = new Paragraph("(" + ventaViaje.getTerminalOrigen().getDireccion() + ")",
                        fontBlueLight);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, origenDireccionIda, 30, 893,
                        0);
                Paragraph destinop = new Paragraph(ventaViaje.getTerminalDestino().getCiudad().getDescripcion() + ", "
                        + ventaViaje.getTerminalDestino().getEstados().getDescripcion(), fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, destinop, 30, 865, 0);
                Paragraph destinoDireccionIda = new Paragraph(
                        "(" + ventaViaje.getTerminalDestino().getDireccion() + ")", fontBlueLight);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, destinoDireccionIda, 30, 853,
                        0);

                Paragraph nAsientop = new Paragraph(
                        (detalles.get(i).getNumeroAsiento() == null || detalles.get(i).getNumeroAsiento() == 0) ? "S/N"
                                : detalles.get(i).getNumeroAsiento().toString(),
                        fontBlueBig2);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_CENTER, nAsientop, 70, 790, 0);
                if (isDoubleDeckerIda) {
                    String plantaIda = "Planta baja";
                    if (detalles.get(i).getTipoPlanta() == null || detalles.get(i).getTipoPlanta() == 1) {
                        plantaIda = "Planta baja";
                    } else {
                        plantaIda = "Planta alta";
                    }
                    Paragraph tipoPlantaIda = new Paragraph(plantaIda, fontBlueLight);
                    ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_CENTER, tipoPlantaIda, 70, 780,
                            0);
                }
                Paragraph tipoBoleto = new Paragraph(detalles.get(i).getTipoBoleto().getDescripcion(), fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_CENTER, tipoBoleto, 70, 769, 0);

                // seccion fecha hora
                Paragraph fechap = new Paragraph(fechaViaje, fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, fechap, 140, 810, 0);
                Paragraph horap = new Paragraph(horaViaje, fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, horap, 140, 770, 0);

                // cuadro precios
                Paragraph subtotald = new Paragraph("$" + decimalFormat.format(subtotalD) + " MXN", fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, subtotald, 450, 835, 0);

                Paragraph ivad = new Paragraph("$" + decimalFormat.format(ivaTotal) + " MXN", fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, ivad, 450, 820, 0);

                Paragraph totald = new Paragraph("$" + decimalFormat.format(detalles.get(i).getCosto()) + " MXN",
                        fontBlueBig);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_CENTER, totald, 440, 770, 0);

                // apartado folio-transaccion
                Paragraph nTransaccionp = new Paragraph(detalles.get(i).getId_detalle_venta() + "", fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, nTransaccionp, 350, 870, 0);

                Paragraph nFoliop = new Paragraph(detalles.get(i).getFolio(), fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, nFoliop, 450, 870, 0);

                // Generar el código qr
                BarcodeQRCode qrDato = new BarcodeQRCode(detalles.get(i).getFolio(), 70, 70, null);
                Image imagenQr = qrDato.getImage();

                imagenQr.setAbsolutePosition(250, 780);
                document.add(imagenQr);

                // codigo para agregar logotipo afiliado------------------
                String logo = detalles.get(i).getVentaViajes().getAfiliado().getLogotipo();
                String logo2 = ventaViaje.getAfiliado().getLogotipo();

                // Quita la parte inicial de la cadena base64
                String imageDataBytes = logo2.substring(logo2.indexOf(",") + 1);

                byte[] logoBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(imageDataBytes);

                // Crea una imagen con los bytes decodificados
                Image logotipo = Image.getInstance(logoBytes);
                logotipo.scaleToFit(100, 50);
                logotipo.setAbsolutePosition(380, 940);

                document.add(logotipo);

                // Generar el código de barras
                Barcode128 codigo128 = new Barcode128();
                codigo128.setCode(detalles.get(i).getFolio());
                Image imagen = codigo128.createImageWithBarcode(archivo.getDirectContent(), BaseColor.BLACK,
                        BaseColor.BLACK);
                imagen.scalePercent(100);

                imagen.setAbsolutePosition(380, 900);
                document.add(imagen);

                // ----------------VIAJE DE VUELTA--------------------
                // cuadro de pasajero -----------------
                Paragraph nombreVuelta = new Paragraph(detalles.get(i + limite).getNombrePasajero(), fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, nombreVuelta, 30, 700, 0);
                Paragraph origenpVuelta = new Paragraph(
                        ventaViajeRegreso.getTerminalOrigen().getCiudad().getDescripcion() + ", "
                                + ventaViaje.getTerminalOrigen().getEstados().getDescripcion(),
                        fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, origenpVuelta, 30, 655, 0);
                Paragraph origenDireccionVuelta = new Paragraph(
                        "(" + ventaViajeRegreso.getTerminalOrigen().getDireccion() + ")", fontBlueLight);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, origenDireccionVuelta, 30,
                        643, 0);
                Paragraph destinopVuelta = new Paragraph(
                        ventaViajeRegreso.getTerminalDestino().getCiudad().getDescripcion() + ", "
                                + ventaViaje.getTerminalDestino().getEstados().getDescripcion(),
                        fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, destinopVuelta, 30, 615, 0);
                Paragraph destinoDireccionVuelta = new Paragraph(
                        "(" + ventaViajeRegreso.getTerminalDestino().getDireccion() + ")", fontBlueLight);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, destinoDireccionVuelta, 30,
                        603, 0);

                Paragraph nAsientopVuelta = new Paragraph(
                        (detalles.get(i + limite).getNumeroAsiento() == null
                                || detalles.get(i + limite).getNumeroAsiento() == 0) ? "S/N"
                                        : detalles.get(i + limite).getNumeroAsiento().toString(),
                        fontBlueBig2);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_CENTER, nAsientopVuelta, 70, 540,
                        0);
                if (isDoubleDeckerVuelta) {
                    String plantaVuelta = "Planta baja";
                    if (detalles.get(i + limite).getTipoPlanta() == null
                            || detalles.get(i + limite).getTipoPlanta() == 1) {
                        plantaVuelta = "Planta baja";
                    } else {
                        plantaVuelta = "Planta alta";
                    }
                    Paragraph tipoPlantaIda = new Paragraph(plantaVuelta, fontBlueLight);
                    ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_CENTER, tipoPlantaIda, 70, 530,
                            0);
                }
                Paragraph tipoBoletoVuelta = new Paragraph(detalles.get(i + limite).getTipoBoleto().getDescripcion(),
                        fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_CENTER, tipoBoletoVuelta, 70, 519,
                        0);

                // seccion fecha hora
                Paragraph fechapVuelta = new Paragraph(fechaViajeVuelta, fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, fechapVuelta, 140, 560, 0);
                Paragraph horapVuelta = new Paragraph(horaViajeVuelta, fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, horapVuelta, 140, 520, 0);

                // cuadro precios
                Paragraph subtotaldVuelta = new Paragraph("$" + decimalFormat.format(subtotalDVuelta) + " MXN",
                        fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, subtotaldVuelta, 450, 585,
                        0);

                Paragraph ivadVuelta = new Paragraph("$" + decimalFormat.format(ivaTotalVuelta) + " MXN", fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, ivadVuelta, 450, 570, 0);

                Paragraph totaldVuelta = new Paragraph(
                        "$" + decimalFormat.format(detalles.get(i + limite).getCosto()) + " MXN", fontBlueBig);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_CENTER, totaldVuelta, 440, 520, 0);

                // apartado folio-transaccion
                Paragraph nTransaccionpVuelta = new Paragraph(detalles.get(i + limite).getId_detalle_venta() + "",
                        fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, nTransaccionpVuelta, 350,
                        620, 0);

                Paragraph nFoliopVuelta = new Paragraph(detalles.get(i + limite).getFolio(), fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, nFoliopVuelta, 450, 620, 0);

                // Generar el código qr
                BarcodeQRCode qrDatoVuelta = new BarcodeQRCode(detalles.get(i + limite).getFolio(), 70, 70, null);
                Image imagenQrVuelta = qrDatoVuelta.getImage();

                imagenQrVuelta.setAbsolutePosition(250, 530);
                document.add(imagenQrVuelta);

                // codigo para agregar logotipo afiliado------------------
                String logoVuelta = detalles.get(i + limite).getVentaViajes().getAfiliado().getLogotipo();
                String logo2Vuelta = ventaViajeRegreso.getAfiliado().getLogotipo();

                // Quita la parte inicial de la cadena base64
                String imageDataBytesVuelta = logo2Vuelta.substring(logo2Vuelta.indexOf(",") + 1);

                byte[] logoBytesVuelta = javax.xml.bind.DatatypeConverter.parseBase64Binary(imageDataBytesVuelta);

                // Crea una imagen con los bytes decodificados
                Image logotipoVuelta = Image.getInstance(logoBytesVuelta);
                logotipoVuelta.scaleToFit(100, 50);
                logotipoVuelta.setAbsolutePosition(380, 690);

                document.add(logotipoVuelta);

                // Generar el código de barras
                Barcode128 codigo128Vuelta = new Barcode128();
                codigo128Vuelta.setCode(detalles.get(i + limite).getFolio());
                Image imagenVuelta = codigo128.createImageWithBarcode(archivo.getDirectContent(), BaseColor.BLACK,
                        BaseColor.BLACK);
                imagenVuelta.scalePercent(100);

                imagenVuelta.setAbsolutePosition(380, 650);
                document.add(imagenVuelta);
                // ----------------------------------------------

                if (detalles.size() > 1) {
                    document.newPage();
                }

            }

            // Cerrar el documento
            document.close();

            System.out.println("El archivo PDF con el código de barras se ha creado correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // boleto tour
    @Async
    public boolean boletoPDFTour(VentaTourMovil ventaViaje, List<DetalleVentaTourMovil> detalles, double ivaReal) {
        try {
            // Crear un objeto Document
            Document document = new Document();

            // Especificar la ubicación del archivo PDF
            String pdfFilePath = "barcode_example.pdf";
            PdfWriter archivo = PdfWriter.getInstance(document, new FileOutputStream(pdfFilePath));

            // Abrir el documento
            document.open();

            // ---------------variables generales---------------------------
            // imagen de fondo
            Image img = Image.getInstance("src/main/resources/templates/plantilla-viaje-sencillo.png");
            // img.scalePercent(47);
            img.scalePercent(23.5f);
            img.setAbsolutePosition(0, 0);
            // img.setAbsolutePosition(40, 150);

            // estilos de letra
            BaseFont baseFont = BaseFont.createFont();
            BaseColor colorBlueCustom = new BaseColor(3, 66, 140);
            // BaseColor colorBlueCustom = new BaseColor(37, 125, 192);
            BaseColor colorGrayCustom = new BaseColor(100, 100, 100);
            Font fontBlue = new Font(baseFont, 12, Font.BOLD, colorBlueCustom);
            Font fontBlueBig = new Font(baseFont, 30, Font.BOLD, colorBlueCustom);
            Font fontBlueBig2 = new Font(baseFont, 40, Font.BOLD, colorBlueCustom);
            Font fontGray = new Font(baseFont, 12, Font.NORMAL, colorGrayCustom);

            // fechas de viaje
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime fechaHora = LocalDateTime.parse(detalles.get(0).getFechaViaje(), formatter);
            String fechaViaje = fechaHora.format(DateTimeFormatter.ofPattern("dd MMM yyyy")).toUpperCase().replace(".",
                    "");
            String horaViaje = fechaHora.format(DateTimeFormatter.ofPattern("hh:mm a")).replace(".", "").trim();

            // rellenar los boletos
            for (int i = 0; i < detalles.size(); i++) {
                double ivaD = ivaReal;
                double totalD = detalles.get(i).getCosto();
                double ivaTotal = totalD * ivaD;
                double subtotalD = totalD - ivaTotal;

                // -----------------------------------
                // formateo de numeros
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                decimalFormat.setDecimalSeparatorAlwaysShown(true);
                decimalFormat.setMinimumFractionDigits(2);

                // imagen de fondo boleto---------------------
                document.add(img);

                // cuadro de pasajero -----------------
                Paragraph nombre = new Paragraph(detalles.get(i).getNombrePasajero(), fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, nombre, 30, 700, 0);
                Paragraph origenp = new Paragraph(ventaViaje.getTour().getCiudadesOrigen().getDescripcion() + ", "
                        + ventaViaje.getTour().getEstadoOrigen().getDescripcion(), fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, origenp, 30, 660, 0);
                Paragraph destinop = new Paragraph(ventaViaje.getTour().getCiudadesDestino().getDescripcion() + ", "
                        + ventaViaje.getTour().getEstadoDestino().getDescripcion(), fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, destinop, 30, 620, 0);

                Paragraph nAsientop = new Paragraph(
                        (detalles.get(i).getNumeroAsiento() == 0 || detalles.get(i).getNumeroAsiento() == null) ? "S/N"
                                : detalles.get(i).getNumeroAsiento().toString(),
                        fontBlueBig2);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_CENTER, nAsientop, 70, 540, 0);
                Paragraph tipoBoleto = new Paragraph(detalles.get(i).getTipoBoleto().getDescripcion(), fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_CENTER, tipoBoleto, 70, 520, 0);

                // seccion fecha hora
                Paragraph fechap = new Paragraph(fechaViaje, fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, fechap, 140, 560, 0);
                Paragraph horap = new Paragraph(horaViaje, fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, horap, 140, 520, 0);

                // cuadro precios
                Paragraph subtotald = new Paragraph("$" + decimalFormat.format(subtotalD) + " MXN", fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, subtotald, 450, 590, 0);

                Paragraph ivad = new Paragraph("$" + decimalFormat.format(ivaTotal) + " MXN", fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, ivad, 450, 575, 0);

                Paragraph totald = new Paragraph("$" + decimalFormat.format(detalles.get(i).getCosto()) + " MXN",
                        fontBlueBig);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_CENTER, totald, 440, 520, 0);

                // apartado folio-transaccion
                Paragraph nTransaccionp = new Paragraph(detalles.get(i).getId_detalle_venta_tour() + "", fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, nTransaccionp, 350, 620, 0);

                Paragraph nFoliop = new Paragraph(detalles.get(i).getFolio(), fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, nFoliop, 450, 620, 0);

                // Generar el código qr
                BarcodeQRCode qrDato = new BarcodeQRCode(detalles.get(i).getFolio(), 70, 70, null);
                Image imagenQr = qrDato.getImage();

                imagenQr.setAbsolutePosition(250, 530);
                document.add(imagenQr);

                // codigo para agregar logotipo afiliado------------------
                String logo = detalles.get(i).getVentaTour().getAfiliado().getLogotipo();
                String logo2 = ventaViaje.getAfiliado().getLogotipo();

                // Quita la parte inicial de la cadena base64
                String imageDataBytes = logo2.substring(logo2.indexOf(",") + 1);

                byte[] logoBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(imageDataBytes);

                // Crea una imagen con los bytes decodificados
                Image logotipo = Image.getInstance(logoBytes);
                logotipo.scaleToFit(100, 50);
                logotipo.setAbsolutePosition(380, 690);

                document.add(logotipo);

                // Generar el código de barras
                Barcode128 codigo128 = new Barcode128();
                codigo128.setCode(detalles.get(i).getFolio());
                Image imagen = codigo128.createImageWithBarcode(archivo.getDirectContent(), BaseColor.BLACK,
                        BaseColor.BLACK);
                imagen.scalePercent(100);

                imagen.setAbsolutePosition(380, 650);
                document.add(imagen);

                // ------------------------------------

                if (detalles.size() > 1) {
                    document.newPage();
                }

            }

            // Cerrar el documento
            document.close();

            System.out.println("El archivo PDF con el código de barras se ha creado correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // segundo codigo
    @Async
    public void codigoBarrasPdf2(VentaViajesMovil ventaViaje, List<DetalleVentaMovil> detalles, double ivaReal) {
        try {
            // Crear un objeto Document
            Document document = new Document();

            // Especificar la ubicación del archivo PDF
            String pdfFilePath = "barcode_example.pdf";
            PdfWriter archivo = PdfWriter.getInstance(document, new FileOutputStream(pdfFilePath));

            // Abrir el documento
            document.open();

            // ---------------variables generales---------------------------
            // imagen de fondo
            Image img = Image.getInstance("src/main/resources/templates/fondoBoleto.png");
            img.scalePercent(20);
            img.setAbsolutePosition(40, 150);

            // estilos de letra
            BaseFont baseFont = BaseFont.createFont();
            BaseColor colorBlueCustom = new BaseColor(3, 66, 140);
            // BaseColor colorBlueCustom = new BaseColor(37, 125, 192);
            BaseColor colorGrayCustom = new BaseColor(100, 100, 100);
            Font fontBlue = new Font(baseFont, 12, Font.BOLD, colorBlueCustom);
            Font fontGray = new Font(baseFont, 12, Font.NORMAL, colorGrayCustom);

            // fechas de viaje
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime fechaHora = LocalDateTime.parse(detalles.get(0).getFechaViaje(), formatter);
            String fechaViaje = fechaHora.format(DateTimeFormatter.ofPattern("dd MMM yyyy")).toUpperCase().replace(".",
                    "");
            String horaViaje = fechaHora.format(DateTimeFormatter.ofPattern("hh:mm a")).replace(".", "").trim();

            // rellenar los boletos
            for (int i = 0; i < detalles.size(); i++) {
                double ivaD = ivaReal;
                double totalD = detalles.get(i).getCosto();
                double ivaTotal = totalD * ivaD;
                double subtotalD = totalD - ivaTotal;

                // -----------------------------------
                // formateo de numeros
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                decimalFormat.setDecimalSeparatorAlwaysShown(true);
                decimalFormat.setMinimumFractionDigits(2);

                // imagen de fondo
                document.add(img);

                // cuadro 1
                Paragraph tipoBoleto = new Paragraph(detalles.get(i).getTipoBoleto().getDescripcion(), fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, tipoBoleto, 100, 670, 0);
                Paragraph nombre = new Paragraph(detalles.get(i).getNombrePasajero(), fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, nombre, 100, 650, 0);

                // cuadro 2
                Paragraph fechap = new Paragraph(fechaViaje, fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, fechap, 100, 610, 0);
                Paragraph horap = new Paragraph(horaViaje, fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, horap, 400, 610, 0);

                // contenido cuadro dos
                Paragraph origenTp = new Paragraph("Origen:", fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, origenTp, 100, 570, 0);
                Paragraph origenp = new Paragraph(ventaViaje.getTerminalOrigen().getCiudad().getDescripcion() + ", "
                        + ventaViaje.getTerminalOrigen().getEstados().getDescripcion(), fontGray);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, origenp, 220, 570, 0);

                Paragraph destinoTp = new Paragraph("Destino:", fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, destinoTp, 100, 510, 0);
                Paragraph destinop = new Paragraph(ventaViaje.getTerminalDestino().getCiudad().getDescripcion() + ", "
                        + ventaViaje.getTerminalDestino().getEstados().getDescripcion(), fontGray);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, destinop, 220, 510, 0);

                Paragraph asientop = new Paragraph("Numero de asiento:", fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, asientop, 100, 450, 0);
                Paragraph nAsientop = new Paragraph(detalles.get(i).getNumeroAsiento() + "", fontGray);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, nAsientop, 220, 450, 0);

                // apartado folio-transaccion
                Paragraph folioTp = new Paragraph("FOLIO", fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, folioTp, 100, 350, 0);
                Paragraph nFoliop = new Paragraph(detalles.get(i).getFolio(), fontGray);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, nFoliop, 100, 330, 0);

                Paragraph transaccionp = new Paragraph("TRANSACCIÓN", fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, transaccionp, 100, 310, 0);
                Paragraph nTransaccionp = new Paragraph(detalles.get(i).getId_detalle_venta() + "", fontGray);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, nTransaccionp, 100, 290, 0);

                // cuadro tres
                Paragraph subtotalp = new Paragraph("SUBTOTAL:", fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, subtotalp, 320, 350, 0);
                Paragraph subtotald = new Paragraph("$" + decimalFormat.format(subtotalD), fontGray);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, subtotald, 450, 350, 0);

                Paragraph ivap = new Paragraph("IVA:", fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, ivap, 320, 330, 0);
                Paragraph ivad = new Paragraph("$" + decimalFormat.format(ivaTotal), fontGray);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, ivad, 450, 330, 0);

                Paragraph totalp = new Paragraph("TOTAL:", fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, totalp, 320, 310, 0);
                Paragraph totald = new Paragraph("$" + decimalFormat.format(detalles.get(i).getCosto()), fontGray);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, totald, 450, 310, 0);

                // Generar el código qr
                BarcodeQRCode qrDato = new BarcodeQRCode(detalles.get(i).getFolio(), 100, 100, null);
                Image imagenQr = qrDato.getImage();

                imagenQr.setAbsolutePosition(90, 190);
                document.add(imagenQr);

                // Generar el código de barras
                Barcode128 codigo128 = new Barcode128();
                codigo128.setCode(detalles.get(i).getFolio());
                Image imagen = codigo128.createImageWithBarcode(archivo.getDirectContent(), BaseColor.BLACK,
                        BaseColor.RED);
                imagen.scalePercent(100);

                imagen.setAbsolutePosition(320, 220);
                document.add(imagen);

                // ------------------------------------

                if (detalles.size() > 1) {
                    document.newPage();
                }

            }

            // Cerrar el documento
            document.close();

            System.out.println("El archivo PDF con el código de barras se ha creado correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // generar pdf para venta de tour
    @Async
    public void codigoBarrasPdfTour2(VentaTourMovil ventaViaje, List<DetalleVentaTourMovil> detalles, double ivaReal) {
        // public static void main(String[] args) {
        try {
            // Crear un objeto Document
            Document document = new Document();

            // Especificar la ubicación del archivo PDF
            String pdfFilePath = "barcode_example.pdf";
            PdfWriter archivo = PdfWriter.getInstance(document, new FileOutputStream(pdfFilePath));

            // Abrir el documento
            document.open();

            // ---------------variables generales---------------------------
            // imagen de fondo
            Image img = Image.getInstance("src/main/resources/templates/fondoBoleto.png");
            img.scalePercent(20);
            img.setAbsolutePosition(40, 150);

            // estilos de letra
            BaseFont baseFont = BaseFont.createFont();
            BaseColor colorBlueCustom = new BaseColor(3, 66, 140);
            // BaseColor colorBlueCustom = new BaseColor(37, 125, 192);
            BaseColor colorGrayCustom = new BaseColor(100, 100, 100);
            Font fontBlue = new Font(baseFont, 12, Font.BOLD, colorBlueCustom);
            Font fontGray = new Font(baseFont, 12, Font.NORMAL, colorGrayCustom);

            // fechas de viaje
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime fechaHora = LocalDateTime.parse(detalles.get(0).getFechaViaje(), formatter);
            String fechaViaje = fechaHora.format(DateTimeFormatter.ofPattern("dd MMM yyyy")).toUpperCase().replace(".",
                    "");
            String horaViaje = fechaHora.format(DateTimeFormatter.ofPattern("hh:mm a")).replace(".", "").trim();

            // rellenar los boletos
            for (int i = 0; i < detalles.size(); i++) {
                double ivaD = ivaReal;
                double totalD = detalles.get(i).getCosto();
                double ivaTotal = totalD * ivaD;
                double subtotalD = totalD - ivaTotal;

                // formateo de numeros
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                decimalFormat.setDecimalSeparatorAlwaysShown(true);
                decimalFormat.setMinimumFractionDigits(2);
                // imagen de fondo
                document.add(img);

                // cuadro 1
                Paragraph tipoBoleto = new Paragraph(detalles.get(i).getTipoBoleto().getDescripcion(), fontBlue);
                // tipoBoleto.setAlignment(Element.ALIGN_CENTER);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, tipoBoleto, 100, 670, 0);
                Paragraph nombre = new Paragraph(detalles.get(i).getNombrePasajero(), fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, nombre, 100, 650, 0);

                // cuadro 2
                Paragraph fecha = new Paragraph(fechaViaje, fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, fecha, 100, 610, 0);
                Paragraph hora = new Paragraph(horaViaje, fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, hora, 400, 610, 0);

                // contenido cuadro dos
                Paragraph origenT = new Paragraph("Origen:", fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, origenT, 100, 570, 0);
                Paragraph origen = new Paragraph(ventaViaje.getTour().getCiudadesOrigen().getDescripcion() + ", "
                        + ventaViaje.getTour().getEstadoOrigen().getDescripcion(), fontGray);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, origen, 220, 570, 0);

                Paragraph destinoT = new Paragraph("Destino:", fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, destinoT, 100, 510, 0);
                Paragraph destino = new Paragraph(ventaViaje.getTour().getCiudadesDestino().getDescripcion() + ", "
                        + ventaViaje.getTour().getEstadoDestino().getDescripcion(), fontGray);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, destino, 220, 510, 0);

                Paragraph asiento = new Paragraph("Numero de asiento:", fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, asiento, 100, 450, 0);
                Paragraph nAsiento = new Paragraph(detalles.get(i).getNumeroAsiento() + "", fontGray);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, nAsiento, 220, 450, 0);

                // apartado folio-transaccion
                Paragraph folioT = new Paragraph("FOLIO", fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, folioT, 100, 350, 0);
                Paragraph nFolio = new Paragraph(detalles.get(i).getFolio(), fontGray);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, nFolio, 100, 330, 0);

                Paragraph transaccion = new Paragraph("TRANSACCIÓN", fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, transaccion, 100, 310, 0);
                Paragraph nTransaccion = new Paragraph(detalles.get(i).getId_detalle_venta_tour() + "", fontGray);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, nTransaccion, 100, 290, 0);

                // cuadro tres
                Paragraph subtotal = new Paragraph("SUBTOTAL:", fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, subtotal, 320, 350, 0);
                Paragraph subtotald = new Paragraph("$" + decimalFormat.format(subtotalD), fontGray);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, subtotald, 450, 350, 0);

                Paragraph iva = new Paragraph("IVA:", fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, iva, 320, 330, 0);
                Paragraph ivad = new Paragraph("$" + decimalFormat.format(ivaTotal), fontGray);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, ivad, 450, 330, 0);

                Paragraph total = new Paragraph("TOTAL:", fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, total, 320, 310, 0);
                Paragraph totald = new Paragraph("$" + decimalFormat.format(detalles.get(i).getCosto()), fontGray);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, totald, 450, 310, 0);

                // Generar el código qr
                BarcodeQRCode qrDato = new BarcodeQRCode(detalles.get(i).getFolio(), 100, 100, null);
                Image imagenQr = qrDato.getImage();

                imagenQr.setAbsolutePosition(90, 190);
                document.add(imagenQr);

                // Generar el código de barras
                Barcode128 codigo128 = new Barcode128();
                codigo128.setCode(detalles.get(i).getFolio());
                Image imagen = codigo128.createImageWithBarcode(archivo.getDirectContent(), BaseColor.BLACK,
                        BaseColor.RED);
                imagen.scalePercent(100);

                imagen.setAbsolutePosition(320, 220);
                document.add(imagen);
                // ----------------------------------
                if (detalles.size() > 1) {
                    document.newPage();
                }

            }

            // Cerrar el documento
            document.close();

            System.out.println("El archivo PDF con el código de barras se ha creado correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
