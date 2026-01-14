package org.raflab.studsluzba.controllers;

import org.raflab.studsluzba.controllers.request.PredispitnaObavezaRequest;
import org.raflab.studsluzba.controllers.response.PredispitnaObavezaResponse;
import org.raflab.studsluzba.mappers.PredispitnaObavezaMapper;
import org.raflab.studsluzba.model.PredispitnaObaveza;
import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.model.SkolskaGodina;
import org.raflab.studsluzba.services.PredispitnaObavezaService;
import org.raflab.studsluzba.services.PredmetService;
import org.raflab.studsluzba.services.SkolskaGodinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/predispitna-obaveza")
public class PredispitnaObavezaController {

    @Autowired
    private PredispitnaObavezaService service;

    @Autowired
    private PredmetService predmetService;

    @Autowired
    private SkolskaGodinaService skolskaGodinaService;

    @Autowired
    private PredispitnaObavezaMapper mapper;

    @GetMapping(path = "/all")
    public List<PredispitnaObavezaResponse> getAll() {
        return mapper.toResponseList(service.findAll());
    }

    @GetMapping(path = "/{id}")
    public PredispitnaObavezaResponse getById(@PathVariable Long id) {
        Optional<PredispitnaObaveza> optional = service.findById(id);
        return optional.map(mapper::toResponse).orElse(null);
    }

    @GetMapping(path = "/predmet/{predmetId}/skolska-godina/{godinaId}")
    public List<PredispitnaObavezaResponse> getByPredmetAndGodina(
            @PathVariable Long predmetId,
            @PathVariable Long godinaId) {
        List<PredispitnaObaveza> obaveze = service.findByPredmetAndSkolskaGodina(predmetId, godinaId);
        return mapper.toResponseList(obaveze);
    }

    @PostMapping(path = "/add")
    public PredispitnaObavezaResponse add(@RequestBody @Valid PredispitnaObavezaRequest request) {
        Predmet predmet = predmetService.findById(request.getPredmetId()).orElse(null);
        SkolskaGodina godina = skolskaGodinaService.findById(request.getSkolskaGodinaId()).orElse(null);

        if (predmet == null || godina == null) {
            return null;
        }

        PredispitnaObaveza obaveza = mapper.toEntity(request, predmet, godina);
        PredispitnaObaveza saved = service.save(obaveza);
        return mapper.toResponse(saved);
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }
}