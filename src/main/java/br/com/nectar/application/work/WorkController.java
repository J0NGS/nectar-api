package br.com.nectar.application.work;

import br.com.nectar.application.commom.ResponseDTO;
import br.com.nectar.application.work.dto.CreateWorkDTO;
import br.com.nectar.application.work.dto.GetWorkPageDTO;
import br.com.nectar.domain.work.WorkService;
import br.com.nectar.domain.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/nectar/api/jobs")
@RequiredArgsConstructor
public class WorkController {
    private final WorkService workService;

    @PostMapping
    public ResponseEntity<?> create (
        @AuthenticationPrincipal CustomUserDetails userAuthentication,
        @RequestBody CreateWorkDTO request
    ) throws Exception {
        return ResponseEntity.ok(
            new ResponseDTO<>(
                workService.create(
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
        @RequestBody GetWorkPageDTO request
    ) throws Exception {
        return ResponseEntity.ok(
            new ResponseDTO<>(
                workService.getPage(
                    userAuthentication.getUser(),
                    page,
                    request
                )
            )
        );
    }

    @PostMapping("/beekeeper/{beekeeperId}/page/{page}")
    public ResponseEntity<?> getBeekeeperPage (
        @AuthenticationPrincipal CustomUserDetails userAuthentication,
        @PathVariable("page") Integer page,
        @PathVariable("beekeeperId") UUID beekeeperId,
        @RequestBody GetWorkPageDTO request
    ) throws Exception {
        return ResponseEntity.ok(
            new ResponseDTO<>(
                workService.getPageByBeekeeperId(
                    beekeeperId,
                    userAuthentication.getUser(),
                    page,
                    request
                )
            )
        );
    }

    @PutMapping("/{Id}")
    public ResponseEntity<?> update (
        @AuthenticationPrincipal CustomUserDetails userAuthentication,
        @PathVariable("Id") UUID Id,
        @RequestBody CreateWorkDTO request
    ) throws Exception {
        return ResponseEntity.ok(
            new ResponseDTO<>(
                workService.update(
                    Id,
                    request
                )
            )
        );
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<?> getById (
        @PathVariable("jobId") UUID jobId
    ) throws Exception {
        return ResponseEntity.ok(
            new ResponseDTO<>(
                workService.getById(jobId)
            )
        );
    }
}
