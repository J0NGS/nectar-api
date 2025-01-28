package br.com.nectar.application.user.dto;

import br.com.nectar.domain.user.UserStatus;
import lombok.Data;

@Data
public class UpdateUserStatusDTO {
    private UserStatus status;
}
