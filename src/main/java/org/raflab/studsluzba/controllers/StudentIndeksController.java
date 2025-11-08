package org.raflab.studsluzba.controllers;

import org.raflab.studsluzba.model.StudentIndeks;
import org.raflab.studsluzba.services.StudentIndeksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/student-indeks")
public class StudentIndeksController {

    @Autowired
    private StudentIndeksService service;

    @GetMapping(path = "/all")
    public List<StudentIndeks> getAll() {
        return service.findAll();
    }

    @GetMapping(path = "/{id}")
    public StudentIndeks getById(@PathVariable Long id) {
        Optional<StudentIndeks> optional = service.findById(id);
        return optional.orElse(null);
    }
}