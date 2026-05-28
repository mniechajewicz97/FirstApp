package com.example.demo.student;


import com.example.demo.common.Language;
import com.example.demo.student.model.Student;
import com.example.demo.teacher.TeacherService;
import com.example.demo.teacher.model.Teacher;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;
    private final TeacherService teacherService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("students", studentService.findAll());
        return "student/list";
    }

    @GetMapping("/{id}")
    public String delete(@PathVariable long id) {
        studentService.deleteById(id);
        return "redirect:/students";
    }
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("languages", Language.values());
        model.addAttribute("teachers", teacherService.findAll());
        return "student/register";
    }
    @PostMapping("/create")
    public String save(Student student, Long teacherId) {
        studentService.save(student, teacherId);
        return "redirect:/students";
    }

//    @GetMapping("/{id}")
//    public Student findById(@PathVariable long id) {
//      return studentService.findById(id);
//
//    }
}
