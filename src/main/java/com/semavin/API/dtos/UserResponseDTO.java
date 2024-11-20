package com.semavin.API.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.*;

@Data
@Builder
public class UserResponseDTO {
    private String email;
    private String role;
    private List<TaskResponseDTO> tasksWhereAuthors;
    private List<TaskResponseDTO> taskWhereAssignee;
}
