package br.com.nectar.application.beekepeer.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class CreateBeekeeperDTO {
    private String name;
    private String email;
    private String document;
    private String phone;
    private LocalDate birthDate;
    private AddressDTO address = null;
}
