package org.raflab.studsluzba.controllers;

import org.raflab.studsluzba.controllers.request.IspitRequest;
import org.raflab.studsluzba.controllers.request.IzlazakNaIspitRequest;
import org.raflab.studsluzba.controllers.request.PrijavaIspitaRequest;
import org.raflab.studsluzba.controllers.response.*;
import org.raflab.studsluzba.model.Ispit;
import org.raflab.studsluzba.model.IspitniRok;
import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.model.DrziPredmet;
import org.raflab.studsluzba.services.IspitService;
import org.raflab.studsluzba.services.IspitniRokService;
import org.raflab.studsluzba.services.PredmetService;
import org.raflab.studsluzba.services.DrziPredmetService;
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
@RequestMapping(path = "/api/ispiti")
@Transactional
public class IspitiController {

    @Autowired
    private IspitService ispitService;  // ← SAMO JEDAN SERVIS!

    @Autowired
    private PredmetService predmetService;

    @Autowired
    private IspitniRokService ispitniRokService;

    @Autowired
    private DrziPredmetService drziPredmetService;

    // ========== CRUD OPERACIJE ==========

    @GetMapping(path = "/all")
    public List<IspitResponse> getAll() {
        List<Ispit> ispiti = ispitService.findAll();
        List<IspitResponse> responses = new ArrayList<>();
        for (Ispit ispit : ispiti) {
            responses.add(Converters.toIspitResponse(ispit));
        }
        return responses;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<IspitResponse> getById(@PathVariable Long id) {
        Ispit ispit = ispitService.findById(id).orElse(null);
        if (ispit == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Converters.toIspitResponse(ispit));
    }



    @GetMapping(path = "/predmet/{predmetId}/rok/{rokId}")
    public List<IspitResponse> getByPredmetAndRok(@PathVariable Long predmetId, @PathVariable Long rokId) {
        List<Ispit> ispiti = ispitService.findByPredmetAndRok(predmetId, rokId);
        List<IspitResponse> responses = new ArrayList<>();
        for (Ispit ispit : ispiti) {
            responses.add(Converters.toIspitResponse(ispit));
        }
        return responses;
    }

    @PostMapping(path = "/add")
    public ResponseEntity<IspitResponse> add(@RequestBody @Valid IspitRequest request) {
        Predmet predmet = predmetService.findById(request.getPredmetId())
                .orElseThrow(() -> new RuntimeException("Predmet not found"));

        IspitniRok rok = ispitniRokService.findById(request.getIspitniRokId())
                .orElseThrow(() -> new RuntimeException("IspitniRok not found"));

        DrziPredmet drziPredmet = drziPredmetService.findById(request.getDrziPredmetId())
                .orElseThrow(() -> new RuntimeException("DrziPredmet not found"));

        Ispit ispit = new Ispit();
        ispit.setPredmet(predmet);
        ispit.setIspitniRok(rok);
        ispit.setDrziPredmet(drziPredmet);
        ispit.setDatumOdrzavanja(request.getDatumOdrzavanja());
        ispit.setVremePocetka(request.getVremePocetka());
        ispit.setNapomena(request.getNapomena());
        ispit.setZakljucen(false);

        Ispit saved = ispitService.save(ispit);

        return ResponseEntity.ok(Converters.toIspitResponse(saved));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<IspitResponse> update(@PathVariable Long id, @RequestBody @Valid IspitRequest request) {
        if (!ispitService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        Predmet predmet = predmetService.findById(request.getPredmetId())
                .orElseThrow(() -> new RuntimeException("Predmet not found"));

        IspitniRok rok = ispitniRokService.findById(request.getIspitniRokId())
                .orElseThrow(() -> new RuntimeException("IspitniRok not found"));

        DrziPredmet drziPredmet = drziPredmetService.findById(request.getDrziPredmetId())
                .orElseThrow(() -> new RuntimeException("DrziPredmet not found"));

        Ispit ispit = new Ispit();
        ispit.setId(id);
        ispit.setPredmet(predmet);
        ispit.setIspitniRok(rok);
        ispit.setDrziPredmet(drziPredmet);
        ispit.setDatumOdrzavanja(request.getDatumOdrzavanja());
        ispit.setVremePocetka(request.getVremePocetka());
        ispit.setNapomena(request.getNapomena());

        Ispit updated = ispitService.save(ispit);

        return ResponseEntity.ok(Converters.toIspitResponse(updated));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!ispitService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        ispitService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    /**
     * 1. Svi prijavljeni studenti za ispit
     */
    @GetMapping(path = "/{ispitId}/prijavljeni")
    public ResponseEntity<List<PrijavaIspitaResponse>> getPrijavljeniStudenti(@PathVariable Long ispitId) {
        List<PrijavaIspitaResponse> studenti = ispitService.getPrijavljeniStudenti(ispitId);
        return ResponseEntity.ok(studenti);
    }

    /**
     * 2. Prosečna ocena na ispitu
     */
    @GetMapping(path = "/{ispitId}/prosecna-ocena")
    public ResponseEntity<ProsecnaOcenaIspitResponse> getProsecnaOcena(@PathVariable Long ispitId) {
        ProsecnaOcenaIspitResponse response = ispitService.getProsecnaOcenaNaIspitu(ispitId);
        return ResponseEntity.ok(response);
    }

    /**
     * 3. Prijava ispita
     */
    @PostMapping(path = "/prijava")
    public ResponseEntity<PrijavaIspitaResponse> prijaviIspit(@RequestBody @Valid PrijavaIspitaRequest request) {
        try {
            PrijavaIspitaResponse response = ispitService.prijaviIspit(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 4. Dodavanje izlaska na ispit
     */
    @PostMapping(path = "/izlazak")
    public ResponseEntity<IzlazakNaIspitResponse> dodajIzlazak(@RequestBody @Valid IzlazakNaIspitRequest request) {
        try {
            IzlazakNaIspitResponse response = ispitService.dodajIzlazakNaIspit(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 5. Rezultati ispita sortirano
     */
    @GetMapping(path = "/{ispitId}/rezultati")
    public ResponseEntity<List<RezultatIspitaResponse>> getRezultati(
            @PathVariable Long ispitId,
            @RequestParam(required = false, defaultValue = "poeni") String sortBy) {

        List<RezultatIspitaResponse> rezultati = ispitService.getRezultatiIspita(ispitId, sortBy);
        return ResponseEntity.ok(rezultati);
    }

    /**
     * 6. Predispitni poeni studenta
     */
    @GetMapping(path = "/predispitni-poeni/{studentIndeksId}/{predmetId}")
    public ResponseEntity<List<OsvojeniPoeniResponse>> getPredispitniPoeni(
            @PathVariable Long studentIndeksId,
            @PathVariable Long predmetId,
            @RequestParam Long skolskaGodinaId) {

        List<OsvojeniPoeniResponse> poeni = ispitService.getPredispitniPoeniStudenta(
                studentIndeksId, predmetId, skolskaGodinaId);
        return ResponseEntity.ok(poeni);
    }

    /**
     * 7. Broj pokušaja polaganja
     */
    @GetMapping(path = "/broj-polaganja/{studentIndeksId}/{predmetId}")
    public ResponseEntity<BrojPokusajaResponse> getBrojPokusaja(
            @PathVariable Long studentIndeksId,
            @PathVariable Long predmetId) {

        BrojPokusajaResponse response = ispitService.getBrojPokusajaPolaganja(studentIndeksId, predmetId);
        return ResponseEntity.ok(response);
    }
}