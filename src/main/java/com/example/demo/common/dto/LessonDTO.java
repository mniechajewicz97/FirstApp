package com.example.demo.common.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class LessonDTO {
    private Long id;
    private String studentFullName;
    private String teacherFullName;
    private LocalDateTime lessonDate;


}
