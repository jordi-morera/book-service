package com.example.bookservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Configuracion de Spring Security 6 basada en beans (SecurityFilterChain), reemplazando
 * el antiguo WebSecurityConfigurerAdapter (deprecado desde Spring Security 5.7).
 *
 * Decisiones clave:
 *  - CSRF deshabilitado por completo: la API es stateless y no sirve formularios HTML,
 *    por lo que no hay sesion de navegador que proteger (patron estandar para APIs REST;
 *    esto tambien cubre la consola H2, que usa POST sin token CSRF).
 *  - Sesion STATELESS: cada peticion se autentica de forma independiente (sin JSESSIONID),
 *    apropiado para un servicio consumido por clientes API/JWT en el futuro.
 *  - /api/v1/public/**, Swagger UI y la consola H2 quedan abiertos; todo lo demas exige
 *    autenticacion HTTP Basic. En un entorno real HTTP Basic se sustituiria por JWT.
 *  - Cabeceras: se permite frameOptions "sameOrigin" unicamente para que la consola H2
 *    (que usa <frame>) funcione; en produccion sin H2 esto se eliminaria.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/public/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/h2-console/**",
                                "/actuator/health"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Usuario en memoria para demo de HTTP Basic. En un entorno real este bean se
     * sustituiria por un UserDetailsService respaldado por base de datos, o directamente
     * por un resource server JWT (spring-boot-starter-oauth2-resource-server).
     */
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails admin = User.withUsername("admin")
                .password(encoder.encode("admin123"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(admin);
    }
}
