package com.example.demo.lesson.model;

import com.example.demo.student.model.Student;
import com.example.demo.teacher.model.Teacher;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Lesson {
    private long id;
    private Student student;
    private Teacher teacher;
    private LocalDateTime lessonDate;

}
