package com.svalero.apirecreation.domain.dto;

import lombok.Data;

@Data
public class EventParticipationDTO {
    private Long eventId;
    private Long memberId;
    private String carriedWeapon;
    private String weaponModel;
}
