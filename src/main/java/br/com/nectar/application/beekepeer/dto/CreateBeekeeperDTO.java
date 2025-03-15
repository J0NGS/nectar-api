package br.com.nectar.application.beekepeer.dto;

import br.com.nectar.domain.user.UserStatus;
import lombok.Data;
import java.time.LocalDate;

@Data
public class CreateBeekeeperDTO {
    private UserStatus status = UserStatus.ACTIVE;
    private String name;
    private String email;
    private String document;
    private String phone;
    private Boolean hasPesticides;
    private Boolean hasHiveLoss;
    private LocalDate birthDate;
    private AddressDTO address = null;
}
