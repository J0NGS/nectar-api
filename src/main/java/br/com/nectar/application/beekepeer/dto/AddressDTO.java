package br.com.nectar.application.beekepeer.dto;

import lombok.Data;

@Data
public class AddressDTO {
    private String street;
    private String number;
    private String cep;
    private String province;
    private String city;
    private String state;
}
