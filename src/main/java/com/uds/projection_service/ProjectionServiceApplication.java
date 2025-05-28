package com.uds.projection_service;

import java.time.LocalDateTime;
import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class ProjectionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectionServiceApplication.class, args);
		System.out.println("La date et l'heure ="+LocalDateTime.now());
	}

	@PostConstruct
	public void started() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
}

}
