package org.raflab.studsluzba.controllers;


import org.raflab.studsluzba.services.StudentProfileService;
import org.raflab.studsluzba.shared.dtos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentProfileService studentProfileService;

    // ============================================
    // STUDENT PROFILE
    // ============================================

    /**
     * Profil studenta po ID
     */
    @GetMapping("/{studentIndeksId}/profile")
    public ResponseEntity<StudentProfileDTO> getStudentProfile(@PathVariable Long studentIndeksId) {
        StudentProfileDTO profile = studentProfileService.getStudentProfile(studentIndeksId);
        return ResponseEntity.ok(profile);
    }

    /**
     * Selekcija studenta preko broja indeksa (godina, broj, oznaka studijskog programa)
     */
    @GetMapping("/indeks")
    public ResponseEntity<StudentProfileDTO> getStudentByIndeks(
            @RequestParam Integer godina,
            @RequestParam Integer broj,
            @RequestParam String oznaka) {
        StudentProfileDTO student = studentProfileService.getStudentByIndeks(godina, broj, oznaka);
        return ResponseEntity.ok(student);
    }

    // ============================================
    // POLOŽENI I NEPOLOŽENI PREDMETI
    // ============================================

    /**
     * Selekcija svih položenih ispita za broj indeksa, paginirano
     */
    @GetMapping("/{studentIndeksId}/polozeni-predmeti")
    public ResponseEntity<Page<PolozenPredmetDTO>> getPolozeniPredmeti(
            @PathVariable Long studentIndeksId,
            Pageable pageable) {
        Page<PolozenPredmetDTO> predmeti = studentProfileService.getPolozeniPredmeti(studentIndeksId, pageable);
        return ResponseEntity.ok(predmeti);
    }

    /**
     * Selekcija svih nepoloženih ispita, paginirano
     */
    @GetMapping("/{studentIndeksId}/nepolozeni-predmeti")
    public ResponseEntity<Page<NepolozenPredmetDTO>> getNepolozeniPredmeti(
            @PathVariable Long studentIndeksId,
            @RequestParam(required = false) String ime,
            @RequestParam(required = false) String prezime,
            Pageable pageable) {
        Page<NepolozenPredmetDTO> predmeti = studentProfileService.getNepolozeniPredmeti(studentIndeksId, ime, prezime, pageable);
        return ResponseEntity.ok(predmeti);
    }

    // ============================================
    // UPIS I OBNOVA GODINE
    // ============================================

    /**
     * Pregled svih upisanih godina za broj indeksa studenta
     */
    @GetMapping("/{studentIndeksId}/upisane-godine")
    public ResponseEntity<List<UpisGodineDTO>> getUpisaneGodine(@PathVariable Long studentIndeksId) {
        List<UpisGodineDTO> upisaneGodine = studentProfileService.getUpisaneGodine(studentIndeksId);
        return ResponseEntity.ok(upisaneGodine);
    }

    /**
     * Upis studenta na godinu
     */
    @PostMapping("/{studentIndeksId}/upis-godine")
    public ResponseEntity<UpisGodineDTO> upisStudentaNaGodinu(
            @PathVariable Long studentIndeksId,
            @RequestBody UpisGodineRequestDTO request) {
        UpisGodineDTO upisGodine = studentProfileService.upisStudentaNaGodinu(studentIndeksId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(upisGodine);
    }

    /**
     * Pregled obnovljenih godina za broj indeksa
     */
    @GetMapping("/{studentIndeksId}/obnovljene-godine")
    public ResponseEntity<List<ObnovaGodineDTO>> getObnovljeneGodine(@PathVariable Long studentIndeksId) {
        List<ObnovaGodineDTO> obnovljeneGodine = studentProfileService.getObnovljeneGodine(studentIndeksId);
        return ResponseEntity.ok(obnovljeneGodine);
    }

    /**
     * Obnova godine za studenta (max 60 ESPB)
     */
    @PostMapping("/{studentIndeksId}/obnova-godine")
    public ResponseEntity<ObnovaGodineDTO> obnovaGodine(
            @PathVariable Long studentIndeksId,
            @RequestBody ObnovaGodineRequestDTO request) {
        ObnovaGodineDTO obnovaGodine = studentProfileService.obnovaGodine(studentIndeksId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(obnovaGodine);
    }

    // ============================================
    // UPLATE -  DODATO
    // ============================================

    /**
     *  NOVA METODA - Selekcija svih uplata za studenta
     */
    @GetMapping("/{studentIndeksId}/uplate")
    public ResponseEntity<List<UplataDTO>> getUplate(@PathVariable Long studentIndeksId) {
        List<UplataDTO> uplate = studentProfileService.getUplate(studentIndeksId);
        return ResponseEntity.ok(uplate);
    }

    /**
     * Dodavanje nove uplate (automatski dohvata srednji kurs EUR)
     */
    @PostMapping("/{studentIndeksId}/uplate")
    public ResponseEntity<UplataDTO> dodajUplatu(
            @PathVariable Long studentIndeksId,
            @RequestBody UplataRequestDTO request) {
        UplataDTO uplata = studentProfileService.dodajUplatu(studentIndeksId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(uplata);
    }

    /**
     * Selekcija preostalog iznosa za uplatu (EUR + RSD)
     */
    @GetMapping("/{studentIndeksId}/preostali-iznos-za-uplatu")
    public ResponseEntity<PreostaliIznosDTO> getPreostaliIznos(@PathVariable Long studentIndeksId) {
        PreostaliIznosDTO iznos = studentProfileService.getPreostaliIznosZaUplatu(studentIndeksId);
        return ResponseEntity.ok(iznos);
    }

    // ============================================
    // PRETRAGA STUDENATA
    // ============================================

    /**
     * Selekcija studenata po imenu i/ili prezimenu, paginirano
     */
    @GetMapping("/ime-prezime")
    public ResponseEntity<Page<StudentPodaciDTO>> getStudentsByImeIPrezime(
            @RequestParam(required = false) String ime,
            @RequestParam(required = false) String prezime,
            Pageable pageable) {
        Page<StudentPodaciDTO> studenti = studentProfileService.getStudentsByImeIPrezime(ime, prezime, pageable);
        return ResponseEntity.ok(studenti);
    }

    /**
     * Selekcija studenata iz određene srednje škole
     */
    @GetMapping("/srednja-skola/{srednjaSkolaId}")
    public ResponseEntity<List<StudentPodaciDTO>> getStudentsBySrednjaSkola(@PathVariable Long srednjaSkolaId) {
        List<StudentPodaciDTO> studenti = studentProfileService.getStudentsBySrednjaSkola(srednjaSkolaId);
        return ResponseEntity.ok(studenti);
    }

    // ============================================
    // STATISTIKA STUDENTA
    // ============================================

    /**
     * Statistika studenta (ESPB, prosek, broj položenih/nepoloženih)
     */
    @GetMapping("/{studentIndeksId}/statistics")
    public ResponseEntity<StudentStatisticsDTO> getStatistics(@PathVariable Long studentIndeksId) {
        StudentStatisticsDTO statistics = studentProfileService.getStatistics(studentIndeksId);
        return ResponseEntity.ok(statistics);
    }
}