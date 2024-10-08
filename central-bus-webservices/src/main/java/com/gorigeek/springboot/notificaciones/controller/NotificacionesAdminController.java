package com.gorigeek.springboot.notificaciones.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gorigeek.springboot.Module;
import com.gorigeek.springboot.entity.DatosNotificacion;
import com.gorigeek.springboot.notificaciones.entity.Detalle_venta;
import com.gorigeek.springboot.notificaciones.entity.Detalle_venta_tour;
import com.gorigeek.springboot.notificaciones.entity.T_notificaciones_push;
import com.gorigeek.springboot.notificaciones.entity.T_usuarios_final;
import com.gorigeek.springboot.notificaciones.entity.T_ventas_distribusion;
import com.gorigeek.springboot.notificaciones.repository.NotificacionesRepository;
import com.gorigeek.springboot.notificaciones.repository.TokenUsuario_NotificacionRepository;
import com.gorigeek.springboot.notificaciones.repository.VentasDistribution_NotificacionRepository;
import com.gorigeek.springboot.notificaciones.repository.VentasTour_NotificacionRepository;
import com.gorigeek.springboot.notificaciones.repository.Ventas_NotificacionRepository;
import com.gorigeek.springboot.notification.FirebaseMessagingService;
import com.gorigeek.springboot.notification.NotificationMessage;
import com.gorigeek.springboot.schedule.NotificacionRunnable;
import com.gorigeek.springboot.util.LoggerUtils;

@RestController

public class NotificacionesAdminController {
    private static final Logger logger = LoggerFactory.getLogger(NotificacionesAdminController.class);

    private String titulo = "";
    private String mensaje = "";
    private String img = "";
    private String url = "";
    private String usuariosFinal = "";

    List<T_notificaciones_push> listaNotificacion = new ArrayList<T_notificaciones_push>();
    List<T_usuarios_final> listaToken = new ArrayList<T_usuarios_final>();
    List<T_usuarios_final> listaTodosToken = new ArrayList<T_usuarios_final>();
    List<T_ventas_distribusion> tDistribution = new ArrayList<T_ventas_distribusion>();
    List<Detalle_venta> ventas = new ArrayList<Detalle_venta>();
    List<Detalle_venta_tour> ventasTour = new ArrayList<Detalle_venta_tour>();

    @Autowired
    private NotificacionesRepository repo;

    @Autowired
    private TokenUsuario_NotificacionRepository repoToken;

    @Autowired
    private VentasDistribution_NotificacionRepository repoDistribution;

    @Autowired
    private Ventas_NotificacionRepository repoVentas;

    @Autowired
    private VentasTour_NotificacionRepository repoVentasTour;

    @Autowired
    FirebaseMessagingService firebaseMessagingService;

    @Module("Notificaciones - Envio de notificaciones push")
    @PostMapping("/enviarNotificacionv1")
    public String updateContrasena(@Param("idNoti") String idNoti) {

        NotificationMessage notification = new NotificationMessage();

        List<DatosNotificacion> listaNotificacionesNuevosUsuarios;

        String json = "";

        try {
            listaNotificacion = repo.findByIdNoti(Integer.parseInt(idNoti));
            if (listaNotificacion.size() > 0) {

                titulo = listaNotificacion.get(0).getTitulo();
                mensaje = listaNotificacion.get(0).getMensaje();
                img = listaNotificacion.get(0).getImg();
                url = listaNotificacion.get(0).getUrl();
                usuariosFinal = listaNotificacion.get(0).getUsuariosFinal();

                switch (listaNotificacion.get(0).getDirigido()) {
                    case 1:
                        json = "true";

                        CompletableFuture.runAsync(() -> {
                            listaTodosToken = repoToken.getAllTokenUser();

                            for (int x = 0; x < listaTodosToken.size(); x++) {
                                try {

                                    notification.setTittle(titulo);
                                    notification.setBody(mensaje);
                                    notification.setImgNoti((img.isEmpty()) ? "" : img);
                                    notification.setRecippientToken(listaTodosToken.get(x).getToken());
                                    firebaseMessagingService.sendNotificationByToken(notification,
                                            (img.isEmpty()) ? "1" : "0");

                                } catch (Exception e) {
                                    System.err
                                            .println("error de envio en notificación todos usuarios");
                                }
                            }
                            repo.updateNotification(Integer.parseInt(idNoti));

                            logger.info("El envío de notificaciones push para todos los usuarios ha sido completado.");

                        });
                        break;

                    case 2:
                        CompletableFuture.runAsync(() -> {
                            titulo = listaNotificacion.get(0).getTitulo();

                            listaToken = repoToken.getNewUser();

                            if (listaToken.size() > 0) {
                                for (int i = 0; i < listaToken.size(); i++) {
                                    tDistribution = repoDistribution
                                            .findByUserSaleDistribution(listaToken.get(i).getIdt_usuarios_final() + "");
                                    if (tDistribution.size() == 0) {
                                        ventas = repoVentas
                                                .findByUserSale(listaToken.get(i).getIdt_usuarios_final() + "");
                                        if (ventas.size() == 0) {
                                            ventasTour = repoVentasTour
                                                    .findByUserTour(listaToken.get(i).getIdt_usuarios_final() + "");
                                            if (ventasTour.size() == 0) {
                                                // Aqui se envia la notificación
                                                try {
                                                    notification.setTittle(titulo);
                                                    notification.setBody(mensaje);
                                                    notification.setImgNoti((img.isEmpty()) ? "" : img);
                                                    notification.setRecippientToken(listaToken.get(i).getToken());
                                                    firebaseMessagingService.sendNotificationByToken(notification,
                                                            (img.isEmpty()) ? "1" : "0");
                                                    // System.out.println("Envio con exito");

                                                } catch (Exception e) {
                                                    System.err.println("error");
                                                }
                                            }
                                        }
                                    }
                                }
                                repo.updateNotification(Integer.parseInt(idNoti));
                                logger.info(
                                        "El envío de notificaciones push para los usuarios nuevos ha sido completado.");

                            }
                        });
                        json = "true";

                        break;

                    case 3:
                        json = "true";
                        listaToken = repoToken.getTokenUser(usuariosFinal);

                        for (int j = 0; j < listaToken.size(); j++) {
                            try {
                                notification.setTittle(titulo);
                                notification.setBody(mensaje);
                                notification.setImgNoti((img.isEmpty()) ? "" : img);
                                notification.setRecippientToken(listaToken.get(j).getToken());
                                firebaseMessagingService.sendNotificationByToken(notification,
                                        (img.isEmpty()) ? "1" : "0");

                            } catch (Exception e) {
                                System.err.println("error");
                            }

                        }
                        repo.updateNotification(Integer.parseInt(idNoti));

                        logger.info("El envío de notificaciones push para usuarios en especifico ha sido completado.");

                        break;

                    default:
                        break;

                }

            } else {
                json = "No se encontró ninguna notificación en la bd";
            }

        } catch (Exception e) {
            // TODO: handle exception
            System.err.println("Error: " + e.getMessage());
            json = (idNoti.isEmpty()) ? "Datos vacios" : "Error API";

        }
        return json;
    }

}
