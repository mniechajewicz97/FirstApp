package com.example.demo.common.mapper;

import com.example.demo.common.dto.TeacherDTO;
import com.example.demo.teacher.model.Teacher;

public class TeacherMapper {

    public static TeacherDTO mapToDto(Teacher teacher) {
        return new TeacherDTO(teacher.getId(), teacher.getFirstName(), teacher.getLastName());
    }
    public static Teacher mapToEntity(TeacherDTO teacherDTO) {
        return Teacher.builder()
                .id(teacherDTO.getId())
                .firstName(teacherDTO.getFirstName())
                .lastName(teacherDTO.getLastName())
                .build();
    }
}
