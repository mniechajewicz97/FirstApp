package com.example.demo.lesson;

import com.example.demo.lesson.model.Lesson;
import com.example.demo.student.StudentRepository;
import com.example.demo.student.model.Student;
import com.example.demo.teacher.TeacherRepository;
import com.example.demo.teacher.TeacherService;
import com.example.demo.teacher.model.Teacher;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public List<Lesson> findAll() {
        return lessonRepository.findAll();
    }

    public void deleteById(long id) {
        lessonRepository.deleteById(id);
    }

    public Lesson findById(long id) {
        return lessonRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Lesson with id " + id + " not found"));

    }

    public void save(Lesson lesson, Long studentId, Long teacherId) {
       if (lesson.getLessonDate().isBefore(LocalDateTime.now())) {
           throw  new IllegalArgumentException("Lesson date is before current date");
       }

       Student student = studentRepository.findById(studentId)
               .orElseThrow(()-> new EntityNotFoundException("Student with id " + studentId + " not found"));
       Teacher teacher = teacherRepository.findById(teacherId)
               .orElseThrow(()-> new EntityNotFoundException("Teacher with id " + teacherId + " not found"));

        LocalDateTime from =  lesson.getLessonDate().minusHours(1);
        LocalDateTime to =  lesson.getLessonDate().plusHours(1);

        if(lessonRepository.existsByTeacherAndLessonDateGreaterThanAndLessonDateLessThan(teacher, from, to)) {
            throw new  IllegalStateException("Lesson with id " + lesson.getId() + " is already exists");
        }

       lesson.setStudent(student);
       lesson.setTeacher(teacher);
       lessonRepository.save(lesson);

    }

}
