package com.semavin.API.services;

import com.semavin.API.models.Role;
import com.semavin.API.repositories.RoleRepository;
import com.semavin.API.utils.exceptions.RoleNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class RoleService {
    private final RoleRepository roleRepository;
    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findByName(String nameRole) {
        Optional<Role> role = roleRepository.findByNameIgnoreCase(nameRole);
        return role.orElseThrow(() -> new RoleNotFoundException("Role not found: " + nameRole));
    }
    @Transactional
    public Role findById(Integer id){
        printAllRoles();
        Optional<Role> role = roleRepository.findById(Long.valueOf(id));
        return role.orElseThrow(() -> new RoleNotFoundException("Role not found: " + id));
    }
    public void printAllRoles() {
        List<Role> roles = roleRepository.findAll();
        roles.forEach(System.out::println);
    }

}
