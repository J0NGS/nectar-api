package br.com.nectar.application.user.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class UserRegistrationRequest {
    private String username;
    private String password;
    private String name;
    private String document;
    private String phone;
    private LocalDate birthDate = null;
    private String role = "ROLE_ORG";
}

