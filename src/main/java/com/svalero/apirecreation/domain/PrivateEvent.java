package com.svalero.apirecreation.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "private_events")
@PrimaryKeyJoinColumn(name = "event_id")
public class PrivateEvent extends Event {

    @Column(length = 100)
    private String sponsor;

    @Column(name = "initial_budget")
    private BigDecimal initialBudget;

    @Column(name = "open_to_public")
    private Boolean openToPublic = false;
}
