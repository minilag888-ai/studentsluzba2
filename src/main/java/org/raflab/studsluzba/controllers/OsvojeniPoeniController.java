package org.raflab.studsluzba.controllers;

import org.raflab.studsluzba.controllers.request.OsvojeniPoeniRequest;
import org.raflab.studsluzba.controllers.response.OsvojeniPoeniResponse;
import org.raflab.studsluzba.model.OsvojeniPoeni;
import org.raflab.studsluzba.model.PredispitnaObaveza;
import org.raflab.studsluzba.model.StudentIndeks;
import org.raflab.studsluzba.services.OsvojeniPoeniService;
import org.raflab.studsluzba.services.PredispitnaObavezaService;
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
@RequestMapping(path = "/api/osvojeni-poeni")
public class OsvojeniPoeniController {

    @Autowired
    private OsvojeniPoeniService service;

    @Autowired
    private StudentIndeksService studentIndeksService;

    @Autowired
    private PredispitnaObavezaService predispitnaObavezaService;

    @GetMapping(path = "/all")
    public List<OsvojeniPoeniResponse> getAll() {
        List<OsvojeniPoeni> poeni = service.findAll();
        List<OsvojeniPoeniResponse> responses = new ArrayList<>();
        for (OsvojeniPoeni p : poeni) {
            responses.add(Converters.toOsvojeniPoeniResponse(p));
        }
        return responses;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<OsvojeniPoeniResponse> getById(@PathVariable Long id) {
        OsvojeniPoeni poeni = service.findById(id).orElse(null);
        if (poeni == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Converters.toOsvojeniPoeniResponse(poeni));
    }

    @GetMapping(path = "/student/{studentId}")
    public List<OsvojeniPoeniResponse> getByStudent(@PathVariable Long studentId) {
        List<OsvojeniPoeni> poeni = service.findByStudent(studentId);
        List<OsvojeniPoeniResponse> responses = new ArrayList<>();
        for (OsvojeniPoeni p : poeni) {
            responses.add(Converters.toOsvojeniPoeniResponse(p));
        }
        return responses;
    }

    @GetMapping(path = "/predmet/{predmetId}/godina/{godinaId}")
    public List<OsvojeniPoeniResponse> getByPredmetGodina(
            @PathVariable Long predmetId,
            @PathVariable Long godinaId) {
        List<OsvojeniPoeni> poeni = service.findByPredmetAndGodina(predmetId, godinaId);
        List<OsvojeniPoeniResponse> responses = new ArrayList<>();
        for (OsvojeniPoeni p : poeni) {
            responses.add(Converters.toOsvojeniPoeniResponse(p));
        }
        return responses;
    }

    @PostMapping(path = "/add")
    @Transactional
    public OsvojeniPoeniResponse add(@RequestBody @Valid OsvojeniPoeniRequest request) {
        StudentIndeks indeks = studentIndeksService.findById(request.getStudentIndeksId())
                .orElseThrow(() -> new RuntimeException("StudentIndeks not found"));

        PredispitnaObaveza obaveza = predispitnaObavezaService.findById(request.getPredispitnaObavezaId())
                .orElseThrow(() -> new RuntimeException("PredispitnaObaveza not found"));

        OsvojeniPoeni poeni = Converters.toOsvojeniPoeni(request, indeks, obaveza);
        OsvojeniPoeni saved = service.save(poeni);

        return Converters.toOsvojeniPoeniResponse(saved);
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