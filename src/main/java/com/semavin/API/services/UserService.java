package com.semavin.API.services;

import com.semavin.API.dtos.UserDTO;
import com.semavin.API.dtos.UserResponseDTO;
import com.semavin.API.repositories.UserRepository;
import com.semavin.API.utils.TaskUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.semavin.API.models.User;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
@Service
@Transactional
public class UserService {
    private UserRepository userRepository;
    private RoleService roleService;
    private TaskUtils taskUtils;
    @Autowired
    public UserService(UserRepository userRepository, RoleService roleService, TaskUtils taskUtils) {
        this.roleService = roleService;
        this.userRepository = userRepository;
        this.taskUtils = taskUtils;
    }
    // TODO обработка исключения на уровне сервиса
    public User findUserByEmail(String email){
        return (userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException(
                "User not found")));
    }
    public UserResponseDTO findUserResponseByEmail(String email){
        return convertToDTO(userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException(
                "User not found")));
    }
    public UserResponseDTO findUserResponseDTOById(Long id){
        return convertToDTO(userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found")));
    }
    public void save(UserDTO userDTO){
        userRepository.save(convertToUser(userDTO));
    }
    public User findUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Not found user"));
    }
    public List<UserResponseDTO> showAll(){
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }
    private User convertToUser(UserDTO userDTO){
        return User.builder()
                .password(userDTO.getPassword())
                .role(roleService.findById(2))
                .email(userDTO.getEmail())
                .createdAt(LocalDateTime.now())
                .build();
    }
    private UserResponseDTO convertToDTO(User user){
        return UserResponseDTO.builder()
                .email(user.getEmail())
                .role(user.getRole().getName())
                .tasksWhereAuthors(taskUtils.findTasksByAuthor(user.getId()))
                .taskWhereAssignee(taskUtils.findTasksByAssignee(user.getId()))
                .build();
    }
}
