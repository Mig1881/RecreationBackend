package com.svalero.apirecreation.service;

import com.svalero.apirecreation.domain.Association;
import com.svalero.apirecreation.domain.Event;
import com.svalero.apirecreation.domain.EventAttendance;
import com.svalero.apirecreation.domain.dto.EventAttendanceDTO;
import com.svalero.apirecreation.exception.DuplicateEnrollmentException;
import com.svalero.apirecreation.repository.EventAttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventAttendanceService {

    @Autowired
    private EventAttendanceRepository attendanceRepository;
    @Autowired
    private AssociationService associationService;
    @Autowired
    private EventService eventService;

    public List<EventAttendance> findAll() {
        return attendanceRepository.findAll();
    }

    public EventAttendance registerAttendance(EventAttendanceDTO dto) {
        if (attendanceRepository.existsByAssociationIdAndEventId(dto.getAssociationId(), dto.getEventId())) {
            throw new DuplicateEnrollmentException("La asociación ya está registrada como asistente a este evento.");
        }

        Association association = associationService.findById(dto.getAssociationId());
        Event event = eventService.findById(dto.getEventId());

        EventAttendance attendance = new EventAttendance();
        attendance.setAssociation(association);
        attendance.setEvent(event);

        return attendanceRepository.save(attendance);
    }

    public void delete(Long id) {
        attendanceRepository.deleteById(id);
    }
}
