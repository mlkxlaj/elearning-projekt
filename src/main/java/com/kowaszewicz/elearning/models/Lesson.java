package com.kowaszewicz.elearning.models;


import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Lesson {

    private int lessonId;
    private String date;
    private String teacherName;
    private String studentName;
    private String topic;

}
