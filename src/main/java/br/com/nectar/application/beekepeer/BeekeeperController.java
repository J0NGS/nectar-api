package br.com.nectar.application.beekepeer;

import br.com.nectar.application.auth.dto.ResponseDTO;
import br.com.nectar.application.beekepeer.dto.CreateBeekeeperDTO;
import br.com.nectar.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/beekeepers")
@RequiredArgsConstructor
public class BeekeeperController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> create (
        @AuthenticationPrincipal Authentication authentication,
        @RequestBody CreateBeekeeperDTO request
    ) throws Exception {
        return ResponseEntity.ok(
            new ResponseDTO("")
        );
    }
}
