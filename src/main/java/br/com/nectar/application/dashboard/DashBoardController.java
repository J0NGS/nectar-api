package br.com.nectar.application.dashboard;

import br.com.nectar.application.auth.dto.ResponseDTO;
import br.com.nectar.application.dashboard.dto.GetDashJobPageDTO;
import br.com.nectar.domain.job.JobService;
import br.com.nectar.domain.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashBoardController {
    private final JobService jobService;

    @PostMapping("/page/{page}")
    public ResponseEntity<?> getJobsPage (
        @AuthenticationPrincipal CustomUserDetails userAuthentication,
        @PathVariable("page") Integer page,
        @RequestBody GetDashJobPageDTO request
    ) throws Exception {
        return ResponseEntity.ok(
            new ResponseDTO(
                jobService.getPageForDash(
                    userAuthentication.getUser(),
                    page,
                    request
                )
            )
        );
    }
}
