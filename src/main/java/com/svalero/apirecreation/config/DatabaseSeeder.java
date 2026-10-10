package com.svalero.apirecreation.config;

import com.svalero.apirecreation.domain.Member;
import com.svalero.apirecreation.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value; // 🔥 IMPORTANTE
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Slf4j
@Configuration
//corre siempre, pero con seguridad inyectada
public class DatabaseSeeder {

    // Lee la contraseña del entorno. Si se esta en local, usa esta por defecto.
    @Value("${admin.initial.password:$2a$12$w1iLeTC7qYYnnPAX3wKOsurEowxiYh2rCaFUSDS5.lpcozHALEqwK}")
    private String adminPasswordHashed;

    @Bean
    public CommandLineRunner initDatabase(MemberRepository memberRepository) {
        return args -> {
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

                // Asignamos la contraseña inyectada
                admin.setPassword(adminPasswordHashed);
                admin.setRole("ROLE_ADMIN");

                memberRepository.save(admin);
                log.info("¡Administrador creado con éxito!");
            }
        };
    }
}