package com.gorigeek.springboot.distribution.utils;

import com.gorigeek.springboot.distribution.entity.VentasDistribution;
import com.gorigeek.springboot.entity.DetalleVentaMovil;
import com.gorigeek.springboot.entity.DetalleVentaTourMovil;
import com.gorigeek.springboot.entity.VentaTourMovil;
import com.gorigeek.springboot.entity.VentaViajesMovil;
import com.itextpdf.text.BadElementException;
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
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.RectangleReadOnly;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.springframework.scheduling.annotation.Async;

public class BarcodeToPDFDistribution {

    // boleto sencillo
    @Async
    public boolean boletoPDF(List<VentasDistribution> ventas, double ivaReal) {
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
            img.scalePercent(47);
            img.setAbsolutePosition(0, 0);
            // img.setAbsolutePosition(40, 150);

            // estilos de letra
            BaseFont baseFont = BaseFont.createFont();
            BaseColor colorBlueCustom = new BaseColor(3, 66, 140);
            // BaseColor colorBlueCustom = new BaseColor(37, 125, 192);
            BaseColor colorGrayCustom = new BaseColor(100, 100, 100);
            Font fontBlue = new Font(baseFont, 12, Font.BOLD, colorBlueCustom);
            Font fontBlueSmall = new Font(baseFont, 4, Font.BOLD, colorBlueCustom);
            Font fontBlueBig = new Font(baseFont, 30, Font.BOLD, colorBlueCustom);
            Font fontBlueBig2 = new Font(baseFont, 40, Font.BOLD, colorBlueCustom);
            Font fontGray = new Font(baseFont, 12, Font.NORMAL, colorGrayCustom);

            // Fechas de viaje
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            String fechaHoraViajeDate = ventas.get(0).getFechaHoraViaje();
            LocalDateTime fechaHora = LocalDateTime.parse(fechaHoraViajeDate, formatter);
            String fechaViaje = fechaHora.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
                    .toUpperCase().replace(".", "");
            String horaViaje = fechaHora.format(DateTimeFormatter.ofPattern("hh:mm a"))
                    .replace(".", "").trim();

            // rellenar los boletos
            for (int i = 0; i < ventas.size(); i++) {
                double ivaD = ivaReal;
                double totalD = ventas.get(i).getPrecioBoleto();
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
                Paragraph nombre = new Paragraph(ventas.get(i).getNombrePasajero(), fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, nombre, 30, 700, 0);
//                Paragraph origenp = new Paragraph(ventaViaje.getTerminalOrigen().getCiudad().getDescripcion()+", "+ventaViaje.getTerminalOrigen().getEstados().getDescripcion(), fontBlue);
                Paragraph origenp = new Paragraph(ventas.get(i).getOrigen(), fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, origenp, 30, 660, 0);
//                Paragraph destinop = new Paragraph(ventaViaje.getTerminalDestino().getCiudad().getDescripcion()+", "+ventaViaje.getTerminalDestino().getEstados().getDescripcion(), fontBlue);
                Paragraph destinop = new Paragraph(ventas.get(i).getDestino(), fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, destinop, 30, 620, 0);
                Paragraph nAsientop;
                if (ventas.get(i).getNumAsiento() != null) {
                    nAsientop = new Paragraph(ventas.get(i).getNumAsiento() + "", fontBlueBig2);
                } else {
                    nAsientop = new Paragraph("S/N", fontBlueBig2);
                }
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_CENTER, nAsientop, 70, 540, 0);
//                Paragraph tipoBoleto = new Paragraph(detalles.get(i).getTipoBoleto().getDescripcion(), fontBlue);
                Paragraph tipoBoleto = new Paragraph("sencillo", fontBlue);
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

                Paragraph totald = new Paragraph("$" + decimalFormat.format(ventas.get(i).getPrecioBoleto()) + " MXN",
                        fontBlueBig);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_CENTER, totald, 440, 520, 0);

