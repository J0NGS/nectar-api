package br.com.nectar.application.user.dto;

import java.util.List;
import java.util.UUID;

import br.com.nectar.domain.privilege.Privilege;
import br.com.nectar.domain.profile.Profile;
import br.com.nectar.domain.user.User;

public record UserResponse(UUID id, String username, Profile profile, String role, List<String> privileges) {
        public static UserResponse fromUser(User user) {
        return new UserResponse(
            user.getId(),
            user.getAuth().getUsername(),
            user.getProfile(),
            user.getRole().getName(),
            user.getPrivileges().stream()
                .map(Privilege::getName)
                .toList()
        );
    }
}
