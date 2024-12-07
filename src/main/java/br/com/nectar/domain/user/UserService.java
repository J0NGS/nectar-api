package br.com.nectar.domain.user;

import br.com.nectar.domain.manager.Manager;
import br.com.nectar.domain.manager.ManagerRepository;
import br.com.nectar.domain.manager.ManagerService;
import br.com.nectar.infrastructure.exceptions.FrontDisplayableException;
import br.com.nectar.application.user.dto.UserRegistrationRequest;
import br.com.nectar.domain.auth.Auth;
import br.com.nectar.domain.auth.AuthService;
import br.com.nectar.domain.profile.Profile;
import br.com.nectar.domain.role.Role;
import br.com.nectar.domain.role.RoleRepository;
import br.com.nectar.infrastructure.services.utils.DocumentValidatorUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ManagerRepository managerRepository;
    private final AuthService authService;
    private final RoleRepository roleRepository;

    @Transactional
    public User save(User user) {
        if (user.getProfile() != null && user.getProfile().getDocument() != null) {
            String document = user.getProfile().getDocument();
            if (document.length() == 11 && !new DocumentValidatorUtil().checkCpf(document)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Documento inválido!");
            } else if (document.length() == 14 && !new DocumentValidatorUtil().checkCnpj(document)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Documento inválido!");
            }
        }
        try {
            // Auth service já faz a verificação do username
            Auth auth = authService.createAuth(user.getAuth().getUsername(), user.getAuth().getPassword()).getBody();
            user.setAuth(auth);
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Erro ao criar autenticação: " + e.getReason()
            );
        }

        return userRepository.save(user);
    }

    @Transactional
    public ResponseEntity<String> create(UserRegistrationRequest user) {
        User newUser = new User();
        try {
            Profile profile = new Profile();
            profile.setName(user.getName());
            profile.setDocument(user.getDocument());
            profile.setPhone(user.getPhone());
            profile.setBirthDate(user.getBirthDate());
            newUser.setProfile(profile);
            
            Auth authRequest = new Auth();
            authRequest.setUsername(user.getUsername());
            authRequest.setPassword(user.getPassword());
            newUser.setAuth(authRequest);

            
            Role role  = roleRepository.findById(user.getRole()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role não encontrada!"));
            newUser.setRole(role);
            save(newUser);
            return new ResponseEntity<>("Usuário registrado !", HttpStatus.OK); 
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Desculpe, erro ao cadastrar usuário!"
            );
        }
        
    }


    public User getUserOrg(UUID userId) {
        Optional<Manager> manager = managerRepository.getByUserId(userId);

        if (manager.isPresent()) {
            return manager.get().getOrg();
        }

        return userRepository.findById(userId).orElseThrow(() ->
            new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Este usuário não existe!"
            )
        );
    }
}
