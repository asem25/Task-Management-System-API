package com.semavin.API.controllers;

import com.semavin.API.dtos.UserDTO;
import com.semavin.API.models.User;
import com.semavin.API.security.jwt.JwtTokenProvider;
import com.semavin.API.services.UserService;
import com.semavin.API.utils.UserFieldsErrorException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
                          UserService userService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }
    @Operation(summary = "Регистрация нового пользователя", description = "Регистрирует нового пользователя с email и паролем.")
    @Parameter(
            name = "user",
            description = "Данные пользователя",
            required = true,
            schema = @Schema(implementation = UserDTO.class)
    )
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDTO user,
                                      BindingResult bindingResult) throws UserFieldsErrorException{
        if (bindingResult.hasErrors()){
            throw new UserFieldsErrorException(bindingResult.getFieldErrors());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return ResponseEntity.ok("User registered successfully");
    }
    @Operation(summary = "Авторизация пользователя", description = "Авторизует пользователя и возвращает JWT токен.")
    @Parameter(
            name = "user",
            description = "Данные пользователя",
            required = true,
            schema = @Schema(implementation = UserDTO.class)
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserDTO user,
                                   BindingResult bindingResult) throws UserFieldsErrorException{
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        String token = jwtTokenProvider.generateToken(authentication.getName(),
                authentication.getAuthorities().iterator().next().getAuthority());
        if (bindingResult.hasErrors()){
            throw new UserFieldsErrorException(bindingResult.getFieldErrors());
        }

        return ResponseEntity.ok(token);
    }
}
