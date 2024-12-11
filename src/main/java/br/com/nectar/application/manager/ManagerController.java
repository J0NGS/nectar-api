package br.com.nectar.application.manager;

import br.com.nectar.application.ResponseDTO;
import br.com.nectar.application.beekepeer.dto.GetPageDTO;
import br.com.nectar.application.manager.dto.CreateManagerDTO;
import br.com.nectar.domain.manager.ManagerService;
import br.com.nectar.domain.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/nectar/api/managers")
@RequiredArgsConstructor
public class ManagerController {
    private final ManagerService managerService;

    @PostMapping
    public ResponseEntity<?> create (
        @AuthenticationPrincipal CustomUserDetails userAuthentication,
        @RequestBody CreateManagerDTO request
    ) throws Exception {
        return ResponseEntity.ok(
            new ResponseDTO(
                managerService.create(
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
                managerService.getPage(
                    userAuthentication.getUser(),
                    page,
                    request
                )
            )
        );
    }

    @PutMapping("/{managerId}")
    public ResponseEntity<?> update (
            @AuthenticationPrincipal CustomUserDetails userAuthentication,
            @PathVariable("managerId") UUID managerId,
            @RequestBody CreateManagerDTO request
    ) throws Exception {
        return ResponseEntity.ok(
            new ResponseDTO(
                managerService.update(
                    managerId,
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
                managerService.getAllActive(
                    userAuthentication.getUser()
                )
            )
        );
    }

    @GetMapping("/{managerId}")
    public ResponseEntity<?> getById (
            @AuthenticationPrincipal CustomUserDetails userAuthentication,
            @PathVariable("managerId") UUID managerId
    ) throws Exception {
        return ResponseEntity.ok(
            new ResponseDTO(
                managerService.getById(managerId)
            )
        );
    }
}
