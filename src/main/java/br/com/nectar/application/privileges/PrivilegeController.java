package br.com.nectar.application.privileges;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.nectar.domain.privilege.Privilege;
import br.com.nectar.domain.privilege.PrivilegeService;

import java.util.UUID;

@RestController
@RequestMapping("nectar/api/privileges")
public class PrivilegeController {

    private final PrivilegeService privilegeService;

    public PrivilegeController(PrivilegeService privilegeService) {
        this.privilegeService = privilegeService;
    }

    @PostMapping
    public ResponseEntity<Privilege> createPrivilege(@RequestParam String name) {
        return privilegeService.createPrivilege(name);
    }

    @GetMapping
    public ResponseEntity<Page<Privilege>> getAllPrivileges(Pageable pageable) {
        return privilegeService.getAllPrivileges(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Privilege> getPrivilegeById(@PathVariable UUID id) {
        return privilegeService.getPrivilegeById(id);
    }

    @GetMapping("/search")
    public ResponseEntity<Privilege> getPrivilegeByName(@RequestParam String name) {
        return privilegeService.getPrivilegeByName(name);
    }

    @PatchMapping("/{id}/signature-revoked")
    public ResponseEntity<Privilege> updateIsSignatureRevoked(
            @PathVariable UUID id,
            @RequestParam boolean isSignatureRevoked) {
        return privilegeService.updateIsSignatureRevoked(id, isSignatureRevoked);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Privilege> updatePrivilege(
            @PathVariable UUID id,
            @RequestParam String newName) {
        return privilegeService.updatePrivilege(id, newName);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrivilege(@PathVariable UUID id) {
        return privilegeService.deletePrivilege(id);
    }
}

