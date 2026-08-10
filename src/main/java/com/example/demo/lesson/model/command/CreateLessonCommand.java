package com.example.demo.lesson.model.command;

import com.example.demo.lesson.model.Lesson;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateLessonCommand {
    private Long studentId;
    private Long teacherId;
    private LocalDateTime lessonDate;

    public Lesson toEntity() {
        return Lesson.builder()
                .lessonDate(lessonDate)
                .build();
    }
}
