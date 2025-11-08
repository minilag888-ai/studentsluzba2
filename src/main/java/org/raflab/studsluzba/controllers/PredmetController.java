package org.raflab.studsluzba.controllers;

import org.raflab.studsluzba.controllers.request.PredmetRequest;
import org.raflab.studsluzba.controllers.response.PredmetResponse;
import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.model.StudijskiProgram;
import org.raflab.studsluzba.services.PredmetService;
import org.raflab.studsluzba.services.StudijskiProgramService;
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
@RequestMapping(path = "/api/predmeti")
@Transactional
public class PredmetController {

    @Autowired
    private PredmetService service;

    @Autowired
    private StudijskiProgramService studijskiProgramService;

    @GetMapping(path = "/all")
    public List<PredmetResponse> getAll() {
        List<Predmet> predmeti = service.findAll();
        List<PredmetResponse> responses = new ArrayList<>();
        for (Predmet predmet : predmeti) {
            responses.add(Converters.toPredmetResponse(predmet));
        }
        return responses;
    }

    @GetMapping(path = "/all/{godinaAkreditacije}")
    public List<PredmetResponse> getByGodinaAkreditacije(@PathVariable Integer godinaAkreditacije) {
        List<Predmet> predmeti = service.findByGodinaAkreditacije(godinaAkreditacije);
        List<PredmetResponse> responses = new ArrayList<>();
        for (Predmet predmet : predmeti) {
            responses.add(Converters.toPredmetResponse(predmet));
        }
        return responses;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<PredmetResponse> getById(@PathVariable Long id) {
        Predmet predmet = service.findById(id).orElse(null);
        if (predmet == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Converters.toPredmetResponse(predmet));
    }

    @GetMapping(path = "/sifra/{sifra}")
    public ResponseEntity<PredmetResponse> getBySifra(@PathVariable String sifra) {
        Predmet predmet = service.findBySifra(sifra).orElse(null);
        if (predmet == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Converters.toPredmetResponse(predmet));
    }

    @PostMapping(path = "/add")
    public ResponseEntity<PredmetResponse> add(@RequestBody @Valid PredmetRequest request) {
        // Proveri da li šifra već postoji
        if (service.existsBySifra(request.getSifra())) {
            return ResponseEntity.badRequest().build();
        }

        StudijskiProgram program = null;
        if (request.getStudijskiProgramId() != null) {
            program = studijskiProgramService.findById(request.getStudijskiProgramId())
                    .orElseThrow(() -> new RuntimeException("StudijskiProgram not found"));
        }

        Predmet predmet = Converters.toPredmet(request, program);
        Predmet saved = service.save(predmet);

        return ResponseEntity.ok(Converters.toPredmetResponse(saved));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<PredmetResponse> update(@PathVariable Long id,
                                                  @RequestBody @Valid PredmetRequest request) {
        if (!service.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        // Proveri da li nova šifra već postoji (ako se menja)
        Predmet existing = service.findById(id).get();
        if (!existing.getSifra().equals(request.getSifra()) && service.existsBySifra(request.getSifra())) {
            return ResponseEntity.badRequest().build();
        }

        StudijskiProgram program = null;
        if (request.getStudijskiProgramId() != null) {
            program = studijskiProgramService.findById(request.getStudijskiProgramId())
                    .orElseThrow(() -> new RuntimeException("StudijskiProgram not found"));
        }

        Predmet predmet = Converters.toPredmet(request, program);
        predmet.setId(id);
        Predmet updated = service.save(predmet);

        return ResponseEntity.ok(Converters.toPredmetResponse(updated));
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