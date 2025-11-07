package com.backend.is.IS_Backend.controller;

import com.backend.is.IS_Backend.dto.auth.AuthRequestDTO;
import com.backend.is.IS_Backend.dto.auth.AuthResponseDTO;
import com.backend.is.IS_Backend.dto.auth.LoginDTO;
import com.backend.is.IS_Backend.service.intf.AuthServiceKt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthServiceKt authenticationService;

    public AuthController(AuthServiceKt authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody AuthRequestDTO request) {
        return new ResponseEntity<>(authenticationService.register(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO request) {
        return new ResponseEntity<>(authenticationService.login(request), HttpStatus.OK);
    }
}