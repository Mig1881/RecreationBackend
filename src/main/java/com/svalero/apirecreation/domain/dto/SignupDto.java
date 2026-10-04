package com.svalero.apirecreation.domain.dto;
import lombok.Data;
import java.time.LocalDate;

@Data
public class SignupDto {
    private String nationalId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private LocalDate birthDate;
}
