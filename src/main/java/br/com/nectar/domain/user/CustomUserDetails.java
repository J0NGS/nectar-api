package br.com.nectar.domain.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.nectar.domain.role.Role;
import br.com.nectar.domain.role.RoleRepository;
import br.com.nectar.domain.role.RoleService;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final User user;
    private final RoleRepository roleRepository;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Obter todas as roles usando RoleService
        Page<Role> rolesPage = roleRepository.findAll(Pageable.unpaged());
    
        if (rolesPage == null || rolesPage.isEmpty()) {
            return Collections.emptyList(); // Retorna vazio se não houver roles
        }
    
        // Mapear as roles para GrantedAuthority
        return rolesPage.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
    
    public User getUser() {
        return user;
    }

    @Override
    public String getPassword() {
        return user.getAuth().getPassword();
    }

    @Override
    public String getUsername() {
        return user.getAuth().getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
