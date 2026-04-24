package com.example.demo.student;

import com.example.demo.student.model.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public void deleteById(long id) {
        studentRepository.deleteById(id);
    }

    public Student findById(long id) {
        return studentRepository.findById(id);
    }


}
