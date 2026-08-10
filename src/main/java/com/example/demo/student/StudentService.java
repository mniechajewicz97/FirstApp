package com.example.demo.student;

import com.example.demo.common.LanguageMismatchException;
import com.example.demo.common.dto.StudentDTO;
import com.example.demo.student.model.Student;
import com.example.demo.student.model.command.CreateStudentCommand;
import com.example.demo.teacher.TeacherRepository;
import com.example.demo.teacher.model.Teacher;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository; //dependency injection
    private final TeacherRepository teacherRepository;

    public List<StudentDTO> findAll() {
        List<StudentDTO> allStudentsDTOs = studentRepository.findAll().stream()
                .map(StudentDTO::from)
                .toList();
        return allStudentsDTOs;
    }

    public void deleteById(long id) {

        studentRepository.deleteById(id);
    }

    public StudentDTO findById(long id) {
        StudentDTO studentDTO = studentRepository.findById(id)
                .map(StudentDTO::from)
                .orElseThrow(() -> new EntityNotFoundException("Student with id " + id + " not found"));

        return studentDTO;
    }

    @Transactional
    // oba wyjatki dziedzicza po Runtimeexception wiec nie ma potrzby dawania w nawiasach bo rollback wydarzy sie automatycznie
    public StudentDTO save(CreateStudentCommand studentCommand) {
        Teacher teacher = teacherRepository.findByIdAndDeletedFalse(studentCommand.getTeacherId()).orElseThrow(
                () -> new EntityNotFoundException("Teacher with id " + studentCommand.getTeacherId() + " not found"));

        Student student = studentCommand.toEntity();
        languageValidator(teacher, student);
        student.setTeacher(teacher);

        return StudentDTO.from(studentRepository.save(student));
    }

    private void languageValidator(Teacher teacher, Student student) {
        if (!teacher.getLanguages().contains(student.getLanguage())) {
            throw new LanguageMismatchException("Language mismatch");
        }
    }

    @Transactional //jw
    public void changeTeacher(Long studentId, Long newTeacherId) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new EntityNotFoundException("Student with id " + studentId + " not found"));
        Teacher teacher = teacherRepository.findByIdAndDeletedFalse(newTeacherId).orElseThrow(() -> new EntityNotFoundException("Teacher with id " + newTeacherId + " not found"));
        languageValidator(teacher, student);
        student.setTeacher(teacher);

        studentRepository.save(student);
    }


    public List<StudentDTO> findAllByTeacher(Long teacherId) {
        teacherRepository.findByIdAndDeletedFalse(teacherId).orElseThrow(
                () -> new EntityNotFoundException("Teacher with id " + teacherId + " not found"));

        return studentRepository.findAllByTeacherId(teacherId).stream()
                .map(StudentDTO::from) // mozna tez: .map(student -> StudentDTO.from(student))
                .toList();

    }
}

