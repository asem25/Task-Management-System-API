package com.semavin.API.services;

import com.semavin.API.dtos.UserDTO;
import com.semavin.API.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import com.semavin.API.models.User;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
@Service
@Transactional
public class UserService {
    private UserRepository userRepository;
    private RoleService roleService;
    @Autowired
    public UserService(UserRepository userRepository, RoleService roleService) {
        this.roleService = roleService;
        this.userRepository = userRepository;
    }
    // TODO обработка исключения на уровне сервиса
    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }
    public void save(UserDTO userDTO){
        userRepository.save(convertToUser(userDTO));
    }
    public User findById(Long id){
        return userRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Not found user"));
    }
    private User convertToUser(UserDTO userDTO){
        return User.builder()
                .password(userDTO.getPassword())
                .role(roleService.findById(2))
                .email(userDTO.getEmail())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
