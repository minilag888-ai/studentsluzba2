package org.raflab.studsluzba.controllers;

import org.raflab.studsluzba.controllers.request.PredmetRequest;
import org.raflab.studsluzba.controllers.response.PredmetResponse;
import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.model.StudijskiProgram;
import org.raflab.studsluzba.model.dtos.PredmetDTO;
import org.raflab.studsluzba.model.dtos.ProsecnaOcenaDTO;
import org.raflab.studsluzba.services.PredmetService;
import org.raflab.studsluzba.services.StudijskiProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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
        return service.findAll().stream()
                .map(this::toPredmetResponse)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/all/{godinaAkreditacije}")
    public List<PredmetResponse> getByGodinaAkreditacije(@PathVariable Integer godinaAkreditacije) {
        return service.findByGodinaAkreditacije(godinaAkreditacije).stream()
                .map(this::toPredmetResponse)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<PredmetResponse> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(this::toPredmetResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(path = "/sifra/{sifra}")
    public ResponseEntity<PredmetResponse> getBySifra(@PathVariable String sifra) {
        return service.findBySifra(sifra)
                .map(this::toPredmetResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(path = "/add")
    public ResponseEntity<PredmetResponse> add(@RequestBody @Valid PredmetRequest request) {
        if (service.existsBySifra(request.getSifra())) {
            return ResponseEntity.badRequest().build();
        }

        StudijskiProgram program = null;
        if (request.getStudijskiProgramId() != null) {
            program = studijskiProgramService.findById(request.getStudijskiProgramId())
                    .orElseThrow(() -> new RuntimeException("StudijskiProgram not found"));
        }

        Predmet predmet = toEntity(request, program);
        Predmet saved = service.save(predmet);

        return ResponseEntity.ok(toPredmetResponse(saved));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<PredmetResponse> update(@PathVariable Long id,
                                                  @RequestBody @Valid PredmetRequest request) {
        if (!service.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        Predmet existing = service.findById(id).get();
        if (!existing.getSifra().equals(request.getSifra()) && service.existsBySifra(request.getSifra())) {
            return ResponseEntity.badRequest().build();
        }

        StudijskiProgram program = null;
        if (request.getStudijskiProgramId() != null) {
            program = studijskiProgramService.findById(request.getStudijskiProgramId())
                    .orElseThrow(() -> new RuntimeException("StudijskiProgram not found"));
        }

        Predmet predmet = toEntity(request, program);
        predmet.setId(id);
        Predmet updated = service.save(predmet);

        return ResponseEntity.ok(toPredmetResponse(updated));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!service.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // NOVI ENDPOINTI ZA SPECIFIKACIJU

    @GetMapping(path = "/studijski-program/{id}")
    public ResponseEntity<List<PredmetDTO>> getPredmetiNaStudijskomProgramu(@PathVariable Long id) {
        List<PredmetDTO> predmeti = service.getPredmetiNaStudijskomProgramu(id);
        return ResponseEntity.ok(predmeti);
    }

    @GetMapping(path = "/{id}/prosecna-ocena")
    public ResponseEntity<ProsecnaOcenaDTO> getProsecnaOcena(
            @PathVariable Long id,
            @RequestParam Integer odGodine,
            @RequestParam Integer doGodine) {

        ProsecnaOcenaDTO dto = service.getProsecnaOcenaNaPredmetu(id, odGodine, doGodine);
        return ResponseEntity.ok(dto);
    }

    // Helper metode - OVDE OSTAVLJAMO JER SU SPECIFIČNE ZA CONTROLLER
    private Predmet toEntity(PredmetRequest request, StudijskiProgram program) {
        Predmet predmet = new Predmet();
        predmet.setSifra(request.getSifra());
        predmet.setNaziv(request.getNaziv());
        predmet.setOpis(request.getOpis());
        predmet.setEspb(request.getEspb());
        predmet.setStudProgram(program);
        predmet.setObavezan(request.getObavezan());
        return predmet;
    }

    private PredmetResponse toPredmetResponse(Predmet predmet) {
        PredmetResponse response = new PredmetResponse();
        response.setId(predmet.getId());
        response.setSifra(predmet.getSifra());
        response.setNaziv(predmet.getNaziv());
        response.setOpis(predmet.getOpis());
        response.setEspb(predmet.getEspb());
        response.setObavezan(predmet.isObavezan());

        if (predmet.getStudProgram() != null) {
            response.setStudijskiProgramId(predmet.getStudProgram().getId());
            response.setStudijskiProgramNaziv(predmet.getStudProgram().getNaziv());
        }

        return response;
    }
}