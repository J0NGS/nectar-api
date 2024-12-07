package br.com.nectar.application.beekepeer.dto;

import br.com.nectar.domain.user.UserStatus;
import lombok.Data;

@Data
public class GetPageDTO {
    private Integer pageSize = 10;
    private UserStatus status = UserStatus.ACTIVE;
}
