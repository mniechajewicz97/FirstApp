package com.example.demo.teacher;
import com.example.demo.teacher.model.Teacher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/teachers")
public class TeacherController {
    private final TeacherService teacherService;

    @GetMapping
    public List<Teacher> getAll() {
        return teacherService.findAll();
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        teacherService.deleteById(id);
    }
    @GetMapping("/{id}")
    public Teacher findById(@PathVariable long id) {
        return teacherService.findById(id);
    }
}
