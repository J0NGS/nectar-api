package br.com.nectar.application.user;

import br.com.nectar.application.user.dto.AuthRequestDTO;
import br.com.nectar.application.user.dto.ResponseDTO;
import br.com.nectar.application.user.dto.UserInfos;
import br.com.nectar.application.user.dto.UserRegistrationRequest;
import br.com.nectar.domain.token.JwtUtil;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.nectar.domain.user.User;
import br.com.nectar.domain.user.UserService;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/nectar/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDTO authRequest) {
        return userService.login(authRequest.username(), authRequest.password());
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegistrationRequest user) {
        return ResponseEntity.ok( new ResponseDTO( userService.create(user)));
    }

    @PatchMapping("/{userId}/update-password")
    public ResponseEntity<?> updatePassword(@PathVariable UUID userId, @RequestParam String newPassword) {
        return ResponseEntity.ok(userService.updatePassword(userId, newPassword));
    }

    @PatchMapping("/{userId}/update-username")
    public ResponseEntity<?> updateUsername(@PathVariable UUID userId, @RequestParam String newUsername) {
        return ResponseEntity.ok(userService.updateUsername(userId, newUsername));
    }

    @PatchMapping("/{userId}/privileges/{privilegeId}/add")
    public ResponseEntity<?> addPrivilegeToUser(@PathVariable UUID userId, @PathVariable UUID privilegeId) {
        return ResponseEntity.ok(userService.addPrivilegeToUser(userId, privilegeId));
    }

    @PatchMapping("/{userId}/privileges/{privilegeId}/remove")
    public ResponseEntity<?> removePrivilegeFromUser(@PathVariable UUID userId,  @PathVariable UUID privilegeId) {
        return ResponseEntity.ok(userService.removePrivilegeFromUser(userId, privilegeId));
    }

    @GetMapping("/{userId}/org")
    public ResponseEntity<?> getUserOrg(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.getUserOrg(userId));
    }

    @PostMapping
    public ResponseEntity<?> saveUser(@RequestBody User user) {
        return userService.save(user);
    }
}
