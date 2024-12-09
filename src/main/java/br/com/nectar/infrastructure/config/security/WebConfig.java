package br.com.nectar.infrastructure.config.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import br.com.nectar.domain.token.JwtAuthenticationFilter;

import java.util.List;

@Configuration
public class WebConfig {
    // Rotas padrão
    public static final String USERS = "/nectar/api/users/";
    public static final String ROLES = "/nectar/api/roles/";
    public static final String MANAGERS = "/nectar/api/managers/";
    public static final String JOBS = "/nectar/api/jobs/";
    public static final String DASHBOARD = "/nectar/api/dashboard/";
    public static final String BEEKEPEERS = "/nectar/api/beekepeers/";

    // Roles
    public static final String ORG = "ORG";
    public static final String MANAGER = "MANAGER";

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public WebConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(ROLES + "**").permitAll()
                .requestMatchers(USERS + "**").permitAll()
                .requestMatchers(MANAGERS + "**").permitAll()
                .requestMatchers(JOBS + "**").permitAll()
                .requestMatchers(DASHBOARD + "**").permitAll()
                .requestMatchers(BEEKEPEERS + "**").permitAll()

                .anyRequest().authenticated())
            .sessionManagement(sessionManagement -> sessionManagement
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*")); // Permite todas as origens;
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Aplica as regras a todos os endpoints
        return source;
    }
}
