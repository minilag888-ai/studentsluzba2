package org.raflab.studsluzba.controllers;

import org.raflab.studsluzba.controllers.request.OsvojeniPoeniRequest;
import org.raflab.studsluzba.controllers.response.OsvojeniPoeniResponse;
import org.raflab.studsluzba.mappers.OsvojeniPoeniMapper;
import org.raflab.studsluzba.model.OsvojeniPoeni;
import org.raflab.studsluzba.model.PredispitnaObaveza;
import org.raflab.studsluzba.model.StudentIndeks;
import org.raflab.studsluzba.services.OsvojeniPoeniService;
import org.raflab.studsluzba.services.PredispitnaObavezaService;
import org.raflab.studsluzba.services.StudentIndeksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @Autowired
    private OsvojeniPoeniMapper mapper;

    @GetMapping(path = "/all")
    public List<OsvojeniPoeniResponse> getAll() {
        return mapper.toResponseList(service.findAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<OsvojeniPoeniResponse> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(mapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(path = "/student/{studentId}")
    public List<OsvojeniPoeniResponse> getByStudent(@PathVariable Long studentId) {
        return mapper.toResponseList(service.findByStudent(studentId));
    }

    @GetMapping(path = "/predmet/{predmetId}/godina/{godinaId}")
    public List<OsvojeniPoeniResponse> getByPredmetGodina(
            @PathVariable Long predmetId,
            @PathVariable Long godinaId) {
        return mapper.toResponseList(service.findByPredmetAndGodina(predmetId, godinaId));
    }

    @PostMapping(path = "/add")
    @Transactional
    public OsvojeniPoeniResponse add(@RequestBody @Valid OsvojeniPoeniRequest request) {
        StudentIndeks indeks = studentIndeksService.findById(request.getStudentIndeksId())
                .orElseThrow(() -> new RuntimeException("StudentIndeks not found"));

        PredispitnaObaveza obaveza = predispitnaObavezaService.findById(request.getPredispitnaObavezaId())
                .orElseThrow(() -> new RuntimeException("PredispitnaObaveza not found"));

        OsvojeniPoeni poeni = mapper.toEntity(request, indeks, obaveza);
        OsvojeniPoeni saved = service.save(poeni);

        return mapper.toResponse(saved);
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