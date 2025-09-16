package org.kshitij.authservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.kshitij.authservice.dto.LoginRequestDto;
import org.kshitij.authservice.dto.LoginResponseDto;
import org.kshitij.authservice.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @Operation(summary = "Generate token on user login")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @RequestBody LoginRequestDto loginRequestDto
            ) {
        Optional<String> token = authService.authenticate(loginRequestDto);

        if(token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String t = token.get();
        return ResponseEntity.ok(new LoginResponseDto(t));
    }

    @Operation(summary ="Endpoint for validation of the token")
    @GetMapping("/validate")
    public ResponseEntity<Void> validateToken(
            @RequestHeader("Authorization") String authHeader) {
        if(authHeader ==null || !authHeader.startsWith("Bearer ")) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return authService.validateToken(authHeader.substring(7))
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
