package com.example.demo.teacher;

import aj.org.objectweb.asm.commons.Remapper;
import com.example.demo.common.Language;
import com.example.demo.common.dto.TeacherDTO;
import com.example.demo.teacher.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
//    private final List<Teacher> teachers = new ArrayList<>();

//    List<Teacher> findAllByLanguagesContains(Language language); //derived query to pozwala filtrowac nauczycieli

    List<Teacher> findAllByDeletedFalse();

    Optional<Teacher> findByIdAndDeletedFalse(long id);

    List<Teacher> findAllByLanguagesContainsAndDeletedFalse(Language language);

//todo zobacz sobie slowa klucz tego typu

//    @PostConstruct
//    public void populateTeachers() {
//        teachers.add(Teacher.builder().id(1111).firstName("Adix").lastName("Brygider").languages(List.of(Language.JAVA, Language.PYTHON, Language.KOTLIN)).build());
//        teachers.add(Teacher.builder().id(1113).firstName("Amadeusz").lastName("Mściwy").languages(List.of(Language.C, Language.CSHARP)).build());
//    }
//    public List<Teacher> findAll() {
//        return teachers;
//    }
//    public void deleteById(long id) {
//        teachers.removeIf(teacher -> teacher.getId() == id);
//    }
//
//
//    public Teacher findById(long id) {
//        return teachers.stream()
//                .filter(teacher -> teacher.getId() == id)
//                .findFirst()
//                .orElse(null);
//    }
}
