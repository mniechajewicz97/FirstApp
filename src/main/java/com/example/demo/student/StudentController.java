package com.example.demo.student;


import com.example.demo.common.Language;
import com.example.demo.common.dto.StudentDTO;
import com.example.demo.common.dto.TeacherDTO;
import com.example.demo.student.model.Student;
import com.example.demo.teacher.TeacherService;
import com.example.demo.teacher.model.Teacher;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/changeTeacher")
    public String changeTeacher(Model model, @RequestParam Long studentId) {
        model.addAttribute("student", studentService.findById(studentId));
        model.addAttribute("teachers", teacherService.findByLanguagesContains(studentService.findById(studentId).getLanguage()));

        return "student/changeTeacher";

    }
    @PostMapping("/changeTeacher")
    public String changeTeacher(@RequestParam Long studentId, @RequestParam Long newTeacherId) {
        studentService.changeTeacher(studentId, newTeacherId);

        return "redirect:/students";

    }

    @GetMapping(params = "teacher")
    @ResponseBody
    public List<StudentDTO> findAllByTeacher(@RequestParam ("teacher") Long teacherId) {

        return studentService.findAllByTeacher(teacherId);
    }


}
