package com.example.demo.common.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentDTO {
    private Long id;
    private String firstName;
    private String lastName;

}
