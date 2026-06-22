package com.example.demo.student;

import com.example.demo.student.model.Student;
import com.example.demo.teacher.TeacherRepository;
import com.example.demo.teacher.model.Teacher;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository; //dependency injection
    private final TeacherRepository teacherRepository;

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public void deleteById(long id) {
        studentRepository.deleteById(id);
    }

    public Student findById(long id) {
        return studentRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Student with id " + id + " not found"));
    }


    public void save(Student student, Long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(
                () -> new EntityNotFoundException("Teacher with id " + teacherId + " not found"));
        student.setTeacher(teacher);
        studentRepository.save(student);
    }

    public void changeTeacher(Long studentId, Long newTeacherId) {
    Student student = studentRepository.findById(studentId).orElseThrow(()-> new EntityNotFoundException("Student with id " + studentId + " not found"));
    Teacher teacher = teacherRepository.findById(newTeacherId).orElseThrow(()-> new EntityNotFoundException("Teacher with id " + newTeacherId + " not found"));

    if (!teacher.getLanguages().contains(student.getLanguage())) {
        throw new IllegalArgumentException("Teacher does not teach student language: " + student.getLanguage());
    }
        student.setTeacher(teacher);
        studentRepository.save(student);
    }


    }

