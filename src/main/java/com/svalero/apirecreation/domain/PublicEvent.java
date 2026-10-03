package com.svalero.apirecreation.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true) // Necesario para que Lombok compare también los campos de la clase padre
@Entity
@Table(name = "public_events")
@PrimaryKeyJoinColumn(name = "event_id") // Le indica a JPA cuál es la FK hacia la tabla events
public class PublicEvent extends Event {

    @Column(name = "public_entity", length = 100)
    private String publicEntity;

    private BigDecimal subsidy;
}
