package com.semavin.API.models;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "task_statuses")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskStatus {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "name")
    private String name;
}
