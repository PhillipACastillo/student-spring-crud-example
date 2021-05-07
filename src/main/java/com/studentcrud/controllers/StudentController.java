package com.studentcrud.controllers;

import com.studentcrud.domain.Student;
import com.studentcrud.repositories.StudentRepository;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class StudentController {
    StudentRepository repository;

    public StudentController(StudentRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/students")
    public List<Student> retrieveAllStudents() {
        return repository.findAll();
    }

    @GetMapping("/students/{id}")
    public Student retrieveStudent(@PathVariable Long id) throws Exception {
        Optional<Student> student = repository.findById(id);

        if (student.isEmpty()) {
            throw new Exception();
        }

        return student.get();
    }

    @DeleteMapping("/students/{id}")
    public void deleteStudent(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @PostMapping("/students")
    public Student newStudent (@RequestBody Student student) {
        return repository.save(student);
    }

    @RequestMapping(value ="/students/{id}", method = RequestMethod.PATCH)
    public void patchStudent(@PathVariable Long id, @RequestBody Map<String,
        Object> fields) throws Exception {
            Student student = retrieveStudent(id);
            fields.forEach((attribute, value) -> {
                Field field = ReflectionUtils.findField(Student.class,
                        attribute);
                if (field != null) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, student, value);
                }
            });
            repository.save(student);
    }
}
