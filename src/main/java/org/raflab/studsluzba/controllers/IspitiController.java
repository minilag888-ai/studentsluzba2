package org.raflab.studsluzba.controllers;

import org.raflab.studsluzba.controllers.request.IspitRequest;
import org.raflab.studsluzba.controllers.response.IspitResponse;
import org.raflab.studsluzba.model.Ispit;
import org.raflab.studsluzba.model.IspitniRok;
import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.model.StudentIndeks;
import org.raflab.studsluzba.services.IspitiService;
import org.raflab.studsluzba.services.IspitniRokService;
import org.raflab.studsluzba.services.PredmetService;
import org.raflab.studsluzba.services.StudentIndeksService;
import org.raflab.studsluzba.utils.Converters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/ispiti")
@Transactional
public class IspitiController {

    @Autowired
    private IspitiService service;

    @Autowired
    private StudentIndeksService studentIndeksService;

    @Autowired
    private PredmetService predmetService;

    @Autowired
    private IspitniRokService ispitniRokService;

    @GetMapping(path = "/all")
    public List<IspitResponse> getAll() {
        List<Ispit> ispiti = service.findAll();
        List<IspitResponse> responses = new ArrayList<>();
        for (Ispit ispit : ispiti) {
            responses.add(Converters.toIspitResponse(ispit));
        }
        return responses;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<IspitResponse> getById(@PathVariable Long id) {
        Ispit ispit = service.findById(id).orElse(null);
        if (ispit == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Converters.toIspitResponse(ispit));
    }

    @GetMapping(path = "/student/{studentId}")
    public List<IspitResponse> getByStudent(@PathVariable Long studentId) {
        List<Ispit> ispiti = service.findByStudent(studentId);
        List<IspitResponse> responses = new ArrayList<>();
        for (Ispit ispit : ispiti) {
            responses.add(Converters.toIspitResponse(ispit));
        }
        return responses;
    }

    @GetMapping(path = "/predmet/{predmetId}/rok/{rokId}")
    public List<IspitResponse> getByPredmetAndRok(@PathVariable Long predmetId, @PathVariable Long rokId) {
        List<Ispit> ispiti = service.findByPredmetAndRok(predmetId, rokId);
        List<IspitResponse> responses = new ArrayList<>();
        for (Ispit ispit : ispiti) {
            responses.add(Converters.toIspitResponse(ispit));
        }
        return responses;
    }

    @PostMapping(path = "/add")
    public ResponseEntity<IspitResponse> add(@RequestBody @Valid IspitRequest request) {
        StudentIndeks indeks = studentIndeksService.findById(request.getStudentIndeksId())
                .orElseThrow(() -> new RuntimeException("StudentIndeks not found"));

        Predmet predmet = predmetService.findById(request.getPredmetId())
                .orElseThrow(() -> new RuntimeException("Predmet not found"));

        IspitniRok rok = ispitniRokService.findById(request.getIspitniRokId())
                .orElseThrow(() -> new RuntimeException("IspitniRok not found"));

        Ispit ispit = Converters.toIspit(request, indeks, predmet, rok);
        Ispit saved = service.save(ispit);

        return ResponseEntity.ok(Converters.toIspitResponse(saved));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<IspitResponse> update(@PathVariable Long id, @RequestBody @Valid IspitRequest request) {
        if (!service.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        StudentIndeks indeks = studentIndeksService.findById(request.getStudentIndeksId())
                .orElseThrow(() -> new RuntimeException("StudentIndeks not found"));

        Predmet predmet = predmetService.findById(request.getPredmetId())
                .orElseThrow(() -> new RuntimeException("Predmet not found"));

        IspitniRok rok = ispitniRokService.findById(request.getIspitniRokId())
                .orElseThrow(() -> new RuntimeException("IspitniRok not found"));

        Ispit ispit = Converters.toIspit(request, indeks, predmet, rok);
        ispit.setId(id);
        Ispit updated = service.save(ispit);

        return ResponseEntity.ok(Converters.toIspitResponse(updated));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!service.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}