package com.example.demo.common.dto;
//Data Transfer Object
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class TeacherDTO {
    private Long id;
    private String firstName;
    private String lastName;
}
