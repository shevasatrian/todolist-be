package com.sheva.todolist.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
// memeriksa dan memvalidasi setiap request http
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    //    deklarasi JwtService
    private final JwtService jwtService;

    //    deklarasi UserDetailService
    private final UserDetailsService userDetailsService;

    //    method memeriksa dan validasi jwt dari request http
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
//        mengambil nilai header Authorization dari request http
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
//        cek header tidak ada dan tidak dimulai dari Baerer
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
//        mengambil token jwt dengan menghapus prefix baerer
        jwt = authHeader.substring(7);
//        extract username dari token jwt
        userEmail = jwtService.extractUsername(jwt);
//        cek userEmail tidak null dan authentication belum diatur
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            load userDetails berdasarkan userEmail
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
//            cek validasi token jwt
            if (jwtService.isTokenValid(jwt, userDetails)) {
//                membuat token authentication berdasarkan userDetails
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
//                set detail tambahan (IP address and browser type) digunakan untuk logging atau tindakan keamanan tambahan
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
//                mengatur dan memberitahu Spring Security bahwa user authentication success
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
//        memproses request dan response
        filterChain.doFilter(request, response);
    }
}
