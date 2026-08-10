package com.example.demo.teacher.model.command;

import com.example.demo.common.Language;
import com.example.demo.teacher.model.Teacher;
import lombok.Data;

import java.util.Set;

@Data
public class CreateTeacherCommand {
    private String firstName;
    private String lastName;
    private Set<Language> languages;

    public Teacher toEntity() {
        return Teacher.builder()
                .firstName(firstName)
                .lastName(lastName)
                .languages(languages)
                .deleted(false)
                .build();
    }
}
