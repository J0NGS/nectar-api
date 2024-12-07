package br.com.nectar.domain.role;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.com.nectar.domain.privilege.Privilege;
import br.com.nectar.domain.privilege.PrivilegeService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final PrivilegeService privilegeService;

    public RoleService(RoleRepository roleRepository, PrivilegeService privilegeService) {
        this.roleRepository = roleRepository;
        this.privilegeService = privilegeService;
    }

    // Criar uma Role com ou sem privilégios
    @Transactional
    public ResponseEntity<Role> createRole(String name, List<UUID> privilegeIds) {
        if (roleRepository.existsByName(name)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Role já existe com este nome!");
        }

        Role role = new Role();
        role.setName(name);

        if (privilegeIds != null && !privilegeIds.isEmpty()) {
            // Valida e busca os privilégios
            List<Privilege> privileges = privilegeIds.stream()
                    .map(privilegeService::getPrivilegeById)
                    .map(ResponseEntity::getBody)
                    .collect(Collectors.toList());
            role.setPrivileges(privileges);
        }

        Role savedRole = roleRepository.save(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRole);
    }

    public ResponseEntity<Page<Role>> getAllRoles(Pageable pageable) {
        Page<Role> roles = roleRepository.findAll(pageable);
        return ResponseEntity.ok(roles);
    }

    public ResponseEntity<Role> getRoleById(UUID roleId) {
        return roleRepository.findById(roleId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role não encontrada!"));
    }

    // Adicionar privilégios a uma Role
    @Transactional
    public ResponseEntity<Role> addPrivilegesToRole(UUID roleId, List<UUID> privilegeIds) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role não encontrada!"));

        // Adicionar privilégios que ainda não estão associados
        privilegeIds.forEach(privilegeId -> {
            if (!roleRepository.isPrivilegeAssignedToRole(roleId, privilegeId)) {
                roleRepository.addPrivilegeToRole(roleId, privilegeId);
            }
        });


        // Atualiza a entidade com os privilégios adicionados
        List<Privilege> updatedPrivileges = new ArrayList<>(
                privilegeService.getAllPrivileges(Pageable.unpaged()).getBody().stream()
                        .filter(privilege -> privilegeIds.contains(privilege.getId())) // Filtrando os privilégios para adicionar somento os que existem
                        .toList());
        role.getPrivileges().addAll(updatedPrivileges); // Salvando filtragem

        return ResponseEntity.ok(role);
    }

    // Remover privilégios de uma Role
    @Transactional
    public ResponseEntity<Role> removePrivilegesFromRole(UUID roleId, List<UUID> privilegeIds) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role não encontrada!"));
    
        // Remover privilégios que estão associados à role
        privilegeIds.forEach(privilegeId -> {
            if (roleRepository.isPrivilegeAssignedToRole(roleId, privilegeId)) {
                roleRepository.removePrivilegeFromRole(roleId, privilegeId);
            }
        });
    
        // Atualiza a entidade com os privilégios removidos
        List<Privilege> updatedPrivileges = role.getPrivileges().stream()
                .filter(privilege -> !privilegeIds.contains(privilege.getId()))
                .toList();
    
        role.setPrivileges(updatedPrivileges);
    
        return ResponseEntity.ok(role);
    }

    @Transactional
    public ResponseEntity<Role> updateRoleName(UUID roleId, String newName) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role não encontrada!"));

        if (roleRepository.existsByName(newName)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Role já existe com este nome!");
        }

        role.setName(newName);
        Role updatedRole = roleRepository.save(role);

        return ResponseEntity.ok(updatedRole);
    }

    @Transactional
    public ResponseEntity<Void> deleteRole(UUID roleId) {
        if (!roleRepository.existsById(roleId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role não encontrada!");
        }
        roleRepository.deleteById(roleId);
        return ResponseEntity.noContent().build();
    }
}
