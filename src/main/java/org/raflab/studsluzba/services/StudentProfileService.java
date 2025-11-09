package org.raflab.studsluzba.services;

import org.raflab.studsluzba.model.*;
import org.raflab.studsluzba.model.dtos.StudentProfileDTO;
import org.raflab.studsluzba.model.dtos.StudentWebProfileDTO;
import org.raflab.studsluzba.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentProfileService {

    @Autowired
    StudentIndeksRepository studentIndeksRepo;

    @Autowired
    StudentPodaciRepository studentPodaciRepo;

    @Autowired
    SlusaPredmetRepository slusaPredmetRepo;

    @Autowired
    PolozenPredmetRepository polozenPredmetRepo;

    @Autowired
    UpisGodineRepository upisGodineRepo;

    @Autowired
    ObnovaGodineRepository obnovaGodineRepo;

    @Autowired
    UplataRepository uplataRepo;

    @Autowired
    PredmetRepository predmetRepo;

    // ✅ Postojeće metode
    public StudentProfileDTO getStudentProfile(Long indeksId) {
        StudentProfileDTO retVal = new StudentProfileDTO();
        StudentIndeks studIndeks = studentIndeksRepo.findById(indeksId).get();
        retVal.setIndeks(studIndeks);
        retVal.setSlusaPredmete(slusaPredmetRepo.getSlusaPredmetForIndeksAktivnaGodina(indeksId));
        return retVal;
    }

    public StudentWebProfileDTO getStudentWebProfile(Long indeksId) {
        StudentWebProfileDTO retVal = new StudentWebProfileDTO();
        StudentIndeks studIndeks = studentIndeksRepo.findById(indeksId).get();
        Long studPodaciId = studIndeks.getStudent().getId();
        retVal.setAktivanIndeks(studentPodaciRepo.getAktivanIndeks(studPodaciId));
        retVal.setSlusaPredmete(slusaPredmetRepo.getSlusaPredmetForIndeksAktivnaGodina(indeksId));
        return retVal;
    }

    //  - Selekcija studenta preko broja indeksa (godina/broj/oznaka)
    public Optional<StudentIndeks> getStudentByIndeks(int godina, int broj, String oznaka) {
        return studentIndeksRepo.findByGodinaAndBrojAndStudProgramOznaka(godina, broj, oznaka);
    }

    //  - Selekcija svih položenih ispita (paginirano)
    public Page<PolozenPredmet> getPolozeniPredmeti(Long studentIndeksId, Pageable pageable) {
        StudentIndeks studentIndeks = studentIndeksRepo.findById(studentIndeksId)
                .orElseThrow(() -> new RuntimeException("Student indeks ne postoji"));
        return polozenPredmetRepo.findByStudentIndeks(studentIndeks, pageable);
    }

    //  - Selekcija svih nepoloženih ispita (paginirano)
    public Page<SlusaPredmet> getNepolozeniPredmeti(Long studentIndeksId, Pageable pageable) {
        StudentIndeks studentIndeks = studentIndeksRepo.findById(studentIndeksId)
                .orElseThrow(() -> new RuntimeException("Student indeks ne postoji"));

        // Nepoloženi = predmeti koje sluša, a nisu u položenima
        return slusaPredmetRepo.findNepolozeniPredmeti(studentIndeks, pageable);
    }

    //  - Pregled svih upisanih godina za broj indeksa
    public List<UpisGodine> getUpisaneGodine(Long studentIndeksId) {
        StudentIndeks studentIndeks = studentIndeksRepo.findById(studentIndeksId)
                .orElseThrow(() -> new RuntimeException("Student indeks ne postoji"));
        return upisGodineRepo.findByStudentIndeksOrderByGodinaStudijaAsc(studentIndeks);
    }

    //  - Pregled obnovljenih godina za broj indeksa
    public List<ObnovaGodine> getObnovljeneGodine(Long studentIndeksId) {
        StudentIndeks studentIndeks = studentIndeksRepo.findById(studentIndeksId)
                .orElseThrow(() -> new RuntimeException("Student indeks ne postoji"));
        return obnovaGodineRepo.findByStudentIndeksOrderByGodinaStudijaAsc(studentIndeks);
    }

    // - Preostali iznos za uplatu (3000 EUR - ukupno uplaćeno)
    public Double getPreostaliIznosZaUplatu(Long studentIndeksId) {
        StudentIndeks studentIndeks = studentIndeksRepo.findById(studentIndeksId)
                .orElseThrow(() -> new RuntimeException("Student indeks ne postoji"));

        Long studentId = studentIndeks.getStudent().getId();
        Double ukupnoUplaceno = uplataRepo.sumIznosEurByStudentId(studentId);

        if (ukupnoUplaceno == null) {
            ukupnoUplaceno = 0.0;
        }

        double skolarinaNaCenu = 3000.0;
        return skolarinaNaCenu - ukupnoUplaceno;
    }

    //   Selekcija studenata po imenu i/ili prezimenu (paginirano)
    public Page<StudentPodaci> getStudentiByImeOrPrezime(String ime, String prezime, Pageable pageable) {
        if (ime != null && prezime != null) {
            return studentPodaciRepo.findByImeContainingIgnoreCaseAndPrezimeContainingIgnoreCase(ime, prezime, pageable);
        } else if (ime != null) {
            return studentPodaciRepo.findByImeContainingIgnoreCase(ime, pageable);
        } else if (prezime != null) {
            return studentPodaciRepo.findByPrezimeContainingIgnoreCase(prezime, pageable);
        } else {
            return studentPodaciRepo.findAll(pageable);
        }
    }

    // Selekcija studenata po srednjoj školi
    public List<StudentPodaci> getStudentiBySrednjaSkola(Long srednjaSkolaId) {
        return studentPodaciRepo.findBySrednjaSkolaId(srednjaSkolaId);
    }
}