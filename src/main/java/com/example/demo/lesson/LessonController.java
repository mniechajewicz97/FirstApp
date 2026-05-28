package com.example.demo.lesson;


import com.example.demo.lesson.model.Lesson;
import com.example.demo.student.StudentService;
import com.example.demo.teacher.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@RequestMapping("/lessons")
public class LessonController {
    private final LessonService lessonService;
    private final StudentService studentService;
    private final TeacherService teacherService;

    @GetMapping
    public String getAll(Model model){
        model.addAttribute("lessons", lessonService.findAll());
        return "lesson/list";
    }
//
//
//    @GetMapping("/{id}")
//    public String deleteById(@PathVariable long id) {
//        lessonService.deleteById(id);
//        return "redirect:/lessons";
//    }
    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("students", studentService.findAll());
        return "lesson/register";
    }
    @PostMapping("/create")
    public String save(Lesson lesson, @RequestParam Long studentId, Long teacherId){
        lessonService.save(lesson, studentId, teacherId);
        return  "redirect:/lessons";
    }


//    @GetMapping("/{id}")
//    public Lesson findById(@PathVariable long id) {
//        return lessonService.findById(id);
//    }


}
