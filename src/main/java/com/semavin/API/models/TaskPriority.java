package com.semavin.API.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "task_priorities")
public class TaskPriority {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "name")
    private String name;
}
