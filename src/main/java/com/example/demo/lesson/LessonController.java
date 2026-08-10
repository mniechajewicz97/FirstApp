package com.example.demo.lesson;


import com.example.demo.common.dto.LessonDTO;
import com.example.demo.lesson.model.command.CreateLessonCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/lessons")
public class LessonController {
    private final LessonService lessonService;

    @GetMapping
    public List<LessonDTO> findAll() {
        return lessonService.findAll();
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable long id) {
        lessonService.deleteById(id);
    }

    @GetMapping("/{id}")
    public LessonDTO findById(@PathVariable long id) {
        return lessonService.findById(id);
    }

    @PostMapping
    public LessonDTO save(@RequestBody CreateLessonCommand lessonCommand) {
        return lessonService.save(lessonCommand);
    }

    @PatchMapping("/{lessonId}")
    public void change(@PathVariable Long lessonId,
                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime newDate) { //iso zmienia date z tekstowego formatu na localdatetime
        lessonService.change(lessonId, newDate); // adnotacja datetimeformat mowi jak ten tekst przeczytac

    }

}






