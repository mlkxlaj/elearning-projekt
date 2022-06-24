package com.kowaszewicz.elearning.controllers;

import com.kowaszewicz.elearning.models.Lesson;
import com.kowaszewicz.elearning.models.Student;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/lessons")
public class LessonController {
    private List<Lesson> lessons = new ArrayList<>();

    @PostConstruct
    public void init() {
        lessons.add(new Lesson(2, "19.06.2022", "Kowalski", "Antek", "Math"));
        lessons.add(new Lesson(1, "13.06.2022", "Kowalski", "Antek", "Math"));
    }

    @GetMapping
    public ResponseEntity getAllLessons() {
        return ResponseEntity.ok(lessons);
    }

    @GetMapping("/{lessonId}")
    public ResponseEntity getLesson(@PathVariable int lessonId) {
        return lessons.stream()
                .filter(lesson -> lesson.getLessonId() == lessonId)
                .findAny()
                .map(lesson -> new ResponseEntity(lesson, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity("Nie znaleziono lekcji", HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity addLesson(@RequestBody Lesson lesson) {
        if (lessons.stream().anyMatch(l -> l.getLessonId() == lesson.getLessonId())) {
            return new ResponseEntity("Juz jest taka lekcja o tym numerze id", HttpStatus.BAD_REQUEST);
        }
        lessons.add(lesson);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @DeleteMapping("/{lessonId}")
    public ResponseEntity removeLesson(@PathVariable int lessonId) {
        if (lessons.removeIf(lesson -> lesson.getLessonId() == lessonId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping
    public ResponseEntity editLesson(@RequestBody Lesson upadtedLesson) {
        if (lessons.stream().noneMatch(lesson -> lesson.getLessonId() == upadtedLesson.getLessonId())) {
            return new ResponseEntity("Brak lesson o danym id", HttpStatus.BAD_REQUEST);
        }
        Lesson lesson = lessons.stream().filter(l -> l.getLessonId() == upadtedLesson.getLessonId()).findAny().get();
        lesson.setDate(upadtedLesson.getDate());
        lesson.setStudentName(upadtedLesson.getStudentName());
        lesson.setTeacherName(upadtedLesson.getTeacherName());
        lesson.setTopic(upadtedLesson.getTopic());
        return new ResponseEntity(lesson, HttpStatus.OK);
    }


}