                // apartado folio-transaccion
                Paragraph nTransaccionp = new Paragraph(ventas.get(i).getIdReservacion() + "", fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, nTransaccionp, 350, 620, 0);
                if (ventas.get(i).getFolioBoleto() != null) {
                    Paragraph nFoliop = new Paragraph(ventas.get(i).getFolioBoleto(), fontBlueSmall);
                    ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, nFoliop, 450, 620, 0);

                }
                // Generar el código qr
                // tamaño original
//                BarcodeQRCode qrDato = new BarcodeQRCode(ventas.get(i).getFolioBoleto(), 70, 70, null);
                // cambiado a
                if (ventas.get(i).getFolioBoleto() != null) {
                    BarcodeQRCode qrDato = new BarcodeQRCode(ventas.get(i).getFolioBoleto(), 90, 90, null);
                    Image imagenQr = qrDato.getImage();
                    imagenQr.setAbsolutePosition(250, 530);
                    document.add(imagenQr);
                }

                // Crea una imagen con los bytes decodificados
                try {
                    byte[] logoData = ventas.get(i).getLogo();

                    // Verificar si el byte array es nulo o está vacío
                    if (logoData == null || logoData.length == 0) {
                        System.out.println("El byte array de la imagen es nulo o está vacío.");
                        // Manejar esta situación según tus necesidades
                    } else {
                        // Intentar crear la instancia de la imagen con iText
                        Image logotipo = Image.getInstance(logoData);
                        logotipo.scaleToFit(Float.MAX_VALUE, 50);
                        logotipo.setAbsolutePosition(380, 690);
                        document.add(logotipo);
                    }
                } catch (IOException e) {
                    // Manejar la excepción de IO
                    e.printStackTrace();
                } catch (BadElementException e) {
                    // Manejar la excepción BadElementException

                    // Intentar convertir la imagen de WebP a PNG
                    byte[] pngData = convertToPNG(ventas.get(i).getLogo());

                    if (pngData != null) {
                        try {
                            // Intentar crear la instancia de la imagen con iText
                            Image logotipo = Image.getInstance(pngData);
                            logotipo.scaleToFit(Float.MAX_VALUE, 50);
                            logotipo.setAbsolutePosition(380, 690);
                            document.add(logotipo);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        } catch (BadElementException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        System.out.println("La conversión de WebP a PNG ha fallado.");
                    }
                }

                // Generar el código de barras--------------------------
                Barcode128 codigo128 = new Barcode128();
                if (ventas.get(i).getFolioBoleto() != null) {
                    codigo128.setCode(ventas.get(i).getFolioBoleto());
                    Image imagen = codigo128.createImageWithBarcode(archivo.getDirectContent(), BaseColor.BLACK,
                            BaseColor.BLACK);
                    imagen.scalePercent(100);
                    imagen.setAbsolutePosition(150, 650);
                    document.add(imagen);
                }
                // ------------------------------------
                if (ventas.size() > 1) {
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
    public boolean boletoPDFRedondo(List<VentasDistribution> ventas, double ivaReal) {
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
            img.scalePercent(47);
            img.setAbsolutePosition(0, 0);
            // img.setAbsolutePosition(40, 150);

            // estilos de letra
            BaseFont baseFont = BaseFont.createFont();
            BaseColor colorBlueCustom = new BaseColor(3, 66, 140);
            // BaseColor colorBlueCustom = new BaseColor(37, 125, 192);
            BaseColor colorGrayCustom = new BaseColor(100, 100, 100);
            Font fontBlue = new Font(baseFont, 12, Font.BOLD, colorBlueCustom);
            Font fontBlueSmall = new Font(baseFont, 4, Font.BOLD, colorBlueCustom);
            Font fontBlueBig = new Font(baseFont, 30, Font.BOLD, colorBlueCustom);
            Font fontBlueBig2 = new Font(baseFont, 40, Font.BOLD, colorBlueCustom);
            Font fontGray = new Font(baseFont, 12, Font.NORMAL, colorGrayCustom);

            // Fecha de ida
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            String fechaHoraViajeDate = ventas.get(0).getFechaHoraViaje(); 
            LocalDateTime fechaHora = LocalDateTime.parse(fechaHoraViajeDate, formatter);
            String fechaViaje = fechaHora.format(DateTimeFormatter.ofPattern("dd MMM yyyy")).toUpperCase().replace(".",
                    "");
            String horaViaje = fechaHora.format(DateTimeFormatter.ofPattern("hh:mm a")).replace(".", "").trim();

            // Fecha de vuelta
            String fechaHoraViajeDateVuelta = ventas.get(1).getFechaHoraViaje(); 
            LocalDateTime fechaHoraVuelta = LocalDateTime.parse(fechaHoraViajeDateVuelta, formatter); 
            String fechaViajeVuelta = fechaHoraVuelta.format(DateTimeFormatter.ofPattern("dd MMM yyyy")).toUpperCase()
                    .replace(".", "");
            String horaViajeVuelta = fechaHoraVuelta.format(DateTimeFormatter.ofPattern("hh:mm a")).replace(".", "")
                    .trim();

            // rellenar los boletos
            // for(int i=0; i<detalles.size(); i++) {
            for (int i = 0; i < ventas.size(); i += 2) {
                double ivaD = ivaReal;
                double totalD = ventas.get(i).getPrecioBoleto();
                double ivaTotal = totalD * ivaD;
                double subtotalD = totalD - ivaTotal;

                double totalDVuelta = ventas.get(i + 1).getPrecioBoleto();
                double ivaTotalVuelta = totalDVuelta * ivaD;
                double subtotalDVuelta = totalDVuelta - ivaTotalVuelta;

                // -----------------------------------
                // formateo de numeros
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                decimalFormat.setDecimalSeparatorAlwaysShown(true);
                decimalFormat.setMinimumFractionDigits(2);

                // imagen de fondo boleto---------------------
                document.add(img);

                // cuadro de pasajero -----------------
                Paragraph nombre = new Paragraph(ventas.get(i).getNombrePasajero(), fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, nombre, 30, 950, 0);
                Paragraph origenp = new Paragraph(ventas.get(i).getOrigen(), fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, origenp, 30, 905, 0);
                Paragraph destinop = new Paragraph(ventas.get(i).getDestino(), fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, destinop, 30, 865, 0);
                Paragraph nAsientop;
                if (ventas.get(i).getNumAsiento() != null) {
                    nAsientop = new Paragraph(ventas.get(i).getNumAsiento() + "", fontBlueBig2);
                } else {
                    nAsientop = new Paragraph("S/N", fontBlueBig2);
                }
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_CENTER, nAsientop, 70, 790, 0);
                Paragraph tipoBoleto = new Paragraph("redondo", fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_CENTER, tipoBoleto, 70, 770, 0);

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

                Paragraph totald = new Paragraph("$" + decimalFormat.format(ventas.get(i).getPrecioBoleto()) + " MXN",
                        fontBlueBig);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_CENTER, totald, 440, 770, 0);

                // apartado folio-transaccion
                Paragraph nTransaccionp = new Paragraph(ventas.get(i).getIdReservacion(), fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, nTransaccionp, 350, 870, 0);
                if (ventas.get(i).getFolioBoleto() != null) {
                    Paragraph nFoliop = new Paragraph(ventas.get(i).getFolioBoleto(), fontBlueSmall);
                    ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, nFoliop, 450, 870, 0);
                }

                // Generar el código qr
//                BarcodeQRCode qrDato = new BarcodeQRCode(ventas.get(i).getFolioBoleto(), 70, 70, null);
                if (ventas.get(i).getFolioBoleto() != null) {
                    BarcodeQRCode qrDato = new BarcodeQRCode(ventas.get(i).getFolioBoleto(), 90, 90, null);
                    Image imagenQr = qrDato.getImage();

                    imagenQr.setAbsolutePosition(250, 780);
                    document.add(imagenQr);
                }

                // Crea una imagen con los bytes decodificados
                try {
                    byte[] logoData = ventas.get(i).getLogo();

                    // Verificar si el byte array es nulo o está vacío
                    if (logoData == null || logoData.length == 0) {
                        System.out.println("El byte array de la imagen es nulo o está vacío.");
                        // Manejar esta situación según tus necesidades
                    } else {
                        // Intentar crear la instancia de la imagen con iText
                        Image logotipo = Image.getInstance(ventas.get(i).getLogo());
                        logotipo.scaleToFit(Float.MAX_VALUE, 50);
                        logotipo.setAbsolutePosition(380, 940);

                        document.add(logotipo);
                    }
                } catch (IOException e) {
                    // Manejar la excepción de IO
                    e.printStackTrace();
                } catch (BadElementException e) {
                    // Manejar la excepción BadElementException

                    // Intentar convertir la imagen de WebP a PNG
                    byte[] pngData = convertToPNG(ventas.get(i).getLogo());

                    if (pngData != null) {
                        try {
                            // Intentar crear la instancia de la imagen con iText
                            Image logotipo = Image.getInstance(pngData);
                            logotipo.scaleToFit(Float.MAX_VALUE, 50);
                            logotipo.setAbsolutePosition(380, 940);
                            document.add(logotipo);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        } catch (BadElementException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        System.out.println("La conversión de WebP a PNG ha fallado.");
                    }
                }

                Barcode128 codigo128 = new Barcode128();
                // Generar el código de barras
                if (ventas.get(i).getFolioBoleto() != null) {
                    codigo128.setCode(ventas.get(i).getFolioBoleto());
                    Image imagen = codigo128.createImageWithBarcode(archivo.getDirectContent(), BaseColor.BLACK,
                            BaseColor.BLACK);
                    imagen.scalePercent(100);

//                    imagen.setAbsolutePosition(380, 900);
                    imagen.setAbsolutePosition(150, 900);
                    document.add(imagen);
                }

                // ----------------VIAJE DE VUELTA--------------------
                // cuadro de pasajero -----------------
                Paragraph nombreVuelta = new Paragraph(ventas.get(i + 1).getNombrePasajero(), fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, nombreVuelta, 30, 700, 0);
                Paragraph origenpVuelta = new Paragraph(ventas.get(i + 1).getOrigen(), fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, origenpVuelta, 30, 655, 0);
                Paragraph destinopVuelta = new Paragraph(ventas.get(i + 1).getDestino(), fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, destinopVuelta, 30, 615, 0);
                Paragraph nAsientopVuelta;
                if (ventas.get(i + 1).getNumAsiento() != null) {
                    nAsientopVuelta = new Paragraph(ventas.get(i + 1).getNumAsiento() + "", fontBlueBig2);

                } else {
                    nAsientopVuelta = new Paragraph("S/N", fontBlueBig2);
                }
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_CENTER, nAsientopVuelta, 70, 540,
                        0);
                Paragraph tipoBoletoVuelta = new Paragraph("redondo", fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_CENTER, tipoBoletoVuelta, 70, 520,
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
                        "$" + decimalFormat.format(ventas.get(i + 1).getPrecioBoleto()) + " MXN", fontBlueBig);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_CENTER, totaldVuelta, 440, 520, 0);

                // apartado folio-transaccion
                Paragraph nTransaccionpVuelta = new Paragraph(ventas.get(i + 1).getIdReservacion(), fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, nTransaccionpVuelta, 350,
                        620, 0);
                if (ventas.get(i + 1).getFolioBoleto() != null) {
                    Paragraph nFoliopVuelta = new Paragraph(ventas.get(i + 1).getFolioBoleto(), fontBlueSmall);
                    ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, nFoliopVuelta, 450, 620,
                            0);
                }

                // Generar el código qr
