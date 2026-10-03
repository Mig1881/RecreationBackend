package com.svalero.apirecreation.repository;

import com.svalero.apirecreation.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // Buscar un evento por su código de negocio único
    Optional<Event> findByEventCode(String eventCode);

    // Buscar solo los eventos que ya han sido publicados por el Admin (útil para el frontend)
    List<Event> findByPublishedTrue();

    // Buscar todos los eventos organizados por una asociación en concreto
    List<Event> findByOrganizingAssociationId(Long associationId);

    boolean existsByEventCode(String eventCode);
}