package com.sheva.todolist.auth;

import com.sheva.todolist.config.JwtService;
import com.sheva.todolist.model.AuthenticationRequest;
import com.sheva.todolist.model.AuthenticationResponse;
import com.sheva.todolist.model.RegisterRequest;
import com.sheva.todolist.entity.User;
import com.sheva.todolist.repository.UserRepository;
import com.sheva.todolist.role.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    //    Mengakses dan mengelola entity user pada database
    private final UserRepository repository;

    //    Enkripsi password
    private final PasswordEncoder passwordEncoder;

    //    Deklarasi JwtService
    private final JwtService jwtService;

    //    Deklarasi authentication credential user
    private  final AuthenticationManager authenticationManager;

    //    Method register
    public AuthenticationResponse register(RegisterRequest request) {
//        mengatur atribut dan enkripsi password user
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
//        simpan ke database
        repository.save(user);
//        generate token jwt
        var jwtToken = jwtService.generateToken(user);
//        return AuthenticationResponse dan token jwt
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    //    Method authenticate
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
//        authentication credentials by request email dan password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
//        mencari user di database
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
//        generate token jwt
        var jwtToken = jwtService.generateToken(user);
//        return AuthenticationResponse dan token jwt
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

}
