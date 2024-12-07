package br.com.nectar.domain.beekeeper;

import br.com.nectar.domain.profile.Profile;
import br.com.nectar.domain.user.User;
import br.com.nectar.domain.user.UserStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "beekeepers")
@Getter
@Setter
public class Beekeeper {
    @Id
    private UUID id = UUID.randomUUID();
    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.ACTIVE;
    private String email = null;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private Profile profile;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt = LocalDateTime.now();
}
