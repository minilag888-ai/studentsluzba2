package org.raflab.studsluzba.controllers;

import org.raflab.studsluzba.controllers.request.IspitniRokRequest;
import org.raflab.studsluzba.controllers.response.IspitniRokResponse;
import org.raflab.studsluzba.model.IspitniRok;
import org.raflab.studsluzba.model.SkolskaGodina;
import org.raflab.studsluzba.services.IspitniRokService;
import org.raflab.studsluzba.services.SkolskaGodinaService;
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
@RequestMapping(path = "/api/ispitni-rokovi")
public class IspitniRokController {

    @Autowired
    private IspitniRokService service;

    @Autowired
    private SkolskaGodinaService skolskaGodinaService;

    @GetMapping(path = "/all")
    @Transactional(readOnly = true)
    public List<IspitniRokResponse> getAll() {
        List<IspitniRok> rokovi = service.findAll();
        List<IspitniRokResponse> responses = new ArrayList<>();
        for (IspitniRok rok : rokovi) {
            responses.add(Converters.toIspitniRokResponse(rok));
        }
        return responses;
    }

    @GetMapping(path = "/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<IspitniRokResponse> getById(@PathVariable Long id) {
        IspitniRok rok = service.findById(id).orElse(null);
        if (rok == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Converters.toIspitniRokResponse(rok));
    }

    @GetMapping(path = "/godina/{godinaId}")
    @Transactional(readOnly = true)
    public List<IspitniRokResponse> getByGodina(@PathVariable Long godinaId) {
        List<IspitniRok> rokovi = service.findBySkolskaGodina(godinaId);
        List<IspitniRokResponse> responses = new ArrayList<>();
        for (IspitniRok rok : rokovi) {
            responses.add(Converters.toIspitniRokResponse(rok));
        }
        return responses;
    }

    @PostMapping(path = "/add")
    @Transactional
    public ResponseEntity<IspitniRokResponse> add(@RequestBody @Valid IspitniRokRequest request) {
        SkolskaGodina godina = skolskaGodinaService.findById(request.getSkolskaGodinaId())
                .orElseThrow(() -> new RuntimeException("SkolskaGodina not found"));

        IspitniRok rok = Converters.toIspitniRok(request, godina);
        IspitniRok saved = service.save(rok);

        return ResponseEntity.ok(Converters.toIspitniRokResponse(saved));
    }

    @PutMapping(path = "/{id}")
    @Transactional
    public ResponseEntity<IspitniRokResponse> update(@PathVariable Long id,
                                                     @RequestBody @Valid IspitniRokRequest request) {
        if (!service.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        SkolskaGodina godina = skolskaGodinaService.findById(request.getSkolskaGodinaId())
                .orElseThrow(() -> new RuntimeException("SkolskaGodina not found"));

        IspitniRok rok = Converters.toIspitniRok(request, godina);
        rok.setId(id);
        IspitniRok updated = service.save(rok);

        return ResponseEntity.ok(Converters.toIspitniRokResponse(updated));
    }

    @DeleteMapping(path = "/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!service.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}