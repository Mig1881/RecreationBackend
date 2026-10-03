package com.svalero.apirecreation.repository;

import com.svalero.apirecreation.domain.Association;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface AssociationRepository extends JpaRepository<Association, Long> {

    Optional<Association> findByTaxId(String taxId);

    List<Association> findByNameContainingIgnoreCase(String name);

    boolean existsByTaxId(String taxId);
}

