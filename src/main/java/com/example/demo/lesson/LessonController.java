package com.example.demo.lesson;

import com.example.demo.lesson.model.Lesson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/lessons")
public class LessonController {
    private final LessonService lessonService;

    @GetMapping
    public List<Lesson> getAll() {
        return lessonService.findAll();
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable long id) {
        lessonService.deleteById(id);
    }

    @GetMapping("/{id}")
    public Lesson findById(@PathVariable long id) {
        return lessonService.findById(id);
    }


}
