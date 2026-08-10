package com.example.demo.lesson;

import com.example.demo.common.Language;
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

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        Student student = new Student();
        Teacher teacher = new Teacher();
        LocalDateTime date = LocalDateTime.now().plusDays(1);
        LocalDateTime date2 = LocalDateTime.now().plusDays(2);

        Lesson lesson1 = Lesson.builder()
                .id(2L)
                .student(student)
                .teacher(teacher)
                .lessonDate(date)
                .build();
        Lesson lesson2 = Lesson.builder()
                .id(3L)
                .student(student)
                .teacher(teacher)
                .lessonDate(date2)
                .build();


        List<Lesson> lessons = List.of(lesson1, lesson2);
        when(lessonRepository.findAll()).thenReturn(lessons);

        //when - akcja czyli wywołujemy nasza metode
        List<LessonDTO> result = lessonService.findAll();

        //then - weryfikacja czy result napewno zwrocil to co trzeba

        assertEquals(2, result.size());
        verify(lessonRepository).findAll();
    }

    @Test
    void testFindById_ResultsInLessonBeingReturned() {
        //given
        Student student = new Student();
        Teacher teacher = new Teacher();
        LocalDateTime date = LocalDateTime.now().plusDays(1);
        Lesson lesson = Lesson.builder()
                .id(2L)
                .student(student)
                .teacher(teacher)
                .lessonDate(date)
                .build();

        when(lessonRepository.findById(2L)).thenReturn(Optional.of(lesson));

        //when
        LessonDTO result = lessonService.findById(2L);

        //then
        assertEquals(lesson.getId(), result.getId());
        verify(lessonRepository).findById(2L);

    }

    //unhappy path
    @Test
    void testFindById_WhenLessonDoesNotExist_ResultsInEntityNotFoundException() {
        //given
        when(lessonRepository.findById(2L)).thenReturn(Optional.empty());
        //when and then

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> lessonService.findById(2L));
        assertEquals("Lesson with id " + 2 + " not found", exception.getMessage()); // oczekuje wyjatku klasy entitynotfoundexeption
        verify(lessonRepository).findById(2L);

    }

    @Test
    void testSave_ResultsInLessonsBeingSaved() {
        //given

        CreateLessonCommand lessonCommand = new CreateLessonCommand();
        lessonCommand.setLessonDate(LocalDateTime.now().plusDays(1));
        lessonCommand.setTeacherId(1L);
        lessonCommand.setStudentId(2L);

        Student student = Student.builder()
                .id(2L)
                .firstName("John")
                .lastName("Doe")
                .build();

        Teacher teacher = Teacher.builder()
                .id(1L)
                .firstName("Jack")
                .lastName("Sam")
                .build();

        when(lessonRepository.save(any(Lesson.class))) // zawsze zwroc ten konkretny obiekt lesson
                .thenAnswer(invocation -> invocation.getArgument(0)); // invocation oznacza wywolanie mockowej metody
        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));
        when(teacherRepository.findByIdAndDeletedFalse(teacher.getId())).thenReturn(Optional.of(teacher));
        when(lessonRepository.existsByTeacherAndLessonDateGreaterThanAndLessonDateLessThan(any(), any(), any())).thenReturn(false); //false bo nie robimy unhappy path
        //when
        lessonService.save(lessonCommand);
        //then
        verify(lessonRepository).save(lessonCaptor.capture());
        Lesson saved = lessonCaptor.getValue();
        assertEquals(student, saved.getStudent());
        assertEquals(teacher, saved.getTeacher());


    }

    @Test
    void testSave_WhenLessonDateIsBeforeCurrentDate_ThrowsLessonDateInPastException() {
        //given

        CreateLessonCommand lessonCommand = new CreateLessonCommand();
        lessonCommand.setLessonDate(LocalDateTime.now().minusHours(1));

        //when and then
        LessonDateInPastException exception = assertThrows(LessonDateInPastException.class, () -> lessonService.save(lessonCommand));
        assertEquals("Lesson date is before current date", exception.getMessage());
        verify(lessonRepository, never()).save(any(Lesson.class)); // sprawdzenie, ze lecja nie zostala zapisana

    }

    @Test
    void testSave_WhenStudentDoesNotExist_ThrowsEntityNotFoundException() {
        //given

        CreateLessonCommand lessonCommand = new CreateLessonCommand();
        lessonCommand.setLessonDate(LocalDateTime.now().plusHours(1));
        lessonCommand.setTeacherId(1L);
        lessonCommand.setStudentId(2L);

        when(studentRepository.findById(2L)).thenReturn(Optional.empty());

        //when and then
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> lessonService.save(lessonCommand))
                .withMessage("Student with id " + lessonCommand.getStudentId() + " not found");

        verify(studentRepository).findById(2L);
        verify(lessonRepository, never()).save(any(Lesson.class));
        verifyNoInteractions(teacherRepository);

    }

    @Test
    void testSave_WhenTeacherDoesNotExist_ThrowsEntityNotFoundException() {
        //given
        Lesson lesson = Lesson.builder()
                .id(2L)
                .lessonDate(LocalDateTime.now().plusHours(1))
                .student(Student.builder().id(4L).build())
                .build();
        CreateLessonCommand lessonCommand = new CreateLessonCommand();
        lessonCommand.setLessonDate(LocalDateTime.now().plusHours(1));
        lessonCommand.setStudentId(4L);
        lessonCommand.setTeacherId(5L);

        when(studentRepository.findById(4L)).thenReturn(Optional.of(lesson.getStudent()));
        when(teacherRepository.findByIdAndDeletedFalse(5L)).thenReturn(Optional.empty());

        //when and then
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(()-> lessonService.save(lessonCommand))
                        .withMessage("Teacher with id " + lessonCommand.getTeacherId() + " not found");
        verify(studentRepository).findById(4L);
        verify(teacherRepository).findByIdAndDeletedFalse(5L);
        verifyNoInteractions(lessonRepository);
    }

    @Test
    void testSave_WhenTeacherHasLessonConflict_ThrowsLessonConflictException() {
        //given
        Teacher teacher = Teacher.builder()
                .id(5L)
                .build();
        Student student = Student.builder()
                .id(4L)
                .build();

        Lesson lesson = Lesson.builder()
                .id(2L)
                .lessonDate(LocalDateTime.now().plusHours(1))
                .student(student)
                .teacher(teacher)
                .build();
        LocalDateTime date = LocalDateTime.now().plusHours(1);

        CreateLessonCommand lessonCommand = new CreateLessonCommand();
        lessonCommand.setLessonDate(date);
        lessonCommand.setStudentId(4L);
        lessonCommand.setTeacherId(5L);

        LocalDateTime from = date.minusHours(1);
        LocalDateTime to = date.plusHours(1);

        when(studentRepository.findById(4L)).thenReturn(Optional.of(student));
        when(teacherRepository.findByIdAndDeletedFalse(5L)).thenReturn(Optional.of(teacher));
        when(lessonRepository.existsByTeacherAndLessonDateGreaterThanAndLessonDateLessThan(lesson.getTeacher(), from, to)).thenReturn(true);

        //when and then
        assertThatExceptionOfType(LessonConflictException.class)
                .isThrownBy(() -> lessonService.save(lessonCommand))
                .withMessage("Lesson already exists");

        verify(lessonRepository).existsByTeacherAndLessonDateGreaterThanAndLessonDateLessThan(teacher, from, to);
        verifyNoMoreInteractions(lessonRepository);
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

    @Test
    void testChange_WhenNewDateIsNull_ThrowsIllegalArgumentException() {
        //when and then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> lessonService.change(2L, null))
                .withMessage("Lesson date is null");

        verifyNoInteractions(lessonRepository); // jest nointeraction bo nie ma kontaktu z baza danych
    }

    @Test
    void testChange_WhenLessonDateIsBeforeCurrentDate_ThrowsLessonDateInPastException() {
        assertThatExceptionOfType(LessonDateInPastException.class)
                .isThrownBy(() -> lessonService.change(2L, LocalDateTime.now().minusHours(1)))
                .withMessage("Lesson date is before current date");

        verifyNoInteractions(lessonRepository);


    }

    @Test
    void testChange_WhenLessonWithGivenIdDoesNotExist_ThrowsEntityNotFoundException() {
        //given
        Lesson lesson = Lesson.builder()
                .id(2L)
                .lessonDate(LocalDateTime.now().plusDays(1))
                .build();
        when(lessonRepository.findById(2L)).thenReturn(Optional.empty());

        //when and then

        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> lessonService.change(lesson.getId(), lesson.getLessonDate()))
                .withMessage("Lesson with id " + 2L + " not found");

        verify(lessonRepository).findById(2L);
        verifyNoMoreInteractions(lessonRepository); //przed tym wystepuje kontakt z baza danych
    }

    @Test
    void testChange_WhenStudentHasLessonConflict_ThrowsLessonConflictException() {

        Lesson lesson = Lesson.builder()
                .id(2L)
                .lessonDate(LocalDateTime.now().plusDays(1))
                .student(Student.builder().id(2L).build())
                .teacher(Teacher.builder().id(3L).build())
                .build();

        LocalDateTime newDate = LocalDateTime.now().plusHours(1);
        LocalDateTime from = newDate.minusHours(1);
        LocalDateTime to = newDate.plusHours(1);

        Long lessonId = lesson.getId();

        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));
        when(lessonRepository.existsByStudentAndLessonDateGreaterThanAndLessonDateLessThanAndIdNot(lesson.getStudent(), from, to, lessonId)).thenReturn(true);

        assertThatExceptionOfType(LessonConflictException.class)
                .isThrownBy(() -> lessonService.change(2L, newDate))
                .withMessage("Lesson Date is not available");

        verify(lessonRepository).findById(lessonId);
        verifyNoMoreInteractions(lessonRepository);
    }


}
