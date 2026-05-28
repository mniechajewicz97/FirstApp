package com.example.demo.student.model;

import com.example.demo.common.Language;
import com.example.demo.teacher.model.Teacher;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor //bezargumentowy konstruktor
@Builder // design pattern, ktory upraszcza tworzenie obiektow

public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstName;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private Language language;
    @ManyToOne
    private Teacher teacher;

}
