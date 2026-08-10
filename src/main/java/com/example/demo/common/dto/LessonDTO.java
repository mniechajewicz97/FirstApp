package com.example.demo.common.dto;

import com.example.demo.lesson.model.Lesson;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class LessonDTO {
    private Long id;
    private StudentDTO student;
    private TeacherDTO teacher;
    private LocalDateTime lessonDate;

public static LessonDTO from(Lesson lesson){
    return new LessonDTO(lesson.getId(),
            StudentDTO.from(lesson.getStudent()),
            TeacherDTO.from(lesson.getTeacher()),
            lesson.getLessonDate());

}

}
