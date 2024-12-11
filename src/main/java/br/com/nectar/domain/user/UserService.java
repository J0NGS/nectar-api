package br.com.nectar.domain.user;

import br.com.nectar.domain.manager.Manager;
import br.com.nectar.domain.manager.ManagerRepository;
import br.com.nectar.domain.manager.ManagerService;
import br.com.nectar.domain.privilege.Privilege;
import br.com.nectar.domain.privilege.PrivilegeRepository;
import br.com.nectar.infrastructure.exceptions.FrontDisplayableException;
import br.com.nectar.application.user.dto.UserResponse;
import br.com.nectar.application.user.dto.ResponseDTO;
import br.com.nectar.application.user.dto.UserInfos;
import br.com.nectar.application.user.dto.UserRegistrationRequest;
import br.com.nectar.domain.auth.Auth;
import br.com.nectar.domain.auth.AuthService;
import br.com.nectar.domain.profile.Profile;
import br.com.nectar.domain.role.Role;
import br.com.nectar.domain.role.RoleRepository;
import br.com.nectar.domain.token.JwtUtil;
import br.com.nectar.infrastructure.services.utils.DocumentValidatorUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PrivilegeRepository privilegeRepository;
    private final ManagerRepository managerRepository;
    private final AuthService authService;
    private final RoleRepository roleRepository;

    @Transactional
    public ResponseEntity<UserInfos> login(String username, String password) {
        try {
            // Realiza autenticação
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            // Gera o token JWT
            String token = jwtUtil.generateToken(username);

            // Busca o usuário e cria o DTO de informações
            User user = userRepository.findByAuthUsername(username)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Usuário não encontrado!"));

            UserInfos userInfos = new UserInfos(
                    user.getId(),
                    user.getAuth().getUsername(),
                    user.getProfile().getName(),
                    user.getRole().getName(),
                    user.getPrivileges(),
                    token);

            return ResponseEntity.ok(userInfos);

        } catch (AuthenticationException e) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Credenciais inválidas");
        }
    }

    @Transactional
    public ResponseEntity<User> save(User user) {
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
                    "Erro ao criar autenticação: " + e.getReason());
        }
        Optional<User> userSaved = Optional.ofNullable(userRepository.save(user));
        
        if (!userSaved.isPresent()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao salvar usuário!");
        }

        return new ResponseEntity<>(userSaved.get(), HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<User> create(UserRegistrationRequest user) {
        User newUser = new User();

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

        Role role = roleRepository.findByName(user.getRole())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role não encontrada!"));
        newUser.setRole(role);

        return save(newUser);
    }

    public ResponseEntity<User> getUserOrg(UUID userId) {
        Optional<Manager> manager = managerRepository.getByUserId(userId);

        if (manager.isPresent()) {
            return new ResponseEntity<>(manager.get().getOrg(), HttpStatus.OK);
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Este usuário não existe!"));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<UserResponse> addPrivilegeToUser(UUID userId, UUID privilegeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado!"));

        Privilege privilege = privilegeRepository.findById(privilegeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Privilégio não encontrado!"));

        if (!user.getPrivileges().contains(privilege)) {
            user.getPrivileges().add(privilege);
            userRepository.save(user);
        }
        
        return new ResponseEntity<> (UserResponse.fromUser(user), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<UserResponse> removePrivilegeFromUser(UUID userId, UUID privilegeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado!"));

        Privilege privilege = privilegeRepository.findById(privilegeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Privilégio não encontrado!"));

        if (user.getPrivileges().contains(privilege)) {
            user.getPrivileges().remove(privilege);
            userRepository.save(user);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Privilégio não pertence ao usuário!");
        }

        return new ResponseEntity<> (UserResponse.fromUser(user), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<UserResponse> updatePassword(UUID userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado!"));

        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A nova senha não pode ser vazia!");
        }

        user.getAuth().setPassword(newPassword);
        return new ResponseEntity<> (UserResponse.fromUser(user), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<UserResponse> updateUsername(UUID userId, String newUsername) {
        if (newUsername == null || newUsername.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O novo nome de usuário não pode ser vazio!");
        }

        if (authService.usernameExists(newUsername)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "O nome de usuário já está em uso!");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado!"));

        user.getAuth().setUsername(newUsername);
        return new ResponseEntity<> (UserResponse.fromUser(user), HttpStatus.OK);
    }

}
