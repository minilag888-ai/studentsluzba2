package org.raflab.studsluzba.controllers;

import org.raflab.studsluzba.controllers.request.IspitniRokRequest;
import org.raflab.studsluzba.controllers.response.IspitniRokResponse;
import org.raflab.studsluzba.mappers.IspitniRokMapper;
import org.raflab.studsluzba.model.IspitniRok;
import org.raflab.studsluzba.model.SkolskaGodina;
import org.raflab.studsluzba.services.IspitniRokService;
import org.raflab.studsluzba.services.SkolskaGodinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/ispitni-rokovi")
public class IspitniRokController {

    @Autowired
    private IspitniRokService service;

    @Autowired
    private SkolskaGodinaService skolskaGodinaService;

    @Autowired
    private IspitniRokMapper mapper;

    @GetMapping(path = "/all")
    @Transactional(readOnly = true)
    public List<IspitniRokResponse> getAll() {
        return mapper.toResponseList(service.findAll());
    }

    @GetMapping(path = "/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<IspitniRokResponse> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(mapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(path = "/godina/{godinaId}")
    @Transactional(readOnly = true)
    public List<IspitniRokResponse> getByGodina(@PathVariable Long godinaId) {
        return mapper.toResponseList(service.findBySkolskaGodina(godinaId));
    }

    @PostMapping(path = "/add")
    @Transactional
    public ResponseEntity<IspitniRokResponse> add(@RequestBody @Valid IspitniRokRequest request) {
        SkolskaGodina godina = skolskaGodinaService.findById(request.getSkolskaGodinaId())
                .orElseThrow(() -> new RuntimeException("SkolskaGodina not found"));

        IspitniRok rok = mapper.toEntity(request, godina);
        IspitniRok saved = service.save(rok);

        return ResponseEntity.ok(mapper.toResponse(saved));
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

        IspitniRok rok = mapper.toEntity(request, godina);
        rok.setId(id);
        IspitniRok updated = service.save(rok);

        return ResponseEntity.ok(mapper.toResponse(updated));
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