package com.gorigeek.springboot.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gorigeek.springboot.entity.DatosNotificacion;
import com.gorigeek.springboot.entity.DetalleNotificacionMovil;
import com.gorigeek.springboot.entity.DetalleNotificacionMovilTour;
import com.gorigeek.springboot.entity.DetalleVentaMovil;
import com.gorigeek.springboot.entity.ListNotify;
import com.gorigeek.springboot.notification.FirebaseMessagingService;
import com.gorigeek.springboot.notification.NotificationMessage;
import com.gorigeek.springboot.repository.DetalleNotificacionRepository;
import com.gorigeek.springboot.repository.DetalleNotificacionTourRepository;
import com.gorigeek.springboot.repository.DetalleVentaRepository;
import com.gorigeek.springboot.util.LoggerUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class NotificacionRunnable implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(NotificacionRunnable.class);
    
    @Autowired
    FirebaseMessagingService firebaseMessagingService;

    List<DetalleNotificacionMovil> listaNotificacion= new ArrayList<DetalleNotificacionMovil>();
    List<DetalleNotificacionMovilTour> listaNotificacionTour = new ArrayList<DetalleNotificacionMovilTour>();;

    @Value("${timeRecollect}")
    private String timeRecollect;
    
    @Value("${leadTime}")
    private String leadTime;
    private DetalleNotificacionRepository detalleNotificacionRepository;
    private DetalleNotificacionTourRepository detalleNotificacionTourRepository;
   
   public NotificacionRunnable(DetalleNotificacionRepository detalleNotificacionRepository, DetalleNotificacionTourRepository detalleNotificacionTourRepository ) {
       this.detalleNotificacionRepository = detalleNotificacionRepository;
       this.detalleNotificacionTourRepository = detalleNotificacionTourRepository;
   }
    
    @SuppressWarnings("unchecked")
    @Override
    public void run() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, Integer.parseInt(leadTime));

        if(timeRecollect.equals(dateFormat.format(date))) {
            ListaNotificaciones.getInstance().getArrayList().clear();

            listaNotificacion= detalleNotificacionRepository.findByStatus(dateFormat2.format(date));
            listaNotificacionTour =  detalleNotificacionTourRepository.findNotificationTour(dateFormat2.format(date));
                        
            if(listaNotificacion.size()>0) {
                for (int a = 0; a < listaNotificacion.size(); a++) { 
                    DatosNotificacion listTemp = new  DatosNotificacion();
                    System.out.println("Fecha BD: "+listaNotificacion.get(a).getFechaViaje());
                    listTemp.setFechaViaje(listaNotificacion.get(a).getFechaViaje());
                    listTemp.setToken(listaNotificacion.get(a).getUsuarioFinal().getToken());
                    listTemp.setCiudadDestino(listaNotificacion.get(a).getVentaViajes().getTerminalDestino().getCiudad().getDescripcion());
                    listTemp.setEstadoDestino(listaNotificacion.get(a).getVentaViajes().getTerminalDestino().getEstados().getDescripcion());
                    ListaNotificaciones.getInstance().getArrayList().add(listTemp);
                }
            }            
            if(listaNotificacionTour.size()>0) {
                for (int d = 0; d < listaNotificacionTour.size(); d++) {
                    DatosNotificacion listTemp = new  DatosNotificacion();
                    listTemp.setFechaViaje(listaNotificacionTour.get(d).getFechaViaje());
                    listTemp.setToken(listaNotificacionTour.get(d).getUsuarioFinal().getToken());
                    listTemp.setCiudadDestino(listaNotificacionTour.get(d).getVentaViajes().getTour().getCiudadesDestino().getDescripcion());
                    listTemp.setEstadoDestino(listaNotificacionTour.get(d).getVentaViajes().getTour().getEstadoDestino().getDescripcion());
                    ListaNotificaciones.getInstance().getArrayList().add(listTemp);
                }
            }    

        }
        else {
      
            
          if(ListaNotificaciones.getInstance().getArrayList().size()>0) {
            for (int i = 0; i<ListaNotificaciones.getInstance().getArrayList().size();i++) {
                NotificationMessage notification = new NotificationMessage();
                String[] partirFechayHora = ListaNotificaciones.getInstance().getArrayList().get(i).getFechaViaje().split(" ");
                String parteFecha = partirFechayHora[0];
                String parteHora = partirFechayHora[1];
                String hora=parteHora.substring(0,parteHora.length()-3);
              
                if(hora.equals(timeFormat.format(cal.getTime()))) {
                    LoggerUtils.logSchedule(logger, "Toca enviar notificación: ", "" + " - " + hora);
                    String[] partirFecha = parteFecha.split("-");
                    String anio = partirFecha[0];
                    String mesNumero = partirFecha[1];
                    String mes="";
                    String dia = partirFecha[2];
                    switch(mesNumero){
                        case "01":
                            mes="enero";
                            break;
                        case "02":
                            mes="febrero";
                            break;
                        case "03":
                            mes="marzo";
                            break;
                        case "04":
                            mes="abril";
                            break;
                        case "05":
                            mes="mayo";
                            break;
                        case "06":
                            mes="junio";
                            break;
                        case "07":
                            mes="julio";
                            break;
                        case "08":
                            mes="agosto";
                            break;
                        case "09":
                            mes="septiembre";
                            break;
                        case "10":
                            mes="octubre";
                            break;
                        case "11":
                            mes="noviembre";
                            break;
                        case "12":
                            mes="diciembre";
                            break;
                    }
                    String ciudad=ListaNotificaciones.getInstance().getArrayList().get(i).getCiudadDestino();
                    String estado=ListaNotificaciones.getInstance().getArrayList().get(i).getEstadoDestino();
                    if(ListaNotificaciones.getInstance().getArrayList().get(i).getToken()!=null) {
                        if(ListaNotificaciones.getInstance().getArrayList().get(i).getToken().length()>=162) {
                            LoggerUtils.logSchedule(logger, "Se enviará la notificación ", ""+"");
                            try {
                                notification.setTittle("¿Estás listo para tu viaje?");
                                notification.setBody("Recuerda el viaje que tienes para el día "+dia+" de "+mes+" "+anio+" con destino a "+ciudad+", "+estado+" a las "+hora+"hrs.");
                                notification.setRecippientToken(ListaNotificaciones.getInstance().getArrayList().get(i).getToken());                            
                                firebaseMessagingService.sendNotificationByToken(notification,"1");
                                ListaNotificaciones.getInstance().getArrayList().remove(i);
                                LoggerUtils.logSchedule(logger, "Se envió correctamente la notificación. ", "OK");
                            } catch (Exception e) {
                                System.err.println(e.getMessage());
                            }
                       
                        }
                    }
                }
                else {
                    //Aún no se notifica
                }
            }
        }}
        //LoggerUtils.logSchedule(logger, "0 0 * * *", "Termina Task enviar la notirifacion");            
    }

}
