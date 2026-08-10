package com.example.demo.teacher;

import com.example.demo.common.Language;
import com.example.demo.common.dto.TeacherDTO;
import com.example.demo.teacher.model.command.CreateTeacherCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/teachers")
public class TeacherController {
    private final TeacherService teacherService;


    @GetMapping
    public List<TeacherDTO> findAll() {
        return teacherService.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        teacherService.deleteById(id);
    }

    @PostMapping
    public TeacherDTO save(@RequestBody CreateTeacherCommand teacherCommand) {
        return teacherService.save(teacherCommand);

    }

    @GetMapping(params = "language")
//    @ResponseBody // tresc od http ktora dostaje user, nie jest to juz nam potrzebne bo RestController działa jak polaczenie controllera i responseBody
    public List<TeacherDTO> findByLanguages(@RequestParam Language language) {
        return teacherService.findByLanguagesContains(language);
    }

    @GetMapping("/{id}")
    public TeacherDTO findById(@PathVariable long id) {
        return teacherService.findById(id);
    }
}

