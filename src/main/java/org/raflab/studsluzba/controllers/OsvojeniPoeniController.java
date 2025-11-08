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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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
        return Converters.toOsvojeniPoeniResponseList(service.findAll());
    }

    @GetMapping(path = "/{id}")
    public OsvojeniPoeniResponse getById(@PathVariable Long id) {
        Optional<OsvojeniPoeni> optional = service.findById(id);
        return optional.map(Converters::toOsvojeniPoeniResponse).orElse(null);
    }

    @GetMapping(path = "/student/{studentId}")
    public List<OsvojeniPoeniResponse> getByStudent(@PathVariable Long studentId) {
        List<OsvojeniPoeni> poeni = service.findByStudent(studentId);
        return Converters.toOsvojeniPoeniResponseList(poeni);
    }

    @GetMapping(path = "/student/{studentId}/predmet/{predmetId}/godina/{godinaId}")
    public List<OsvojeniPoeniResponse> getByStudentPredmetGodina(
            @PathVariable Long studentId,
            @PathVariable Long predmetId,
            @PathVariable Long godinaId) {
        List<OsvojeniPoeni> poeni = service.findByStudentPredmetGodina(studentId, predmetId, godinaId);
        return Converters.toOsvojeniPoeniResponseList(poeni);
    }

    @PostMapping(path = "/add")
    @Transactional
    public OsvojeniPoeniResponse add(@RequestBody @Valid OsvojeniPoeniRequest request) {
        System.out.println("=== DEBUGGING ===");
        System.out.println("REQUEST: " + request);

        StudentIndeks indeks = studentIndeksService.findById(request.getStudentIndeksId()).orElse(null);
        System.out.println("STUDENT INDEKS: " + indeks);

        PredispitnaObaveza obaveza = predispitnaObavezaService.findById(request.getPredispitnaObavezaId()).orElse(null);
        System.out.println("PREDISPITNA OBAVEZA: " + obaveza);

        if (indeks == null || obaveza == null) {
            System.out.println("❌ NULL! Indeks=" + indeks + ", Obaveza=" + obaveza);
            return null;
        }

        System.out.println("✅ Kreiram OsvojeniPoeni...");
        OsvojeniPoeni poeni = Converters.toOsvojeniPoeni(request, indeks, obaveza);
        System.out.println("POENI OBJEKAT: " + poeni);

        OsvojeniPoeni saved = service.save(poeni);
        System.out.println("SAVED: " + saved);

        OsvojeniPoeniResponse response = Converters.toOsvojeniPoeniResponse(saved);
        System.out.println("RESPONSE: " + response);

        return response;
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }

}