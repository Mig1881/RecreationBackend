package com.svalero.apirecreation.repository;

import com.svalero.apirecreation.domain.EventAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventAttendanceRepository extends JpaRepository<EventAttendance, Long> {
    List<EventAttendance> findByAssociationId(Long associationId);
    List<EventAttendance> findByEventId(Long eventId);
    boolean existsByAssociationIdAndEventId(Long associationId, Long eventId);
}
