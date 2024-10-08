package com.gorigeek.springboot.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

@Service
public class FirebaseMessagingService {

    @Autowired
    private FirebaseMessaging firebaseMessaging;

    public String sendNotificationByToken(NotificationMessage notificationMessage, String tpNoti) {
        Notification notification;

        if (tpNoti.equals("0")) {
            notification = Notification
                    .builder()
                    .setTitle(notificationMessage.getTittle())
                    .setBody(notificationMessage.getBody())
                    .setImage(notificationMessage.getImgNoti())
                    .build();
        } else {
            notification = Notification
                    .builder()
                    .setTitle(notificationMessage.getTittle())
                    .setBody(notificationMessage.getBody())
                    .build();
        }

        Aps aps = Aps.builder()
                .setSound("default")
                .build();

        ApnsConfig apnsConfig = ApnsConfig.builder()
                .setAps(aps)
                .build();

        AndroidNotification androidNofi = AndroidNotification.builder()
                .setSound("default")
                .build();

        AndroidConfig androidConfig = AndroidConfig.builder()
                .setNotification(androidNofi)
                .build();

        Message message = Message
                .builder()
                .setToken(notificationMessage.getRecippientToken())
                .setNotification(notification)
                .setApnsConfig(apnsConfig)
                .setAndroidConfig(androidConfig)
                .build();

        try {
            firebaseMessaging.send(message);

            return "Exito al enviar notificación";
        } catch (FirebaseMessagingException e) {
            // e.printStackTrace();
            return "Error al enviar notificación";
        }
    }
}
