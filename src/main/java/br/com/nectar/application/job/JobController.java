package br.com.nectar.application.job;

import br.com.nectar.application.ResponseDTO;
import br.com.nectar.application.job.dto.CreateJobDTO;
import br.com.nectar.application.job.dto.GetJobPageDTO;
import br.com.nectar.domain.job.JobService;
import br.com.nectar.domain.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/nectar/api/jobs")
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;

    @PostMapping
    public ResponseEntity<?> create (
        @AuthenticationPrincipal CustomUserDetails userAuthentication,
        @RequestBody CreateJobDTO request
    ) throws Exception {
        return ResponseEntity.ok(
            new ResponseDTO<>(
                jobService.create(
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
        @RequestBody GetJobPageDTO request
    ) throws Exception {
        return ResponseEntity.ok(
            new ResponseDTO<>(
                jobService.getPage(
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
        @RequestBody GetJobPageDTO request
    ) throws Exception {
        return ResponseEntity.ok(
            new ResponseDTO<>(
                jobService.getPageByBeekeeperId(
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
        @RequestBody CreateJobDTO request
    ) throws Exception {
        return ResponseEntity.ok(
            new ResponseDTO<>(
                jobService.update(
                    Id,
                    request
                )
            )
        );
    }
}
