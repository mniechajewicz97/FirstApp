package com.example.demo.teacher;

import com.example.demo.common.Language;
import com.example.demo.common.dto.TeacherDTO;
import com.example.demo.student.StudentRepository;
import com.example.demo.student.model.Student;
import com.example.demo.teacher.model.Teacher;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;


    public List<TeacherDTO> findAll() {

        List<TeacherDTO> allTeachersDTOS = teacherRepository.findAll().stream()
                .map(TeacherDTO::from)
                .toList();
        return allTeachersDTOS;
    }
    @Transactional
    public void deleteById(long id) {

        Teacher removedTeacher = teacherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Teacher with id " + id + " not found"));
        Set<Student> students = removedTeacher.getStudents();
        students.forEach(student -> {
            student.setTeacher(null);
        });
        studentRepository.saveAll(students); // zapisujemy studentow,
        teacherRepository.delete(removedTeacher);

    }

    public TeacherDTO findById(long id) {
        TeacherDTO teacherDTO = teacherRepository.findById(id)
                .map(TeacherDTO::from)
                .orElseThrow(() -> new EntityNotFoundException("Teacher with id " + id + " not found"));

        return teacherDTO;
    }

    public void save(Teacher teacher) {

        teacherRepository.save(teacher);
    }

    public List<TeacherDTO> findByLanguagesContains(Language language) {
        List<TeacherDTO> teacherDTOS = teacherRepository.findAllByLanguagesContains(language).stream()
                .map(TeacherDTO::from)// wywolanie metody statycznej dla kazdego elementu
                .toList();
        return teacherDTOS;


    }


}

