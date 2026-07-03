package com.example.demo.lesson;

import com.example.demo.common.Language;
import com.example.demo.lesson.model.Lesson;
import com.example.demo.student.StudentRepository;
import com.example.demo.student.model.Student;
import com.example.demo.teacher.TeacherRepository;
import com.example.demo.teacher.model.Teacher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class LessonServiceTest {

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private LessonService lessonService;

    @Captor
    private ArgumentCaptor<Lesson> lessonCaptor; // pocwiczyc z chatem

    @Test
    void testFindAll_ResultsInLessonsBeingReturned() {
        //given - przygotowanie danych i co maja robic mocki
        List<Lesson> lessons = List.of(new Lesson(), new Lesson());
        when(lessonRepository.findAll()).thenReturn(lessons);

        //when - akcja czyli wywołujemy nasza metode
        List<Lesson> result = lessonService.findAll();

        //then - weryfikacja czy result napewno zwrocil to co trzeba

        assertEquals(2, result.size());
        verify(lessonRepository).findAll();
    }

    @Test
    void testSave_ResultsInLessonsBeingSaved() {
        //given
        Lesson lesson = Lesson.builder()
                .id(1L)
                .lessonDate(LocalDateTime.now().plusDays(1))
                .build();

        Student student = Student.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .build();

        Teacher teacher = Teacher.builder()
                .id(1L)
                .firstName("Jack")
                .lastName("Sam")
                .build();

        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));
        when(teacherRepository.findById(teacher.getId())).thenReturn(Optional.of(teacher));
        when(lessonRepository.existsByTeacherAndLessonDateGreaterThanAndLessonDateLessThan(any(), any(), any())).thenReturn(false); //false bo nie robimy unhappy path
        //when
        lessonService.save(lesson, student.getId(), teacher.getId());
        //then
        verify(lessonRepository).save(lessonCaptor.capture());
        Lesson saved = lessonCaptor.getValue();
        assertEquals(student, saved.getStudent());
        assertEquals(teacher, saved.getTeacher());


    }

    @Test
    void testChange_ResultsInLessonBeingChanged() {
        //given - przygotowanie danych i co maja robic mocki

        LocalDateTime oldDate = LocalDateTime.now().plusDays(1);
        LocalDateTime newDate = LocalDateTime.now().plusDays(2);

        Lesson lesson = Lesson.builder()
                .id(1L)
                .lessonDate(oldDate)
                .student(Student.builder()
                        .id(2L)
                        .firstName("Jan")
                        .lastName("Matejko")
                        .language(Language.JAVA)
                        .build())
                .teacher(Teacher.builder()
                        .id(3L)
                        .firstName("Jack")
                        .lastName("Sam")
                        .languages(Set.of(Language.JAVA))
                        .build())
                .build();

        LocalDateTime from = newDate.minusHours(1);
        LocalDateTime to = newDate.plusHours(1);

        Teacher teacher = lesson.getTeacher();
        Student student = lesson.getStudent();

        when(lessonRepository.findById(lesson.getId())).thenReturn(Optional.of(lesson));
        when(lessonRepository.existsByStudentAndLessonDateGreaterThanAndLessonDateLessThanAndIdNot(student, from, to, lesson.getId())).thenReturn(false);
        when(lessonRepository.existsByTeacherAndLessonDateGreaterThanAndLessonDateLessThanAndIdNot(teacher, from, to, lesson.getId())).thenReturn(false);

        //when akcja czyli wywołujemy nasza metode
        lessonService.change(lesson.getId(), newDate);

        //then - weryfikacja czy result napewno zwrocil to co trzeba

        verify(lessonRepository).save(lessonCaptor.capture());
        assertEquals(lesson.getId(), lessonCaptor.getValue().getId());
        assertEquals(newDate, lessonCaptor.getValue().getLessonDate());

    }



}
