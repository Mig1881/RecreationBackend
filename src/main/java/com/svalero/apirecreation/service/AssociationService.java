package com.svalero.apirecreation.service;

import com.svalero.apirecreation.domain.Association;
import com.svalero.apirecreation.domain.dto.AssociationDTO;
import com.svalero.apirecreation.exception.AssociationNotFoundException;
import com.svalero.apirecreation.repository.AssociationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AssociationService {

    @Autowired
    private AssociationRepository associationRepository;

    public List<Association> findAll() {
        return associationRepository.findAll();
    }

    public Association findById(Long id) {
        return associationRepository.findById(id)
                .orElseThrow(() -> new AssociationNotFoundException("Asociación no encontrada con ID: " + id));
    }

    //Recibe el DTO y lo mapea a Entidad
    public Association save(AssociationDTO dto) {
        Association association = new Association();
        association.setTaxId(dto.getTaxId());
        association.setName(dto.getName());
        association.setFoundationYear(dto.getFoundationYear());
        association.setCity(dto.getCity());
        association.setAllegiance(dto.getAllegiance());
        association.setHistoricalAttire(dto.getHistoricalAttire());
        association.setLogoBase64(dto.getLogoBase64());

        return associationRepository.save(association);
    }

    //Recibe el DTO y actualiza la Entidad
    public Association update(Long id, AssociationDTO dto) {
        Association existingAssociation = findById(id);

        existingAssociation.setTaxId(dto.getTaxId());
        existingAssociation.setName(dto.getName());
        existingAssociation.setFoundationYear(dto.getFoundationYear());
        existingAssociation.setCity(dto.getCity());
        existingAssociation.setAllegiance(dto.getAllegiance());
        existingAssociation.setHistoricalAttire(dto.getHistoricalAttire());
        existingAssociation.setLogoBase64(dto.getLogoBase64());

        return associationRepository.save(existingAssociation);
    }

    public void delete(Long id) {
        Association existingAssociation = findById(id);
        associationRepository.delete(existingAssociation);
    }
}