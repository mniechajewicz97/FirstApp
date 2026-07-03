package com.example.demo.teacher;

import com.example.demo.common.Language;
import com.example.demo.common.dto.TeacherDTO;
import com.example.demo.student.StudentRepository;
import com.example.demo.student.model.Student;
import com.example.demo.teacher.model.Teacher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private TeacherService teacherService;

    @Captor
    private ArgumentCaptor<Teacher> teacherCaptor;

    @Test
    void testFindAll_ResultInTeacherBeingReturned() {
        //given
        List<Teacher> teachers = List.of(new Teacher(), new Teacher(), new Teacher());
        when(teacherRepository.findAll()).thenReturn(teachers);

        //when
        List<TeacherDTO> result = teacherService.findAll();

        //then
        assertEquals(3, result.size());
        verify(teacherRepository).findAll();

    }

    @Test
    void testDeleteById_ResultsInTeacherBeingDeleted() {

        //given
        Teacher teacher = Teacher.builder()
                .id(1L)
                .firstName("Jack")
                .lastName("Sack")
                .build();
        Long teacherId = teacher.getId();

        Student student1 = Student.builder()
                .id(2L)
                .firstName("Pak")
                .lastName("Mak")
                .teacher(teacher)
                .build();
        Student student2 = Student.builder()
                .id(3L)
                .firstName("Plak")
                .lastName("Flak")
                .teacher(teacher)
                .build();
        Set<Student> students = new HashSet<>();
        students.add(student1);
        students.add(student2);
        teacher.setStudents(students);

        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));

        //when
        teacherService.deleteById(teacherId);

        //then
        verify(teacherRepository).findById(teacherId);
        assertNull(student1.getTeacher());
        assertNull(student2.getTeacher());
        verify(studentRepository).saveAll(students);
        verify(teacherRepository).delete(teacher);


    }

    @Test
    void testFindById_ResultsInTeacherDTOBeingReturned() {
        //given
        Teacher teacher = Teacher.builder()
                .id(1L)
                .firstName("Jack")
                .lastName("Sack")
                .build();
        Long teacherId = teacher.getId();
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));

        //when
        TeacherDTO result = teacherService.findById(teacherId);

        //then
        verify(teacherRepository).findById(teacherId);
        assertEquals(teacher.getFirstName(), result.getFirstName());
        assertEquals(teacher.getLastName(), result.getLastName());
        assertEquals(teacherId, result.getId());

    }

    @Test
    void testFindByLanguageContains_ResultsInTeacherDTOsBeingReturned() {
        //given
        Teacher teacher = Teacher.builder()
                .id(1L)
                .firstName("Jack")
                .lastName("Sack")
                .languages(Set.of(Language.JAVA, Language.PYTHON))
                .build();
        Language language = Language.JAVA;
        Teacher teacher2 = Teacher.builder()
                .id(4L)
                .firstName("Pan")
                .lastName("Kracy")
                .languages(Set.of(Language.JAVA, Language.C))
                .build();


        when(teacherRepository.findAllByLanguagesContains(language)).thenReturn(List.of(teacher, teacher2));

        //when
        List<TeacherDTO> result = teacherService.findByLanguagesContains(language);

        //then
        verify(teacherRepository).findAllByLanguagesContains(language);
        assertEquals(2, result.size());
        assertEquals(teacher.getFirstName(), result.get(0).getFirstName());
        assertEquals(teacher.getLastName(), result.get(0).getLastName());
        assertEquals(teacher2.getLastName(), result.get(1).getLastName());
        assertEquals(teacher.getId(), result.get(0).getId());


    }


}
