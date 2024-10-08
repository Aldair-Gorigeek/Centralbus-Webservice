package com.gorigeek.springboot;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@Configurable
@EnableScheduling
public class CentralBusWebservicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(CentralBusWebservicesApplication.class, args);
	}

}
