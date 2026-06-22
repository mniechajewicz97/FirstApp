package com.example.demo.common.dto;

import com.example.demo.student.model.Student;
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

    public static StudentDTO from(Student student) {
        return new StudentDTO(student.getId(),
                student.getFirstName(),
                student.getLastName());
    }

    public  static Student to(StudentDTO studentDTO) {
        return Student.builder()
                .id(studentDTO.getId())
                .firstName(studentDTO.getFirstName())
                .lastName(studentDTO.getLastName())
                .build();


    }

}
