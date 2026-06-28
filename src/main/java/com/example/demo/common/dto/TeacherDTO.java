package com.example.demo.common.dto;
//Data Transfer Object
import com.example.demo.common.Language;
import com.example.demo.teacher.model.Teacher;
import lombok.*;

import java.util.Set;

@Data // zawiera wszystko, tylko i wylacznie do obiekow, ktore nie sa encjami
@Builder

public class TeacherDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private Set<Language> languages;

    public static TeacherDTO from(Teacher teacher) {
        return new TeacherDTO(teacher.getId(),
                teacher.getFirstName(),
                teacher.getLastName(),
                teacher.getLanguages());
    }
    public static Teacher to(TeacherDTO teacherDTO) {
        return Teacher.builder()
                .id(teacherDTO.getId())
                .firstName(teacherDTO.getFirstName())
                .lastName(teacherDTO.getLastName())
                .languages(teacherDTO.getLanguages())
                .build();
    }
}
