package br.com.nectar.infrastructure.jwt;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import br.com.nectar.domain.user.CustomUserDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtGenerator {

    @Value("${api.security.token.secret}")
    private String SECRET_KEY;

    static final long EXPIRATION_TIME = (1000 * 60 * 60 * 10); // 10 horas

    public String generateToken(Authentication authentication) {
        var userDetails = (CustomUserDetails) authentication.getPrincipal();
        var user = userDetails.getUser();
    
        var privileges = user.getPrivileges().stream()
                .map(privilege -> privilege.getName())
                .toArray(String[]::new);
    
        return Jwts.builder()
                .setSubject(user.getAuth().getUsername())
                .addClaims(
                    Map.of(
                        "UUID", user.getId(),
                        "AUTHORITIES", privileges,
                        "ROLE", user.getRole().getName(),
                        "name", user.getProfile() == null
                                ? "Usuário Indefinido"
                                : user.getProfile().getName()
                    )
                )
                .setIssuedAt(new Date(System.currentTimeMillis())) // Data de emissão
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Expira em 10 horas
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
}

