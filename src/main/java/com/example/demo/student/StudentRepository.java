package com.example.demo.student;

import com.example.demo.common.Language;
import com.example.demo.student.model.Student;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;


@Repository
public class StudentRepository {
    private final List<Student> students = new ArrayList<>(); // final bo po stworzeniu listy nie jest ona edytowalna

    @PostConstruct // to powoduje, ze zawsze po odpaleniu apki ta metoda sie wykona
    public void populateStudents() {
//        students.add(new Student(1234, null, "Kowalski", Language.JAVA));
        students.add(Student.builder().id(1234).lastName("Kowalski").language(Language.JAVA).build());
        students.add(Student.builder().firstName("Monika").lastName("Niechajewicz").id(1235).language(Language.JAVA).build());
        students.add(Student.builder().firstName("Hieronim").lastName("Pietruszka").id(6969).language(Language.PYTHON).build());
    }

    public List<Student> findAll() {
        return students;
    }

    public void deleteById(long id) {
        students.removeIf(student -> student.getId() == id);
    }

    public Student findById(long id) {
        return students.stream()
                .filter(student -> student.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
