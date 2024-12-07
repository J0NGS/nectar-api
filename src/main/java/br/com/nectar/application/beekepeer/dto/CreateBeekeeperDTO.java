package br.com.nectar.application.beekepeer.dto;

import br.com.nectar.domain.address.Address;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateBeekeeperDTO {
    private String name;
    private String email;
    private String document;
    private String phone;
    private LocalDate birthDate;

    private String street = null;
    private String number = null;
    private String cep = null;
    private String province = null;
    private String city = null;
    private String state = null;
}
