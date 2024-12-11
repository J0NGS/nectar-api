package br.com.nectar.infrastructure.config.security;


import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CustomRoleHierarchy implements RoleHierarchy {

    @Override
    public Collection<? extends GrantedAuthority> getReachableGrantedAuthorities(
            Collection<? extends GrantedAuthority> authorities) {

        Set<GrantedAuthority> reachableAuthorities = new HashSet<>(authorities);

        // Adiciona a lógica para expandir a hierarquia
        authorities.forEach(authority -> {
            if ("ROLE_ORG".equals(authority.getAuthority())) {
                reachableAuthorities.add(new SimpleGrantedAuthority("ROLE_ORG"));
                reachableAuthorities.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
            } else if ("ROLE_MANAGER".equals(authority.getAuthority())) {
                reachableAuthorities.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
            }
        });

        reachableAuthorities.forEach(auth -> System.out.println("Role alcançável: " + auth.getAuthority()));


        return reachableAuthorities;
    }
}


