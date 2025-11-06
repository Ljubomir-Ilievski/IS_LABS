package com.backend.is.IS_Backend.service.impl;

import mk.ukim.finki.it.reservengo.dto.jwtDTO.JWTAuthenticationRequestDTO;
import mk.ukim.finki.it.reservengo.dto.jwtDTO.JWTAuthenticationResponseDTO;
import mk.ukim.finki.it.reservengo.dto.jwtDTO.JWTLoginDTO;
import mk.ukim.finki.it.reservengo.model.domain.Customer;
import mk.ukim.finki.it.reservengo.model.domain.LocalManager;
import mk.ukim.finki.it.reservengo.model.domain.LocalWorker;
import mk.ukim.finki.it.reservengo.model.domain.User;
import mk.ukim.finki.it.reservengo.model.exceptions.EmailNotFoundException;
import mk.ukim.finki.it.reservengo.repository.UserRepository;
import mk.ukim.finki.it.reservengo.service.intf.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final CustomerService customerService;
    private final LocalWorkerService localWorkerService;
    private final LocalManagerService localManagerService;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserService userService;

    public AuthServiceImpl(CustomerService customerService, LocalWorkerService localWorkerService, LocalManagerService localManagerService, JWTService jwtService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, UserRepository userRepository, UserService userService) {
        this.customerService = customerService;
        this.localWorkerService = localWorkerService;
        this.localManagerService = localManagerService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public JWTAuthenticationResponseDTO registerCustomer(JWTAuthenticationRequestDTO jwtAuthenticationRequestDTO) {
        Customer customer = jwtAuthenticationRequestDTO.toCustomer(passwordEncoder.encode(jwtAuthenticationRequestDTO.password()));

        customerService.save(customer);
        String jwt = jwtService.generateToken(customer);

        return JWTAuthenticationResponseDTO.fromUser(customer, jwt);
    }

    @Override
    public JWTAuthenticationResponseDTO registerLocalWorker(JWTAuthenticationRequestDTO jwtAuthenticationRequestDTO, String token) {
        validateTokenEmail(token, jwtAuthenticationRequestDTO.email());

        LocalWorker localWorker = jwtAuthenticationRequestDTO.toLocalWorker(passwordEncoder.encode(jwtAuthenticationRequestDTO.password()));

        localWorkerService.save(localWorker);
        String jwt = jwtService.generateToken(localWorker);

        return JWTAuthenticationResponseDTO.fromUser(localWorker, jwt);
    }

    @Override
    public JWTAuthenticationResponseDTO registerLocalManager(JWTAuthenticationRequestDTO jwtAuthenticationRequestDTO, String token) {
        validateTokenEmail(token, jwtAuthenticationRequestDTO.email());

        LocalManager localManager = jwtAuthenticationRequestDTO.toLocalManager(passwordEncoder.encode(jwtAuthenticationRequestDTO.password()));

        localManagerService.save(localManager);
        String jwt = jwtService.generateToken(localManager);

        return JWTAuthenticationResponseDTO.fromUser(localManager, jwt);
    }

    @Override
    public JWTAuthenticationResponseDTO login(JWTLoginDTO jwtLoginDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtLoginDTO.email(), jwtLoginDTO.password()));

        User user = userRepository.findByEmail(jwtLoginDTO.email()).orElseThrow(() -> new EmailNotFoundException(jwtLoginDTO.email()));
        String jwt = jwtService.generateToken(user);

        userService.updateUserActivity(user.getId());

        return JWTAuthenticationResponseDTO.fromUser(user, jwt);
    }

    @Override
    public void reactivateProfile(JWTLoginDTO jwtLoginDTO) {
        User user = userService.findUserByEmail(jwtLoginDTO.email());

        if (user == null) {
            throw new EmailNotFoundException(jwtLoginDTO.email());
        }

        if (!passwordEncoder.matches(jwtLoginDTO.password(), user.getPassword())) {
            throw new BadCredentialsException("Bad credentials.") ;
        }

        if (!user.isEnabled()) {
            userService.enableProfile(user.getId());
        }
    }

    private void validateTokenEmail(String token, String email) {
        String tokenEmail = jwtService.extractClaim(token, claims -> claims.get("email", String.class));
        if (!tokenEmail.equals(email)) {
            throw new IllegalArgumentException("Email must match the token's email");
        }
    }
}