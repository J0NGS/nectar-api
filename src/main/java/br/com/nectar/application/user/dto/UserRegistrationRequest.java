package br.com.nectar.application.user.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class UserRegistrationRequest
{   
    String username; 
    String password; 
    String name;
    String document;
    String phone;
    LocalDate birthDate;    
    UUID role;
}

