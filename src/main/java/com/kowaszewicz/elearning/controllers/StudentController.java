package com.kowaszewicz.elearning.controllers;

import com.kowaszewicz.elearning.models.Student;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/students")
public class StudentController {
    private List<Student> students = new ArrayList<>();

    @PostConstruct
    public void init() {
        students.add(new Student("Mirosław Kowalski", "mirek@gmail.com", "Michał Leja", 200));
        students.add(new Student("Jakub Nowicki", "jakub8899wp.pl", "Michał Leja", 200));
    }

    @GetMapping
    public ResponseEntity getAllStudents() {
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{email}")
    public ResponseEntity getStudent(@PathVariable String email) {
        return students.stream()
                .filter(student -> student.getEmail().equals(email))
                .findAny()
                .map(student -> new ResponseEntity(student, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity("Nie znaleziono studenta o emailu " + email, HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity addStudent(@RequestBody Student student) {
        if (students.stream().anyMatch(s -> s.getEmail().equals(student.getEmail()))) {
            return new ResponseEntity("Konto z takim adresem email juz istnieje", HttpStatus.BAD_REQUEST);
        }
        students.add(student);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity removeStudent(@PathVariable String email) {
        if (students.removeIf(student -> student.getEmail().equals(email))) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping
    public ResponseEntity editStudent(@RequestBody Student updatedStudent) {
        if (students.stream().noneMatch(student -> student.getEmail().equals(updatedStudent.getEmail()))) {
            return new ResponseEntity("Brak studenta o danym emailu", HttpStatus.BAD_REQUEST);
        }
        Student student = students.stream().filter(s -> s.getEmail().equals(updatedStudent.getEmail())).findAny().get();
        student.setEmail(updatedStudent.getEmail());
        student.setName(updatedStudent.getName());
        student.setTeacher(updatedStudent.getTeacher());
        student.setRate(updatedStudent.getRate());
        return new ResponseEntity(student, HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity editStudentPartially(@RequestBody Student updatedStudent) {
        if (students.stream().noneMatch(student -> student.getEmail().equals(updatedStudent.getEmail()))) {
            return new ResponseEntity("Brak studenta o danym emailu", HttpStatus.BAD_REQUEST);
        }
        Student student = students.stream().filter(s -> s.getEmail().equals(updatedStudent.getEmail())).findAny().get();
        Optional.ofNullable(updatedStudent.getName()).ifPresent(student::setName);
        Optional.ofNullable(updatedStudent.getTeacher()).ifPresent(student::setTeacher);
        Optional.ofNullable(updatedStudent.getRate()).ifPresent(student::setRate);
        return new ResponseEntity(student, HttpStatus.OK);
    }


}
