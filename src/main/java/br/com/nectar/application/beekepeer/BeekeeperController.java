package br.com.nectar.application.beekepeer;

import br.com.nectar.application.auth.dto.ResponseDTO;
import br.com.nectar.application.beekepeer.dto.CreateBeekeeperDTO;
import br.com.nectar.domain.beekeeper.BeekeeperService;
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
        @AuthenticationPrincipal Authentication authentication,
        @RequestBody CreateBeekeeperDTO request
    ) throws Exception {
        return ResponseEntity.ok(
            new ResponseDTO(
                beekeeperService.create(request)
            )
        );
    }

    @GetMapping("/{beekeeperId}")
    public ResponseEntity<?> create (
        @AuthenticationPrincipal Authentication authentication,
        @PathVariable("beekeeperId") UUID beekeeperId
    ) throws Exception {
        return ResponseEntity.ok(
            new ResponseDTO(
                beekeeperService.getById(beekeeperId)
            )
        );
    }
}
