package com.sheva.todolist.config;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        Menonaktifkan proteksi CSRF (Cross-Site Request Forgery)
        http
                .csrf()
                .disable()
//                konfigurasi authorize request HTTP
                .authorizeHttpRequests()
//                Mengizinkan semua permintaan ke endpoint yang dimulai dengan /api/v1/auth/ tanpa autentikasi
                .requestMatchers("/api/v1/auth/**")
                .permitAll()
//                autentikasi untuk semua request lainnya
                .anyRequest()
                .authenticated()
//                konfigurasi manajemen sesi sebagai stateless
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
//                Mengatur penyedia autentikasi yang akan digunakan oleh Spring Security
                .authenticationProvider(authenticationProvider)
//                memastikan  JWT diproses sebelum Spring mengautentikasi request username dan password.
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
