package com.backend.is.IS_Backend.service.intf;

import mk.ukim.finki.it.reservengo.dto.jwtDTO.JWTAuthenticationRequestDTO;
import mk.ukim.finki.it.reservengo.dto.jwtDTO.JWTAuthenticationResponseDTO;
import mk.ukim.finki.it.reservengo.dto.jwtDTO.JWTLoginDTO;

public interface AuthService {
    JWTAuthenticationResponseDTO registerCustomer(JWTAuthenticationRequestDTO jwtAuthenticationRequestDTO);

    JWTAuthenticationResponseDTO registerLocalWorker(JWTAuthenticationRequestDTO jwtAuthenticationRequestDTO, String token);

    JWTAuthenticationResponseDTO registerLocalManager(JWTAuthenticationRequestDTO jwtAuthenticationRequestDTO, String token);

    JWTAuthenticationResponseDTO login(JWTLoginDTO jwtLoginDTO);

    void reactivateProfile(JWTLoginDTO jwtLoginDTO);
}
