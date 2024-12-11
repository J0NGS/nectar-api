package br.com.nectar.domain.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if ("ROLE_ORG".equals(this.user.getRole().getName())) {
            return List.of(new SimpleGrantedAuthority("ROLE_ORG"), new SimpleGrantedAuthority("ROLE_MANAGER"));
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_MANAGER"));
        }
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

    public String getName() {
        return user.getProfile().getName();
    }
    
    public String getRole() {
        return user.getRole().getName();
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
