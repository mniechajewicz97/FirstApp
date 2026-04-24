package com.example.demo.teacher;

import com.example.demo.teacher.model.Teacher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherRepository teacherRepository;


    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }

    public void deleteById(long id) {
         teacherRepository.deleteById(id);
    }

    public Teacher findById(long id) {
        return teacherRepository.findById(id);
    }
}
