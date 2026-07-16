package com.example.demo.student;

import com.example.demo.common.Language;
import com.example.demo.common.LanguageMismatchException;
import com.example.demo.common.dto.StudentDTO;
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

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private StudentService studentService;

    @Captor
    private ArgumentCaptor<Student> studentCaptor;

    @Test
    void testFindAll_ResultsInStudentDTOsBeingReturned() {
        //given - przygotowanie danych i co maja robic mocki
        List<Student> students = List.of(new Student(), new Student(), new Student());
        when(studentRepository.findAll()).thenReturn(students);

        //when - akcja czyli wywołujemy nasza metode
        List<StudentDTO> result = studentService.findAll();

        //then - weryfikacja czy result napewno zwrocil to co trzeba
        assertEquals(3, result.size());
        verify(studentRepository).findAll();

    }

    @Test
    void testSave_ResultsInStudentBeingSaved() {
        //given
        Student student = Student.builder()
                .id(3L)
                .firstName("John")
                .lastName("Doe")
                .build();

        Teacher teacher = Teacher.builder()
                .id(1L)
                .firstName("Jack")
                .lastName("Sam")
                .build();
        Long teacherId = teacher.getId();

        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));

        // when
        studentService.save(student, teacherId);

        // then

        verify(studentRepository).save(studentCaptor.capture());
        Student savedStudent = studentCaptor.getValue();
        Teacher savedTeacher = savedStudent.getTeacher();
        assertEquals(student.getFirstName(), savedStudent.getFirstName());
        assertEquals(teacherId, savedTeacher.getId());

    }

    @Test
    void testSave_WhenTeacherDoesNotExist_ThrowsEntityNotFoundException() {
        Student student = Student.builder()
                .id(3L)
                .build();
        Teacher teacher = Teacher.builder()
                .id(1L)
                .build();
        when(teacherRepository.findById(teacher.getId())).thenReturn(Optional.empty());

        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(()-> studentService.save(student, teacher.getId()))
                .withMessage("Teacher with id " + teacher.getId() + " not found");

    }


    @Test
    void testChangeTeacher_ResultsInTeacherBeingChanged() {
        //given
        Student student = Student.builder()
                .id(3L)
                .firstName("John")
                .lastName("Doe")
                .language(Language.JAVA)
                .build();

        Teacher teacher = Teacher.builder()
                .id(1L)
                .firstName("Jack")
                .lastName("Sam")
                .languages(Set.of(Language.JAVA, Language.PYTHON))
                .build();

        Long studentId = student.getId();
        Long teacherId = teacher.getId();

        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        //when
        studentService.changeTeacher(studentId, teacherId);

        //then
        verify(studentRepository).save(studentCaptor.capture());
        Student savedStudent = studentCaptor.getValue();
        Teacher savedTeacher = savedStudent.getTeacher();
        assertEquals(student.getFirstName(), savedStudent.getFirstName());
        assertEquals(teacherId, savedTeacher.getId());
        assertEquals(Language.JAVA, savedStudent.getLanguage());
        assertTrue(savedTeacher.getLanguages().contains(savedStudent.getLanguage()));

    }
    @Test
    void testChangeTeacher_WhenStudentDoesNotExist_ThrowsEntityNotFoundException() {

        Long studentId = 1L;
        Long teacherId = 2L;

        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(()-> studentService.changeTeacher(studentId, teacherId))
                .withMessage("Student with id " + studentId + " not found");

        verifyNoMoreInteractions(studentRepository);

    }
    @Test
    void testChangeTeacher_WhenTeacherDoesNotExist_ThrowsEntityNotFoundException() {

        Student student = Student.builder()
                .id(2L)
                .language(Language.JAVA)
                .build();
        Long teacherId = 2L;
        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(()-> studentService.changeTeacher(student.getId(), teacherId))
                .withMessage("Teacher with id " + teacherId + " not found");

        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    void testChangeTeacher_WhenTeacherDoesNotContainSelectedLanguage_ThrowsLanguageMismatchException() {
        //given
        Student student = Student.builder()
                .id(2L)
                .language(Language.JAVA)
                .build();

        Teacher teacher = Teacher.builder()
                .id(4L)
                .languages(Set.of(Language.C))
                .build();

        Long studentId = student.getId();
        Long teacherId = teacher.getId();

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));

        //when and then
        assertThatExceptionOfType(LanguageMismatchException.class)
                .isThrownBy(() -> studentService.changeTeacher(studentId, teacherId))
                .withMessage("Teacher does not teach language: " + student.getLanguage());

        verifyNoMoreInteractions(studentRepository);
    }


    @Test
    void testFindAllByTeacher_ResultsInStudentsBeingReturned() {

        //given
        Teacher teacher = Teacher.builder()
                .id(1L)
                .firstName("Jack")
                .lastName("Sam")
                .build();
        Long teacherId = teacher.getId();
        Student student1 = Student.builder()
                .id(5L)
                .firstName("Pak")
                .lastName("Srak")
                .build();
        Student student2 = Student.builder()
                .id(6L)
                .firstName("Mak")
                .lastName("Plak")
                .build();
        List<Student> students = List.of(student1, student2);
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));
        when(studentRepository.findAllByTeacherId(teacherId)).thenReturn(students);

        //when
        List<StudentDTO> result = studentService.findAllByTeacher(teacherId);

        //then
        verify(studentRepository).findAllByTeacherId(teacherId);
        assertEquals(2, result.size());
        assertEquals(students.get(0).getFirstName(), result.get(0).getFirstName());
        assertEquals(students.get(1).getFirstName(), result.get(1).getFirstName());
        assertEquals(students.get(0).getLastName(), result.get(0).getLastName());
        assertEquals(students.get(1).getLastName(), result.get(1).getLastName());
        assertEquals(students.get(0).getId(), result.get(0).getId());
        assertEquals(students.get(1).getId(), result.get(1).getId());
        verify(teacherRepository).findById(teacherId);

    }
    @Test
    void testFindAllByTeacher_WhenTeacherDoesNotExist_ThrowsEntityNotFoundException() {
        Long teacherId = 1L;
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> studentService.findAllByTeacher(teacherId))
                .withMessage("Teacher with id " + teacherId + " not found");
        verifyNoMoreInteractions(teacherRepository);
    }

}
