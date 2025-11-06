package com.backend.is.IS_Backend.controller;

import mk.ukim.finki.it.reservengo.dto.jwtDTO.JWTAuthenticationRequestDTO;
import mk.ukim.finki.it.reservengo.dto.jwtDTO.JWTAuthenticationResponseDTO;
import mk.ukim.finki.it.reservengo.dto.jwtDTO.JWTLoginDTO;
import mk.ukim.finki.it.reservengo.service.intf.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authenticationService;

    public AuthController(AuthService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(value = "/register/customer")
    public ResponseEntity<JWTAuthenticationResponseDTO> registerCustomer(@RequestBody JWTAuthenticationRequestDTO jwtAuthenticationRequestDTO) {
        return new ResponseEntity<>(authenticationService.registerCustomer(jwtAuthenticationRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/register/local-worker")
    public ResponseEntity<JWTAuthenticationResponseDTO> registerLocalWorker(@RequestBody JWTAuthenticationRequestDTO jwtAuthenticationRequestDTO,
                                                                            @RequestHeader(value = "Invite-Token") String token) {
        return new ResponseEntity<>(authenticationService.registerLocalWorker(jwtAuthenticationRequestDTO, token), HttpStatus.CREATED);
    }

    @PostMapping("/register/local-manager")
    public ResponseEntity<JWTAuthenticationResponseDTO> registerLocalManager(@RequestBody JWTAuthenticationRequestDTO jwtAuthenticationRequestDTO,
                                                                             @RequestHeader(value = "Invite-Token") String token) {
        return new ResponseEntity<>(authenticationService.registerLocalManager(jwtAuthenticationRequestDTO, token), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JWTAuthenticationResponseDTO> login(@RequestBody JWTLoginDTO jwtLoginDTO) {
        return new ResponseEntity<>(authenticationService.login(jwtLoginDTO), HttpStatus.OK);
    }

    @PatchMapping("/enable")
    public ResponseEntity<Void> reactivateAccount(@RequestBody JWTLoginDTO jwtLoginDTO) {
        authenticationService.reactivateProfile(jwtLoginDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/register/invite/check")
    public ResponseEntity<Void> checkInvite() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}