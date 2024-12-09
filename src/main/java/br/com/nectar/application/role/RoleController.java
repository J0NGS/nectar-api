package br.com.nectar.application.role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.nectar.domain.role.Role;
import br.com.nectar.domain.role.RoleService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/nectar/api/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    // Criar Role
    @PostMapping
    public ResponseEntity<Role> createRole(@RequestParam String name, @RequestBody(required = false) List<UUID> privilegeIds) {
        return roleService.createRole(name, privilegeIds);
    }

    // Buscar todas as Roles com paginação
    @GetMapping
    public ResponseEntity<Page<Role>> getAllRoles(Pageable pageable) {
        return roleService.getAllRoles(pageable);
    }

    // Buscar Role por ID
    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable UUID id) {
        return roleService.getRoleById(id);
    }

    // Adicionar privilégios a uma Role
    @PostMapping("/{id}/privileges")
    public ResponseEntity<Role> addPrivilegesToRole(@PathVariable UUID id, @RequestBody List<UUID> privilegeIds) {
        return roleService.addPrivilegesToRole(id, privilegeIds);
    }

    // Remover privilégios de uma Role
    @DeleteMapping("/{id}/privileges")
    public ResponseEntity<Role> removePrivilegesFromRole(@PathVariable UUID id, @RequestBody List<UUID> privilegeIds) {
        return roleService.removePrivilegesFromRole(id, privilegeIds);
    }

    // Atualizar nome de uma Role
    @PutMapping("/{id}/name")
    public ResponseEntity<Role> updateRoleName(@PathVariable UUID id, @RequestParam String newName) {
        return roleService.updateRoleName(id, newName);
    }

    // Deletar Role
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable UUID id) {
        return roleService.deleteRole(id);
    }
}
