package com.svalero.apirecreation.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDate;

@Data
public class MemberDTO {

    @NotBlank(message = "El DNI/Pasaporte es obligatorio")
    private String nationalId;

    @NotBlank(message = "El nombre es obligatorio")
    private String firstName;

    @NotBlank(message = "Los apellidos son obligatorios")
    private String lastName;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El formato del correo electrónico no es válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    @NotBlank(message = "El rol es obligatorio")
    private String role; // "ROLE_MEMBER", "ROLE_PRESIDENT", etc.

    // Campos opcionales
    private String phone;
    private String associationPosition;
    private String historicalRank;
    private String weaponLicense;
    private LocalDate birthDate;
    private String imageUrl;
}