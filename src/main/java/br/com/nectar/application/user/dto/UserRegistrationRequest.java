package br.com.nectar.application.user.dto;

import br.com.nectar.domain.user.UserStatus;
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
    private UserStatus status = null;
    private String role = "ROLE_ORG";
}

