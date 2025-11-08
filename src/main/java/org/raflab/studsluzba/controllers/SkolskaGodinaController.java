package org.raflab.studsluzba.controllers;

import org.raflab.studsluzba.controllers.request.SkolskaGodinaRequest;
import org.raflab.studsluzba.controllers.response.SkolskaGodinaResponse;
import org.raflab.studsluzba.model.SkolskaGodina;
import org.raflab.studsluzba.services.SkolskaGodinaService;
import org.raflab.studsluzba.utils.Converters;
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

    @GetMapping(path = "/all")
    public List<SkolskaGodinaResponse> getAll() {
        return Converters.toSkolskaGodinaResponseList(service.findAll());
    }

    @GetMapping(path = "/{id}")
    public SkolskaGodinaResponse getById(@PathVariable Long id) {
        Optional<SkolskaGodina> optional = service.findById(id);
        return optional.map(Converters::toSkolskaGodinaResponse).orElse(null);
    }

    @GetMapping(path = "/aktivna")
    public SkolskaGodinaResponse getAktivna() {
        Optional<SkolskaGodina> optional = service.findAktivna();
        return optional.map(Converters::toSkolskaGodinaResponse).orElse(null);
    }

    @PostMapping(path = "/add")
    public SkolskaGodinaResponse add(@RequestBody @Valid SkolskaGodinaRequest request) {
        SkolskaGodina godina = Converters.toSkolskaGodina(request);
        SkolskaGodina saved = service.save(godina);
        return Converters.toSkolskaGodinaResponse(saved);
    }

    @PutMapping(path = "/{id}/aktiviraj")
    public SkolskaGodinaResponse aktiviraj(@PathVariable Long id) {
        SkolskaGodina godina = service.postaviAktivnu(id);
        return godina != null ? Converters.toSkolskaGodinaResponse(godina) : null;
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }
}