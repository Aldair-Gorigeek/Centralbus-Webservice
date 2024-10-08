package com.gorigeek.springboot.notification;

import java.io.IOException;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

@SpringBootApplication
public class FirebaseNotificationApplication {

    @Bean
    FirebaseMessaging firebaseMessaging() throws IOException {
        // Carga las credenciales desde el archivo JSON
        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(
                new ClassPathResource("centralbus-firebase.json").getInputStream());

        // Configura las opciones de Firebase con las credenciales
        FirebaseOptions firebaseOptions = FirebaseOptions.builder()
                .setCredentials(googleCredentials)
                .build();

        // Inicializa FirebaseApp con las opciones proporcionadas
        FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions);

        // Devuelve una instancia de FirebaseMessaging asociada a FirebaseApp
        return FirebaseMessaging.getInstance(app);
    }
}

