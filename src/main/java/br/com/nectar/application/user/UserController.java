package br.com.nectar.application.user;

import br.com.nectar.application.user.dto.AuthRequestDTO;
import br.com.nectar.application.user.dto.ResponseDTO;
import br.com.nectar.application.user.dto.UserRegistrationRequest;
import br.com.nectar.domain.token.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.nectar.domain.user.UserService;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/nectar/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(
        @RequestBody AuthRequestDTO authRequest
    ) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    authRequest.username(),
                    authRequest.password()
                )
            );

            return ResponseEntity.ok(
                new ResponseDTO(
                    jwtUtil.generateToken(authRequest.username())
                )
            );
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Credenciais inválidas"
            );
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
        @RequestBody UserRegistrationRequest user
    ) {
        return ResponseEntity.ok(
            new ResponseDTO(
                userService.create(user)
            )
        );
    }
}
