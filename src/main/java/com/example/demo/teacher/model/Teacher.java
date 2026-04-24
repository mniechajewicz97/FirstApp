package com.example.demo.teacher.model;

import com.example.demo.common.Language;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Teacher {
    private long id;
    private String firstName;
    private String lastName;
    private List<Language> languages = new ArrayList<>();
}
