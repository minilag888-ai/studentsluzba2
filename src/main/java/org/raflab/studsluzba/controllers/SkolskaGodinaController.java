package org.raflab.studsluzba.controllers;

import org.raflab.studsluzba.controllers.request.SkolskaGodinaRequest;
import org.raflab.studsluzba.controllers.response.SkolskaGodinaResponse;
import org.raflab.studsluzba.mappers.SkolskaGodinaMapper;
import org.raflab.studsluzba.model.SkolskaGodina;
import org.raflab.studsluzba.services.SkolskaGodinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/skolska-godina")
public class SkolskaGodinaController {

    @Autowired
    private SkolskaGodinaService service;

    @Autowired
    private SkolskaGodinaMapper mapper;

    @GetMapping(path = "/all")
    public List<SkolskaGodinaResponse> getAll() {
        return mapper.toResponseList(service.findAll());
    }

    @GetMapping(path = "/{id}")
    public SkolskaGodinaResponse getById(@PathVariable Long id) {
        Optional<SkolskaGodina> optional = service.findById(id);
        return optional.map(mapper::toResponse).orElse(null);
    }

    @GetMapping(path = "/aktivna")
    public SkolskaGodinaResponse getAktivna() {
        Optional<SkolskaGodina> optional = service.findAktivna();
        return optional.map(mapper::toResponse).orElse(null);
    }

    @PostMapping(path = "/add")
    public SkolskaGodinaResponse add(@RequestBody @Valid SkolskaGodinaRequest request) {
        SkolskaGodina godina = mapper.toEntity(request);
        SkolskaGodina saved = service.save(godina);
        return mapper.toResponse(saved);
    }

    @PutMapping(path = "/{id}/aktiviraj")
    public SkolskaGodinaResponse aktiviraj(@PathVariable Long id) {
        SkolskaGodina godina = service.postaviAktivnu(id);
        return godina != null ? mapper.toResponse(godina) : null;
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }
}