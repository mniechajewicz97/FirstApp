package com.example.demo.lesson;

import com.example.demo.lesson.model.Lesson;
import com.example.demo.student.StudentRepository;
import com.example.demo.student.model.Student;
import com.example.demo.teacher.TeacherRepository;
import com.example.demo.teacher.model.Teacher;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class LessonRepository {
    private final List<Lesson> lessons = new ArrayList<>();
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;


    @PostConstruct
    public void populateLessons() {
        Teacher teacher1 = teacherRepository.findById(1111);
        Student student1 = studentRepository.findById(1235);
        if (teacher1 != null && student1 != null) {
            lessons.add(new Lesson(0001, student1, teacher1, LocalDateTime.of(2026, 4, 27, 18, 00)));
        } else {
            System.out.println("Teacher or Student not found");
        }

    }

    public List<Lesson> findAll() {
        return lessons;
    }

    public void deleteById(long id) {
        lessons.removeIf(lesson -> lesson.getId() == id);
    }

    public Lesson findById(long id) {
        return lessons.stream()
                .filter(lesson -> lesson.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
