package com.example.demo.teacher.model;

import com.example.demo.common.Language;
import com.example.demo.student.model.Student;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstName;
    private String lastName;
    private boolean deleted;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER) // pobiera wszystkie jezyki dla nauczyciela od razu, bazowo jest LAZY wiec za kazdym razem jak odnosisz sie do jezykow to wykonuje sie dodatkowe zapytanie to DB
    @CollectionTable(name =  "teacher_language", joinColumns = @JoinColumn(name = "teacher_id"))
    @Column(name = "language")
    private Set<Language> languages;

    @OneToMany(mappedBy = "teacher") // oznaczamy to tak, aby studenci mogli byc wyszukiwani po id teachera
    private Set<Student> students;

}
