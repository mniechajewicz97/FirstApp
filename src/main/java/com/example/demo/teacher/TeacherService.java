package com.example.demo.teacher;

import com.example.demo.student.StudentRepository;
import com.example.demo.student.model.Student;
import com.example.demo.teacher.model.Teacher;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;


    public List<Teacher> findAll() {

        return teacherRepository.findAll();
    }

    public void deleteById(long id) {

       Teacher removedTeacher = teacherRepository.findById(id)
               .orElseThrow( ()-> new EntityNotFoundException("No teacher found"));
        Set<Student> students =  removedTeacher.getStudents();
        students.forEach(student -> {
            student.setTeacher(null);
        });
        studentRepository.saveAll(students); // zapisujemy studentow,
        teacherRepository.delete(removedTeacher);

    }

    public Teacher findById(long id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Teacher with id " + id + " not found"));
    }

    public void save(Teacher teacher) {
        teacherRepository.save(teacher);



        }


    }

