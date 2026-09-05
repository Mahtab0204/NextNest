package com.kgm.nextnest.config;

import com.kgm.nextnest.security.JwtAuthenticationEntryPoint;
import com.kgm.nextnest.security.JwtAuthenticationFilter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint authenticationEntryPoint;

    private final JwtAuthenticationFilter authenticationFilter;


     // Password Encoder

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


     // Authentication Manager
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }


     // Security Filter Chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http

                // Disable CSRF
                .csrf(csrf -> csrf.disable())

                // Exception Handling
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(authenticationEntryPoint)
                )

                // Stateless JWT Authentication
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // URL Authorization
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/api/auth/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        .requestMatchers("/api/admin/**")
                        .hasRole("ADMIN")


                        .requestMatchers("/api/owner/**")
                        .hasRole("OWNER")


                        .requestMatchers("/api/customer/**")
                        .hasRole("CUSTOMER")

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/apartments/**"
                        ).hasRole("OWNER")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/apartments/**"
                        ).hasRole("OWNER")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/apartments/**"
                        ).hasRole("OWNER")

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/apartments/**"
                        ).permitAll()

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/inquiries"
                        ).hasRole("CUSTOMER")

                        .requestMatchers(
                                "/api/inquiries/my"
                        ).hasRole("CUSTOMER")

                        .requestMatchers(
                                "/api/inquiries/owner"
                        ).hasRole("OWNER")

                        .requestMatchers(
                                "/uploads/**"
                        ).permitAll()

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/locations/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/locations/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/locations/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/locations/**"
                        ).permitAll()

                        .requestMatchers(
                                "/api/users/**"
                        ).permitAll()
                        .requestMatchers(
                                "/api/messages/**"
                        ).authenticated()
                        .requestMatchers(
                                "/api/notifications/**"
                        ).permitAll()

                        .requestMatchers(
                                "/api/wishlist/**"
                        ).hasRole("CUSTOMER")
                        .requestMatchers(
                                "/swagger-ui/**").permitAll()
                        .requestMatchers(
                                "/v3/api-docs/**").permitAll()

                        // Everything Else
                        .anyRequest()
                        .authenticated()

                )

                .httpBasic(Customizer.withDefaults());

        // Register JWT Filter
        http.addFilterBefore(
                authenticationFilter,
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }
}