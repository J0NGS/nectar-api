package br.com.nectar.application.manager.dto;

import br.com.nectar.application.beekepeer.dto.AddressDTO;
import br.com.nectar.domain.user.UserStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateManagerDTO {
    private UserStatus status = UserStatus.ACTIVE;
    private String name;
    private String email;
    private String document;
    private String phone;
    private String password;
    private LocalDate birthDate;
    private AddressDTO address = null;
}
