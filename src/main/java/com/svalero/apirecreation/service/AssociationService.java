package com.svalero.apirecreation.service;

import com.svalero.apirecreation.domain.Association;
import com.svalero.apirecreation.exception.AssociationNotFoundException;
import com.svalero.apirecreation.repository.AssociationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssociationService {

    @Autowired
    private AssociationRepository associationRepository;

    // Obtener todas las asociaciones
    public List<Association> findAll() {
        return associationRepository.findAll();
    }

    // Obtener una asociación por ID
    public Association findById(Long id) {
        return associationRepository.findById(id)
                .orElseThrow(() -> new AssociationNotFoundException("Asociación no encontrada con ID: " + id));
    }

    // Crear una nueva asociación
    public Association save(Association association) {
        // Aquí podríamos añadir lógica extra en el futuro (ej. validar que el CIF no exista)
        return associationRepository.save(association);
    }

    // Actualizar una asociación existente
    public Association update(Long id, Association associationDetails) {
        Association existingAssociation = findById(id);

        existingAssociation.setTaxId(associationDetails.getTaxId());
        existingAssociation.setName(associationDetails.getName());
        existingAssociation.setFoundationYear(associationDetails.getFoundationYear());
        existingAssociation.setAllegiance(associationDetails.getAllegiance());
        existingAssociation.setHistoricalAttire(associationDetails.getHistoricalAttire());
        existingAssociation.setLogoBase64(associationDetails.getLogoBase64());

        return associationRepository.save(existingAssociation);
    }

    // Eliminar una asociación
    public void delete(Long id) {
        Association existingAssociation = findById(id);
        associationRepository.delete(existingAssociation);
    }
}
