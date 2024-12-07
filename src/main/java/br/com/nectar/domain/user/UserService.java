package br.com.nectar.domain.user;

import br.com.nectar.infrastructure.services.utils.DocumentValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User save(User user) {
        if (user.getProfile() != null && user.getProfile().getDocument() != null) {
            String document = user.getProfile().getDocument();
            if (document.length() == 11 && !new DocumentValidatorUtil().checkCpf(document)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "document is invalid!");
            } else if (document.length() == 14 && !new DocumentValidatorUtil().checkCnpj(document)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "document is invalid!");
            }
        }
        return userRepository.save(user);
    }

    public User create(User user) {
        // Business logic
        if (userRepository.getByUsername(user.getAuth().getUsername()) != null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Desculpe, já existe um usuário com este email cadastrado!"
            );
        }

        return save(user);
    }

    public Optional<User> getById(UUID uuid) {
        return userRepository.findById(uuid);
    }

    public Optional<User> getByUsername(String username) {
        return userRepository.getByUsername(username);
    }
}
