package com.example.demo.student;

import com.example.demo.student.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.*;
import java.util.List;


public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findAllByTeacherId(Long teacherId);
//    private final List<Student> students = new ArrayList<>(); // final bo po stworzeniu listy nie jest ona edytowalna
//
//    @PostConstruct // to powoduje, ze zawsze po odpaleniu apki ta metoda sie wykona
//    public void populateStudents() {
////        students.add(new Student(1234, null, "Kowalski", Language.JAVA));
//        students.add(Student.builder().id(1234).lastName("Kowalski").language(Language.JAVA).build());
//        students.add(Student.builder().firstName("Monika").lastName("Niechajewicz").id(1235).language(Language.JAVA).build());
//        students.add(Student.builder().firstName("Hieronim").lastName("Pietruszka").id(6969).language(Language.PYTHON).build());
//    }
//
//    public List<Student> findAll() {
//        return students;
//    }
//
//    public void deleteById(long id) {
//        students.removeIf(student -> student.getId() == id);
//    }
//
//    public Student findById(long id) {
//        return students.stream()
//                .filter(student -> student.getId() == id)
//                .findFirst()
//                .orElse(null);
//    }
}
