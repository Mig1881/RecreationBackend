package com.svalero.apirecreation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@Slf4j
@SpringBootApplication
public class ApirecreationApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApirecreationApplication.class, args);
	}

	// Este método se ejecutará automáticamente justo cuando Tomcat termine de levantar
	@EventListener(ApplicationReadyEvent.class)
	public void onStartup() {
		log.info("=========================================================");
		log.info("🛡️ API RECREACIÓN HISTÓRICA INICIADA CON ÉXITO");
		log.info("📍 URL Base: http://localhost:8081/api");
		log.info("🔒 Estado de Seguridad: JWT Stateless Activado");
		log.info("=========================================================");
	}
}