//                BarcodeQRCode qrDatoVuelta = new BarcodeQRCode(ventas.get(i+1).getFolioBoleto(), 70, 70, null);
                if (ventas.get(i + 1).getFolioBoleto() != null) {
                    BarcodeQRCode qrDatoVuelta = new BarcodeQRCode(ventas.get(i + 1).getFolioBoleto(), 90, 90, null);
                    Image imagenQrVuelta = qrDatoVuelta.getImage();

                    imagenQrVuelta.setAbsolutePosition(250, 530);
                    document.add(imagenQrVuelta);
                }

                // Crea una imagen con los bytes decodificados
                try {
                    byte[] logoData = ventas.get(i).getLogo();

                    // Verificar si el byte array es nulo o está vacío
                    if (logoData == null || logoData.length == 0) {
                        System.out.println("El byte array de la imagen es nulo o está vacío.");
                        // Manejar esta situación según tus necesidades
                    } else {
                        // Intentar crear la instancia de la imagen con iText
                        Image logotipoVuelta = Image.getInstance(ventas.get(i + 1).getLogo());
                        logotipoVuelta.scaleToFit(Float.MAX_VALUE, 50);
                        logotipoVuelta.setAbsolutePosition(380, 690);

                        document.add(logotipoVuelta);
                    }
                } catch (IOException e) {
                    // Manejar la excepción de IO
                    e.printStackTrace();
                } catch (BadElementException e) {
                    // Manejar la excepción BadElementException

                    // Intentar convertir la imagen de WebP a PNG
                    byte[] pngData = convertToPNG(ventas.get(i + 1).getLogo());

                    if (pngData != null) {
                        try {
                            // Intentar crear la instancia de la imagen con iText
                            Image logotipoVuelta = Image.getInstance(pngData);
                            logotipoVuelta.scaleToFit(Float.MAX_VALUE, 50);
                            logotipoVuelta.setAbsolutePosition(380, 690);

                            document.add(logotipoVuelta);

                        } catch (IOException ex) {
                            ex.printStackTrace();
                        } catch (BadElementException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        System.out.println("La conversión de WebP a PNG ha fallado.");
                    }
                }

                // Generar el código de barras
                if (ventas.get(i + 1).getFolioBoleto() != null) {
                    Barcode128 codigo128Vuelta = new Barcode128();
                    codigo128Vuelta.setCode(ventas.get(i + 1).getFolioBoleto());
                    Image imagenVuelta = codigo128.createImageWithBarcode(archivo.getDirectContent(), BaseColor.BLACK,
                            BaseColor.BLACK);
                    imagenVuelta.scalePercent(100);

//                    imagenVuelta.setAbsolutePosition(380, 650);
                    imagenVuelta.setAbsolutePosition(150, 650);
                    document.add(imagenVuelta);
                }

                // ----------------------------------------------

                if (ventas.size() > 1) {
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

    public static byte[] convertToPNG(byte[] imageData) throws IOException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            BufferedImage image = ImageIO.read(inputStream);
            if (image == null) {
                throw new IllegalArgumentException("No se pudo leer la imagen.");
            }
            ImageIO.write(image, "png", outputStream);
            return outputStream.toByteArray();
        }
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
            img.scalePercent(47);
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

                Paragraph nAsientop = new Paragraph(detalles.get(i).getNumeroAsiento() + "", fontBlueBig2);
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
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, subtotald, 520, 590, 0);

                Paragraph ivad = new Paragraph("$" + decimalFormat.format(ivaTotal) + " MXN", fontBlue);
                ColumnText.showTextAligned(archivo.getDirectContent(), Element.ALIGN_LEFT, ivad, 520, 575, 0);

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
                logotipo.scaleToFit(Float.MAX_VALUE, 50);
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
