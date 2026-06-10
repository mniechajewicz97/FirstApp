package com.example.demo.lesson.model;

import com.example.demo.student.model.Student;
import com.example.demo.teacher.model.Teacher;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
    private LocalDateTime lessonDate;

}
