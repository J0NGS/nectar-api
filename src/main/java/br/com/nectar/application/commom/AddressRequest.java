package br.com.nectar.application.commom;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddressRequest{
    String street;
    String number;
    String cep;
    String province;
    String city;
    String state;
}