package br.com.nectar.domain.token;
import br.com.nectar.domain.user.CustomUserDetails;
import io.jsonwebtoken.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${api.security.token.secret}")
    private String SECRET_KEY;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(Authentication authentication) {
        var userDetails = (CustomUserDetails) authentication.getPrincipal();

        var user = userDetails.getUser();

        var privileges = user.getPrivileges().stream()
                .map(privilege -> privilege.getName())
                .toArray(String[]::new);

        return Jwts.builder()
                .setSubject(user.getAuth().getUsername())
                .setClaims(
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
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Expira em 10 horas
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) 
                .compact();
    }

    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        boolean valid = extractedUsername.equals(username) && !isTokenExpired(token);
    
        System.out.println("Token Validation:");
        System.out.println("Extracted Username: " + extractedUsername);
        System.out.println("Token Valid: " + valid);
    
        return valid;
    }
    

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
