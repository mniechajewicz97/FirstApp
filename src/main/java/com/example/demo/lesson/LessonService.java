package com.example.demo.lesson;

import com.example.demo.common.LessonConflictException;
import com.example.demo.common.LessonDateInPastException;
import com.example.demo.lesson.model.Lesson;
import com.example.demo.student.StudentRepository;
import com.example.demo.student.model.Student;
import com.example.demo.teacher.TeacherRepository;
import com.example.demo.teacher.model.Teacher;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

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

    @Transactional(readOnly = true)
    public Lesson findById(long id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lesson with id " + id + " not found"));
    }

    @Transactional
    public void save(Lesson lesson, Long studentId, Long teacherId) {
        if (lesson.getLessonDate().isBefore(LocalDateTime.now())) {
            throw new LessonDateInPastException("Lesson date is before current date");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student with id " + studentId + " not found"));
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new EntityNotFoundException("Teacher with id " + teacherId + " not found"));

        LocalDateTime from = lesson.getLessonDate().minusHours(1);
        LocalDateTime to = lesson.getLessonDate().plusHours(1);

        if (lessonRepository.existsByTeacherAndLessonDateGreaterThanAndLessonDateLessThan(teacher, from, to)) {
            throw new LessonConflictException("Lesson with id " + lesson.getId() + " already exists");
        }

        lesson.setStudent(student);
        lesson.setTeacher(teacher);
        lessonRepository.save(lesson);
    }

    @Transactional
    public void change(Long lessonId, LocalDateTime newDate) {

        if (newDate == null) {
            throw new IllegalArgumentException("Lesson date is null");
        }

        if (newDate.isBefore(LocalDateTime.now())) {
            throw new LessonDateInPastException("Lesson date is before current date");
        }

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new EntityNotFoundException("Lesson with id " + lessonId + " not found"));

        Teacher teacher = lesson.getTeacher();
        Student student = lesson.getStudent();

        lessonRepository.delete(lesson);// usuwamy lekcje, zeby termin nie kolidowal
        lessonRepository.flush(); // flush sluzy do usuniecia tego natychmiastowo

        LocalDateTime from = newDate.minusHours(1);
        LocalDateTime to = newDate.plusHours(1);

        studentHasConflict(student, from, to, lessonId);
        teacherHasConflict(teacher, from, to, lessonId);

//        lesson.setLessonDate(newDate);
        Lesson newLesson = Lesson.builder()
                .student(student)
                .teacher(teacher)
                .lessonDate(newDate)
                .build();
        lessonRepository.save(newLesson);
//        lessonRepository.save(lesson);
    }

    private void studentHasConflict(Student student, LocalDateTime from, LocalDateTime to, Long lessonId) {

        if (lessonRepository.existsByStudentAndLessonDateGreaterThanAndLessonDateLessThan(student, from, to)) {
            throw new LessonConflictException("Lesson with id " + lessonId + " already exists");
        } //todo rozkminić jak inaczej można to zrobić nie używając IdNot, które było powyżej
    }


    private void teacherHasConflict(Teacher teacher, LocalDateTime from, LocalDateTime to, Long lessonId) {

        if (lessonRepository.existsByTeacherAndLessonDateGreaterThanAndLessonDateLessThan(teacher, from, to)) {
            throw new LessonConflictException("Lesson with id " + lessonId + " already exists");
        }
    }
}


