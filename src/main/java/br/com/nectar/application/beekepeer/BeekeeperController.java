package br.com.nectar.application.beekepeer;

import br.com.nectar.application.auth.dto.ResponseDTO;
import br.com.nectar.application.beekepeer.dto.CreateBeekeeperDTO;
import br.com.nectar.domain.beekeeper.BeekeeperService;
import br.com.nectar.domain.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/beekeepers")
@RequiredArgsConstructor
public class BeekeeperController {
    private final BeekeeperService beekeeperService;

    @PostMapping
    public ResponseEntity<?> create (
        @AuthenticationPrincipal CustomUserDetails userAuthentication,
        @RequestBody CreateBeekeeperDTO request
    ) throws Exception {
        return ResponseEntity.ok(
            new ResponseDTO(
                beekeeperService.create(
                    request,
                    userAuthentication.getUser()
                )
            )
        );
    }

    @GetMapping("/{beekeeperId}")
    public ResponseEntity<?> getById (
        @AuthenticationPrincipal CustomUserDetails userAuthentication,
        @PathVariable("beekeeperId") UUID beekeeperId
    ) throws Exception {
        return ResponseEntity.ok(
            new ResponseDTO(
                beekeeperService.getById(beekeeperId)
            )
        );
    }
}
