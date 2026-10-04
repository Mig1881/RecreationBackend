package com.svalero.apirecreation.service;

import com.svalero.apirecreation.domain.Event;
import com.svalero.apirecreation.domain.EventParticipation;
import com.svalero.apirecreation.exception.EventNotFoundException;
import com.svalero.apirecreation.repository.EventParticipationRepository;
import com.svalero.apirecreation.repository.EventRepository;
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

    public void exportEventParticipantsToCsv(Long eventId, PrintWriter writer) {
        //Se Comprobar que el evento existe
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Evento no encontrado con ID: " + eventId));

        //Se Obtiene la lista de participantes
        List<EventParticipation> participations = participationRepository.findByEventId(eventId);

        //Se configura el formato del CSV (Cabeceras que requiere la Guardia Civil)
        CSVFormat format = CSVFormat.EXCEL.builder()
                .setHeader("DNI/NIE", "Apellidos", "Nombre", "Licencia de Armas", "Tipo de Arma", "Modelo")
                .setDelimiter(';') // En España y Europa, Excel abre mejor el CSV si el delimitador es punto y coma
                .build();

        //Se Escriben  los datos
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