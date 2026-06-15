package com.example.demo.common.dto;
//Data Transfer Object
import com.example.demo.teacher.model.Teacher;
import lombok.*;

@Data // zawiera wszystko, tylko i wylacznie do obiekow, ktore nie sa encjami
@Builder

public class TeacherDTO {
    private Long id;
    private String firstName;
    private String lastName;

    public static TeacherDTO from(Teacher teacher) {
        return new TeacherDTO(teacher.getId(),
                teacher.getFirstName(),
                teacher.getLastName());
    }
    public static Teacher to(TeacherDTO teacherDTO) {
        return Teacher.builder()
                .id(teacherDTO.getId())
                .firstName(teacherDTO.getFirstName())
                .lastName(teacherDTO.getLastName())
                .build();
    }
}
