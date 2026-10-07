package com.svalero.apirecreation.config;

import com.svalero.apirecreation.domain.Member;
import com.svalero.apirecreation.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;

@Slf4j
@Configuration
@Profile("dev")
public class DatabaseSeeder {

    @Bean
    public CommandLineRunner initDatabase(MemberRepository memberRepository) {
        return args -> {
            // Buscamos si el admin ya existe para no duplicarlo al reiniciar el docker-compose
            if (memberRepository.findByEmail("admin@ane.es").isEmpty()) {
                log.info("Sembrando base de datos: Creando cuenta de Administrador (Mando Central)...");

                Member admin = new Member();
                admin.setNationalId("00000000A");
                admin.setFirstName("Admin");
                admin.setLastName("Mando Central");
                admin.setPhone("+34600000000");
                admin.setAssociationPosition("Administrador del Sistema");
                admin.setHistoricalRank("Estado Mayor");
                admin.setWeaponLicense("NO-APLICA");
                admin.setBirthDate(LocalDate.of(1980, 1, 1));
                admin.setEmail("admin@ane.es");

                // Contraseña "admin123" ya encriptada con BCrypt
                admin.setPassword("$2a$12$w1iLeTC7qYYnnPAX3wKOsurEowxiYh2rCaFUSDS5.lpcozHALEqwK");
                admin.setRole("ADMIN");

                memberRepository.save(admin);
                log.info("¡Administrador creado con éxito! Ya puedes loguearte en React.");
            } else {
                log.info("La cuenta de Administrador ya existe. Omitiendo inicialización.");
            }
        };
    }
}
