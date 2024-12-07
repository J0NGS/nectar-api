package br.com.nectar.application.user.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UserRegistrationRequest
{   
    String username; 
    String password; 
    String name;
    String document;
    String phone;
    LocalDate birthDate;    
    String role;
}

