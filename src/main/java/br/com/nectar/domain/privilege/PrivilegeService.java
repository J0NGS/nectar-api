package br.com.nectar.domain.privilege;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;

import java.util.UUID;

@Service
public class PrivilegeService {

    private final PrivilegeRepository privilegeRepository;

    public PrivilegeService(PrivilegeRepository privilegeRepository) {
        this.privilegeRepository = privilegeRepository;
    }

    @Transactional
    public ResponseEntity<Privilege> createPrivilege(String name) {
        if (privilegeRepository.existsByName(name)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Privilégio já existente com o mesmo nome!");
        }

        Privilege privilege = new Privilege();
        privilege.setName(name);

        Privilege savedPrivilege = privilegeRepository.save(privilege);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPrivilege);
    }

    // Buscar todos os privilégios com paginação
    public ResponseEntity<Page<Privilege>> getAllPrivileges(Pageable pageable) {
        Page<Privilege> privileges = privilegeRepository.findAll(pageable);
        return ResponseEntity.ok(privileges);
    }

    public ResponseEntity<Privilege> getPrivilegeById(UUID id) {
        return privilegeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Privilégio não encontrado!"));
    }

    // Buscar privilégio por nome
    public ResponseEntity<Privilege> getPrivilegeByName(String name) {
        return privilegeRepository.findByName(name)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Privilégio não encontrado!"));
    }

    // Atualizar o estado de isSignatureRevoked
    @Transactional
    public ResponseEntity<Privilege> updateIsSignatureRevoked(UUID id, boolean isSignatureRevoked) {
        return privilegeRepository.findById(id).map(privilege -> {
            privilege.setIsSignatureRevoked(isSignatureRevoked);
            Privilege updatedPrivilege = privilegeRepository.save(privilege);
            return ResponseEntity.ok(updatedPrivilege);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Privilégio não encontrado!"));
    }

    // Atualizar o nome do privilégio
    @Transactional
    public ResponseEntity<Privilege> updatePrivilege(UUID id, String newName) {
        return privilegeRepository.findById(id).map(privilege -> {
            if (!privilege.getName().equals(newName) && privilegeRepository.existsByName(newName)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um privilégio com esse nome!");
            }
            privilege.setName(newName);
            Privilege updatedPrivilege = privilegeRepository.save(privilege);
            return ResponseEntity.ok(updatedPrivilege);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Privilégio não encontrado!"));
    }

    @Transactional
    public ResponseEntity<Void> deletePrivilege(UUID id) {
        if (!privilegeRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Privilégio não encontrado!");
        }
        privilegeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
