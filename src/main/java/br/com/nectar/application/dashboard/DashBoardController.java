package br.com.nectar.application.dashboard;

import br.com.nectar.application.ResponseDTO;
import br.com.nectar.application.dashboard.dto.GetDashJobPageDTO;
import br.com.nectar.domain.job.JobService;
import br.com.nectar.domain.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/nectar/api/dashboard")
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

    @GetMapping("/monthly/graph")
    public ResponseEntity<?> monthlyGraph (
        @AuthenticationPrincipal CustomUserDetails userAuthentication,
        @RequestParam("month") String month
    ) {
        return ResponseEntity.ok(
            new ResponseDTO(
                jobService.getMonthlyGraph(
                    userAuthentication.getUser(),
                        LocalDate.parse(month)
                )
            )
        );
    }

    @GetMapping("/monthly/board")
    public ResponseEntity<?> monthlyBoard (
            @AuthenticationPrincipal CustomUserDetails userAuthentication,
            @RequestParam("month") String month
    ) {
        return ResponseEntity.ok(
            new ResponseDTO(
                jobService.getMonthlyBoard(
                    userAuthentication.getUser(),
                    LocalDate.parse(month)
                )
            )
        );
    }
}
