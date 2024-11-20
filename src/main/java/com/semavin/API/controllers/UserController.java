package com.semavin.API.controllers;

import com.semavin.API.dtos.UserResponseDTO;
import com.semavin.API.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> showAll(){
        return ResponseEntity.ok(userService.showAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> showById(@PathVariable Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin){
            return ResponseEntity.ok(userService.findUserResponseByEmail(authentication.getName()));
        }
        return ResponseEntity.ok(userService.findUserResponseDTOById(id));
    }
}
