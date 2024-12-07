package br.com.nectar.application.auth.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Data;

@Data
public class AuthRequestDTO {
    String username;
    String password;
    PasswordEncoder passwordEncoder;
    public AuthRequestDTO(String username, String password, PasswordEncoder passwordEncoder){
        this.username = username;
        this.passwordEncoder = passwordEncoder;
        this.password = passwordEncoder.encode(password);
    }
}
