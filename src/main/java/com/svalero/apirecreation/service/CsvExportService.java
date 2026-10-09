package com.svalero.apirecreation.service;

import com.svalero.apirecreation.domain.Event;
import com.svalero.apirecreation.domain.EventParticipation;
import com.svalero.apirecreation.domain.Member;
import com.svalero.apirecreation.exception.EventNotFoundException;
import com.svalero.apirecreation.repository.EventParticipationRepository;
import com.svalero.apirecreation.repository.EventRepository;
import com.svalero.apirecreation.repository.MemberRepository;
import com.svalero.apirecreation.repository.MembershipRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Service
public class CsvExportService {

    @Autowired
    private EventParticipationRepository participationRepository;

    @Autowired
    private EventRepository eventRepository;

    // 🔥 Inyectamos los repositorios necesarios para validar la seguridad
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MembershipRepository membershipRepository;

    public void exportEventParticipantsToCsv(Long eventId, PrintWriter writer, String userEmail, boolean isAdmin) {

        // 1. Comprobar que el evento existe
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Evento no encontrado con ID: " + eventId));

        // 2. VALIDACIÓN DE PROPIEDAD (Evitar que otro Presidente robe tus datos)
        if (!isAdmin) {
            // A. Buscamos al usuario que está haciendo la petición usando su email
            Member currentUser = memberRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("Usuario logueado no encontrado en el sistema."));

            // B. Sacamos la ID de la asociación que organiza el evento
            Long organizingAssociationId = event.getOrganizingAssociation().getId();

            // C. Comprobamos si el usuario actual pertenece a esa asociación organizadora
            boolean isPresidentOfOrganizingAssoc = membershipRepository.existsByAssociationIdAndMemberId(
                    organizingAssociationId,
                    currentUser.getId()
            );

            if (!isPresidentOfOrganizingAssoc) {
                throw new RuntimeException("Acceso Denegado: Solo el Mando Central o el Presidente de la asociación organizadora pueden exportar este listado.");
            }
        }

        // 3. Se Obtiene la lista de TODOS los participantes (sean de tu asociación o invitados)
        List<EventParticipation> participations = participationRepository.findByEventId(eventId);

        // 4. Se configura el formato del CSV para la Guardia Civil
        CSVFormat format = CSVFormat.EXCEL.builder()
                .setHeader("DNI/NIE", "Apellidos", "Nombre", "Licencia de Armas", "Tipo de Arma", "Modelo")
                .setDelimiter(';')
                .build();

        // 5. Se Escriben los datos
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, format)) {
            for (EventParticipation p : participations) {
                csvPrinter.printRecord(
                        p.getMember().getNationalId(),
                        p.getMember().getLastName(),
                        p.getMember().getFirstName(),
                        p.getMember().getWeaponLicense() != null ? p.getMember().getWeaponLicense() : "Sin Licencia",
                        p.getCarriedWeapon(),
                        p.getWeaponModel()
                );
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al generar el archivo CSV", e);
        }
    }
}