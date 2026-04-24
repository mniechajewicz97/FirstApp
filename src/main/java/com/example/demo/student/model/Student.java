package com.example.demo.student.model;

import com.example.demo.common.Language;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor //bezargumentowy konstruktor
@Builder // design pattern, ktory upraszcza tworzenie obiektow

public class Student {
    private long id;
    private String firstName;
    private String lastName;
    private Language language;


}
