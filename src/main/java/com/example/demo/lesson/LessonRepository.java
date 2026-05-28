package com.example.demo.lesson;

import com.example.demo.lesson.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;



public interface LessonRepository extends JpaRepository<Lesson, Long> {
//    private final List<Lesson> lessons = new ArrayList<>();
//    private final TeacherRepository teacherRepository;
//    private final StudentRepository studentRepository;

//
//    @PostConstruct
//    public void populateLessons() {
//        Teacher teacher1 = teacherRepository.findById(1111);
//        Student student1 = studentRepository.findById(1235);
//        if (teacher1 != null && student1 != null) {
//            lessons.add(new Lesson(1, student1, teacher1, LocalDateTime.of(2026, 4, 27, 18, 0)));
//        } else {
//            System.out.println("Teacher or Student not found");
//        }

//    }
//
//    public List<Lesson> findAll() {
//        return lessons;
//    }
//
//    public void deleteById(long id) {
//        lessons.removeIf(lesson -> lesson.getId() == id);
//    }
//
//    public Lesson findById(long id) {
//        return lessons.stream()
//                .filter(lesson -> lesson.getId() == id)
//                .findFirst()
//                .orElse(null);
    }

