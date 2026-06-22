//package com.example.demo.common.mapper;
//
//import com.example.demo.common.dto.StudentDTO;
//import com.example.demo.student.model.Student;
//
//public class StudentMapper {
//
//    public static StudentDTO mapToDto(Student student) {
//        return new StudentDTO(student.getId(), student.getFirstName(), student.getLastName());
//    }
//
//    public  static Student mapToEntity(StudentDTO studentDTO) {
//       return Student.builder()
//               .id(studentDTO.getId())
//               .firstName(studentDTO.getFirstName())
//               .lastName(studentDTO.getLastName())
//               .build();
//
//
//    }
//}
