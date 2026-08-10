package com.example.demo.student.model.command;

import com.example.demo.common.Language;
import com.example.demo.student.model.Student;
import lombok.Data;

@Data
public class CreateStudentCommand {
    private String firstName;
    private String lastName;
    private Language language;
    private Long teacherId;

    public Student toEntity() {
        return Student.builder()
                .firstName(firstName)
                .lastName(lastName)
                .language(language)
                .build();
    }

}
