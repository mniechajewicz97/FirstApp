package com.example.demo.teacher;

import com.example.demo.common.Language;
import com.example.demo.common.dto.TeacherDTO;
import com.example.demo.student.StudentRepository;
import com.example.demo.teacher.model.Teacher;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

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
        when(teacherRepository.findAllByDeletedFalse()).thenReturn(teachers);

        //when
        List<TeacherDTO> result = teacherService.findAll();

        //then
        assertEquals(3, result.size());
        verify(teacherRepository).findAllByDeletedFalse();

    }

    @Test
    void testDeleteById_ResultsInTeacherBeingDeleted() {

        //given
        Teacher teacher = Teacher.builder()
                .id(2L)
                .firstName("Jack")
                .lastName("Sack")
                .build();
        long teacherId = teacher.getId();
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));


        //when
        teacherService.deleteById(teacherId);

        //then
        assertTrue(teacher.isDeleted());
        verify(teacherRepository).findById(teacherId);



    }

    @Test
    void testDeleteById_WhenTeacherDoesntExist_ThrowsEntityNotFoundException() {

        Long teacherId = 1L;
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> teacherService.deleteById(teacherId))
                .withMessage("Teacher with id " + teacherId + " not found");
        verify(teacherRepository).findById(teacherId);
        verifyNoMoreInteractions(teacherRepository);

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
        when(teacherRepository.findByIdAndDeletedFalse(teacherId)).thenReturn(Optional.of(teacher));

        //when
        TeacherDTO result = teacherService.findById(teacherId);

        //then
        verify(teacherRepository).findByIdAndDeletedFalse(teacherId);
        assertEquals(teacher.getFirstName(), result.getFirstName());
        assertEquals(teacher.getLastName(), result.getLastName());
        assertEquals(teacherId, result.getId());

    }

    @Test
    void testFindById_WhenTeacherDoesntExist_ThrowsEntityNotFoundException() {

        Long teacherId = 1L;
        when(teacherRepository.findByIdAndDeletedFalse(teacherId)).thenReturn(Optional.empty());
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> teacherService.findById(teacherId))
                .withMessage("Teacher with id " + teacherId + " not found");
        verifyNoMoreInteractions(teacherRepository);

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


        when(teacherRepository.findAllByLanguagesContainsAndDeletedFalse(language)).thenReturn(List.of(teacher, teacher2));

        //when
        List<TeacherDTO> result = teacherService.findByLanguagesContains(language);

        //then
        verify(teacherRepository).findAllByLanguagesContainsAndDeletedFalse(language);
        assertEquals(2, result.size());
        assertEquals(teacher.getFirstName(), result.get(0).getFirstName());
        assertEquals(teacher.getLastName(), result.get(0).getLastName());
        assertEquals(teacher2.getLastName(), result.get(1).getLastName());
        assertEquals(teacher.getId(), result.get(0).getId());


    }


}
