package com.example.demo.lesson;

import com.example.demo.common.LessonConflictException;
import com.example.demo.common.LessonDateInPastException;
import com.example.demo.common.dto.LessonDTO;
import com.example.demo.lesson.model.Lesson;
import com.example.demo.lesson.model.command.CreateLessonCommand;
import com.example.demo.student.StudentRepository;
import com.example.demo.student.model.Student;
import com.example.demo.teacher.TeacherRepository;
import com.example.demo.teacher.model.Teacher;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public List<LessonDTO> findAll() {
        List<LessonDTO> alllessonsDTOS = lessonRepository.findAll().stream()
                .map(LessonDTO::from)
                .toList();

        return alllessonsDTOS;
    }

    public void deleteById(long id) {

        lessonRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public LessonDTO findById(long id) {
        LessonDTO lessonDTO = lessonRepository.findById(id)
                .map(LessonDTO::from)
                .orElseThrow(() -> new EntityNotFoundException("Lesson with id " + id + " not found"));
        return lessonDTO;
    }

    @Transactional
    public LessonDTO save(CreateLessonCommand lessonCommand) {

        lessonDateValidator(lessonCommand.getLessonDate());

        Student student = studentRepository.findById(lessonCommand.getStudentId())
                .orElseThrow(() -> new EntityNotFoundException("Student with id " + lessonCommand.getStudentId() + " not found"));
        Teacher teacher = teacherRepository.findByIdAndDeletedFalse(lessonCommand.getTeacherId())
                .orElseThrow(() -> new EntityNotFoundException("Teacher with id " + lessonCommand.getTeacherId() + " not found"));

        LocalDateTime from = lessonCommand.getLessonDate().minusHours(1);
        LocalDateTime to = lessonCommand.getLessonDate().plusHours(1);

        Lesson lesson = lessonCommand.toEntity();

        if (lessonRepository.existsByTeacherAndLessonDateGreaterThanAndLessonDateLessThan(teacher, from, to)) {
            throw new LessonConflictException("Lesson already exists");
        }


        lesson.setStudent(student);
        lesson.setTeacher(teacher);

        return  LessonDTO.from(lessonRepository.save(lesson));
    }

    private void lessonDateValidator(LocalDateTime newDate) {
        if (newDate == null) {
            throw new IllegalArgumentException("Lesson date is null");
        }

        if (newDate.isBefore(LocalDateTime.now())) {
            throw new LessonDateInPastException("Lesson date is before current date");
        }
    }

    @Transactional
    public void change(Long lessonId, LocalDateTime newDate) {

        lessonDateValidator(newDate);

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new EntityNotFoundException("Lesson with id " + lessonId + " not found"));

        Teacher teacher = lesson.getTeacher();
        Student student = lesson.getStudent();


        LocalDateTime from = newDate.minusHours(1);
        LocalDateTime to = newDate.plusHours(1);

        studentHasConflict(student, from, to, lessonId);
        teacherHasConflict(teacher, from, to, lessonId);

        lesson.setLessonDate(newDate);
//        Lesson newLesson = Lesson.builder()
//                .student(student)
//                .teacher(teacher)
//                .lessonDate(newDate)
//                .build();
//        lessonRepository.save(newLesson);
        lessonRepository.save(lesson);
    }

    private void studentHasConflict(Student student, LocalDateTime from, LocalDateTime to, Long lessonId) {

        if (lessonRepository.existsByStudentAndLessonDateGreaterThanAndLessonDateLessThanAndIdNot(student, from, to, lessonId)) {
            throw new LessonConflictException("Lesson Date is not available");
        }
    }


    private void teacherHasConflict(Teacher teacher, LocalDateTime from, LocalDateTime to, Long lessonId) {

        if (lessonRepository.existsByTeacherAndLessonDateGreaterThanAndLessonDateLessThanAndIdNot(teacher, from, to, lessonId)) {
            throw new LessonConflictException("Lesson Date is not available");
        }
    }
}


