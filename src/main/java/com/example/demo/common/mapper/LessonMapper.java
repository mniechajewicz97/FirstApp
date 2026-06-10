package com.example.demo.common.mapper;

import com.example.demo.common.dto.LessonDTO;
import com.example.demo.lesson.model.Lesson;


public class LessonMapper {
    public static LessonDTO mapToDTO(Lesson lesson) {
        return new LessonDTO(lesson.getId(),
                lesson.getStudent().getFirstName()+ " " + lesson.getStudent().getLastName(),
                lesson.getTeacher().getFirstName()+ " " + lesson.getTeacher().getLastName(),
                lesson.getLessonDate());
    }
}
