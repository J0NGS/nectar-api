package br.com.nectar.application.beekepeer;

import br.com.nectar.application.user.dto.ResponseDTO;
import br.com.nectar.application.beekepeer.dto.CreateBeekeeperDTO;
import br.com.nectar.application.beekepeer.dto.GetPageDTO;
import br.com.nectar.domain.beekeeper.BeekeeperService;
import br.com.nectar.domain.job.JobService;
import br.com.nectar.domain.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/nectar/api/beekeepers")
@RequiredArgsConstructor
public class BeekeeperController {
    private final BeekeeperService beekeeperService;
    private final JobService jobService;

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

    @PostMapping("/page/{page}")
    public ResponseEntity<?> getPage (
        @AuthenticationPrincipal CustomUserDetails userAuthentication,
        @PathVariable("page") Integer page,
        @RequestBody GetPageDTO request
    ) throws Exception {
        return ResponseEntity.ok(
            new ResponseDTO(
                beekeeperService.getPage(
                    userAuthentication.getUser(),
                    page,
                    request
                )
            )
        );
    }

    @PutMapping("/{beekeeperId}")
    public ResponseEntity<?> update (
        @AuthenticationPrincipal CustomUserDetails userAuthentication,
        @PathVariable("beekeeperId") UUID beekeeperId,
        @RequestBody CreateBeekeeperDTO request
    ) throws Exception {
        return ResponseEntity.ok(
            new ResponseDTO(
                beekeeperService.update(
                    beekeeperId,
                    request
                )
            )
        );
    }

    @GetMapping
    public ResponseEntity<?> getAll(
        @AuthenticationPrincipal CustomUserDetails userAuthentication
    ) throws Exception {
        return ResponseEntity.ok(
            new ResponseDTO(
                beekeeperService.getAllActiveByUser(
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
