package com.example.demo.teacher;

import com.example.demo.common.Language;
import com.example.demo.student.StudentService;
import com.example.demo.teacher.model.Teacher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;


@Controller
@RequiredArgsConstructor
@RequestMapping("/teachers")
public class TeacherController {
    private final TeacherService teacherService;
    private final StudentService studentService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("teachers", teacherService.findAll());
        model.addAttribute("students", studentService.findAll());
        return "teacher/list";
    }

    @GetMapping("/{id}")
    public String delete(@PathVariable long id) {
        teacherService.deleteById(id);
        return "redirect:/teachers";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("languages", Language.values());

        return "teacher/register";

    }

    @PostMapping("/create")
    public String save(Teacher teacher) {
        teacherService.save(teacher);
        return "redirect:/teachers";
    }
}


//    @GetMapping("/{id}")
//    public Teacher findById(@PathVariable long id) {
//        return teacherService.findById(id);
//    }

