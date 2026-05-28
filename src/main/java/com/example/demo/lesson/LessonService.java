package com.example.demo.lesson;

import com.example.demo.lesson.model.Lesson;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;

    public List<Lesson> findAll() {
        return lessonRepository.findAll();
    }

    public void deleteById(long id) {
        lessonRepository.deleteById(id);
    }

    public Lesson findById(long id) {
        return lessonRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Lesson with id " + id + " not found"));

    }

    public void save(Lesson lesson) {
        lessonRepository.save(lesson);
    }
}
