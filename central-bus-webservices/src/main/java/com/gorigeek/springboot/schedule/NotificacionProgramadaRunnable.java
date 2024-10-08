package com.gorigeek.springboot.schedule;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

@Service
public class NotificacionProgramadaRunnable implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(NotificacionRunnable.class);

    @Autowired
    FirebaseMessagingService firebaseMessagingService;
    NotificationMessage notification = new NotificationMessage();

    private int contador = 0;
    List<T_notificaciones_push> listaNotificacion = new ArrayList<T_notificaciones_push>();

    List<T_usuarios_final> listaTodosToken = new ArrayList<T_usuarios_final>();
    List<T_usuarios_final> listaToken = new ArrayList<T_usuarios_final>();
    List<T_usuarios_final> listaTokenNU = new ArrayList<T_usuarios_final>();

    List<T_ventas_distribusion> tDistribution = new ArrayList<T_ventas_distribusion>();
    List<Detalle_venta> ventas = new ArrayList<Detalle_venta>();
    List<Detalle_venta_tour> ventasTour = new ArrayList<Detalle_venta_tour>();

    @Value("${timeRecollect}")
    private String timeRecollect;

    @Value("${leadTime}")
    private String leadTime;

    private NotificacionesRepository repoNoti;
    private TokenUsuario_NotificacionRepository repoTokenUser;
    private VentasDistribution_NotificacionRepository repoDistribution;
    private Ventas_NotificacionRepository repoVentas;
    private VentasTour_NotificacionRepository repoVentasTour;

    public NotificacionProgramadaRunnable(NotificacionesRepository repoNoti,
            TokenUsuario_NotificacionRepository repoTokenUser,
            VentasDistribution_NotificacionRepository repoDistribution,
            Ventas_NotificacionRepository repoVentas,
            VentasTour_NotificacionRepository repoVentasTour) {
        this.repoNoti = repoNoti;
        this.repoTokenUser = repoTokenUser;
        this.repoDistribution = repoDistribution;
        this.repoVentas = repoVentas;
        this.repoVentasTour = repoVentasTour;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run() {
        DateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat formatoHora = new SimpleDateFormat("HH:mm");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, Integer.parseInt("1"));

        Date date = new Date();

        try {
            if (listaNotificacion.size() > 0) {
                for (int i = 0; i < listaNotificacion.size(); i++) {
                    switch (listaNotificacion.get(i).getDirigido()) {
                        case 1:
                            String[] fechaHoraBDTU = listaNotificacion.get(i).getFechaHora().split(" ");

                            if (fechaHoraBDTU[0].equals(formatoFecha.format(date))) {
                                String horaBD = fechaHoraBDTU[1].substring(0, fechaHoraBDTU[1].length() - 3);
                                if (horaBD.equals(formatoHora.format(cal.getTime()))) {

                                    listaTodosToken = repoTokenUser.getAllTokenUser();
                                    String imgTU = listaNotificacion.get(i).getImg();

                                    for (int x = 0; x < listaTodosToken.size(); x++) {
                                        try {
                                            repoNoti.updateNotification(
                                                    listaNotificacion.get(i).getIdt_notificaciones_push());

                                            notification.setTittle(listaNotificacion.get(i).getTitulo());
                                            notification.setBody(listaNotificacion.get(i).getMensaje());
                                            notification.setImgNoti((imgTU.isEmpty()) ? "" : imgTU);
                                            notification.setRecippientToken(listaTodosToken.get(x).getToken());
                                            firebaseMessagingService.sendNotificationByToken(notification,
                                                    (imgTU.isEmpty()) ? "1" : "0");

                                        } catch (Exception e) {
                                            System.err
                                                    .println("error de envio en notificación Todos usuarios");
                                        }
                                    }
                                }
                            }

                            break;

                        case 2:
                            String[] fechaHoraBDNU = listaNotificacion.get(i).getFechaHora().split(" ");
                            String imgNU = listaNotificacion.get(i).getImg();

                            if (fechaHoraBDNU[0].equals(formatoFecha.format(date))) {
                                String horaBD = fechaHoraBDNU[1].substring(0, fechaHoraBDNU[1].length() - 3);
                                if (horaBD.equals(formatoHora.format(cal.getTime()))) {
                                    listaTokenNU = repoTokenUser.getNewUser();
                                    if (listaTokenNU.size() > 0) {
                                        for (int a = 0; a < listaTokenNU.size(); a++) {
                                            tDistribution = repoDistribution
                                                    .findByUserSaleDistribution(
                                                            listaTokenNU.get(i).getIdt_usuarios_final() + "");
                                            if (tDistribution.size() == 0) {
                                                ventas = repoVentas.findByUserSale(
                                                        listaTokenNU.get(i).getIdt_usuarios_final() + "");
                                                if (ventas.size() == 0) {
                                                    ventasTour = repoVentasTour
                                                            .findByUserTour(
                                                                    listaTokenNU.get(i).getIdt_usuarios_final() + "");
                                                    if (ventasTour.size() == 0) {
                                                        repoNoti.updateNotification(
                                                                listaNotificacion.get(i).getIdt_notificaciones_push());

                                                        try {
                                                            notification
                                                                    .setTittle(listaNotificacion.get(i).getTitulo());
                                                            notification.setBody(listaNotificacion.get(i).getMensaje());
                                                            notification.setImgNoti((imgNU.isEmpty()) ? "" : imgNU);
                                                            notification
                                                                    .setRecippientToken(listaTokenNU.get(i).getToken());
                                                            firebaseMessagingService.sendNotificationByToken(
                                                                    notification,
                                                                    (imgNU.isEmpty()) ? "1" : "0");

                                                        } catch (Exception e) {
                                                            System.err.println(
                                                                    "error de envio en notificación de nuevos usuarios");
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                }
                            }
                            break;
                        case 3:
                            String[] fechaHoraBD = listaNotificacion.get(i).getFechaHora().split(" ");
                            String imgUE = listaNotificacion.get(i).getImg();

                            if (fechaHoraBD[0].equals(formatoFecha.format(date))) {
                                String horaBD = fechaHoraBD[1].substring(0, fechaHoraBD[1].length() - 3);
                                if (horaBD.equals(formatoHora.format(cal.getTime()))) {
                                    listaToken = repoTokenUser
                                            .getTokenUser(listaNotificacion.get(i).getUsuariosFinal());

                                    for (int j = 0; j < listaToken.size(); j++) {
                                        try {
                                            repoNoti.updateNotification(
                                                    listaNotificacion.get(i).getIdt_notificaciones_push());

                                            notification.setTittle(listaNotificacion.get(i).getTitulo());
                                            notification.setBody(listaNotificacion.get(i).getMensaje());
                                            notification.setImgNoti((imgUE.isEmpty()) ? "" : imgUE);
                                            notification.setRecippientToken(listaToken.get(j).getToken());
                                            firebaseMessagingService.sendNotificationByToken(notification,
                                                    (imgUE.isEmpty()) ? "1" : "0");

                                        } catch (Exception e) {
                                            System.err
                                                    .println("error de envio en notificación a usuarios en especifico");
                                        }

                                    }

                                }
                            }
                            break;

                        default:
                            break;

                    }
                }
            } else if (contador == 0) {
                listaNotificacion = repoNoti.findNotifications();
                contador = 1;
            } else {
            }
        } catch (Exception e) {
            System.err.println("ERROR EN EL CATCH");
        }

    }

}
