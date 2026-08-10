package com.example.demo.student;


import com.example.demo.common.dto.StudentDTO;
import com.example.demo.student.model.command.CreateStudentCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;

    @GetMapping
    public List<StudentDTO> findAll() {
        return studentService.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        studentService.deleteById(id);
    }

    @GetMapping("/{id}")
    public StudentDTO findById(@PathVariable long id) {
        return studentService.findById(id);
    }

    @PostMapping
    public StudentDTO save(@RequestBody CreateStudentCommand studentCommand) {
        return studentService.save(studentCommand);
    }

    @PatchMapping("/{studentId}")
    public void changeTeacher(@PathVariable Long studentId, @RequestParam Long newTeacherId) {
        studentService.changeTeacher(studentId, newTeacherId);

    }

    @GetMapping(params = "teacher")
    public List<StudentDTO> findAllByTeacher(@RequestParam("teacher") Long teacherId) {
        return studentService.findAllByTeacher(teacherId);
    }


}
