package org.raflab.studsluzba.services;

import org.raflab.studsluzba.controllers.request.IzlazakNaIspitRequest;
import org.raflab.studsluzba.controllers.request.PrijavaIspitaRequest;
import org.raflab.studsluzba.controllers.response.*;
import org.raflab.studsluzba.model.*;
import org.raflab.studsluzba.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class IspitService {

    @Autowired
    private IspitiRepository ispitiRepository;

    @Autowired
    private PrijavaIspitaRepository prijavaIspitaRepository;

    @Autowired
    private IzlazakNaIspitRepository izlazakNaIspitRepository;

    @Autowired
    private StudentIndeksRepository studentIndeksRepository;

    @Autowired
    private OsvojeniPoeniRepository osvojeniPoeniRepository;


    public List<Ispit> findAll() {
        return (List<Ispit>) ispitiRepository.findAll();
    }

    public Optional<Ispit> findById(Long id) {
        return ispitiRepository.findById(id);
    }



    public List<Ispit> findByPredmetAndRok(Long predmetId, Long rokId) {
        return ispitiRepository.findByPredmetIdAndIspitniRokId(predmetId, rokId);
    }

    public Ispit save(Ispit ispit) {
        return ispitiRepository.save(ispit);
    }

    public void deleteById(Long id) {
        ispitiRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return ispitiRepository.existsById(id);
    }


    /**
     * 1. Svi prijavljeni studenti za ispit
     */
    @Transactional(readOnly = true)
    public List<PrijavaIspitaResponse> getPrijavljeniStudenti(Long ispitId) {
        Ispit ispit = ispitiRepository.findById(ispitId)
                .orElseThrow(() -> new RuntimeException("Ispit ne postoji"));

        List<PrijavaIspita> prijave = prijavaIspitaRepository.findByIspit(ispit);

        return prijave.stream()
                .map(this::convertToPrijavaResponse)
                .collect(Collectors.toList());
    }

    /**
     * 2. Prosečna ocena na ispitu
     */
    @Transactional(readOnly = true)
    public ProsecnaOcenaIspitResponse getProsecnaOcenaNaIspitu(Long ispitId) {
        Ispit ispit = ispitiRepository.findById(ispitId)
                .orElseThrow(() -> new RuntimeException("Ispit ne postoji"));

        List<PrijavaIspita> prijave = prijavaIspitaRepository.findByIspit(ispit);

        List<IzlazakNaIspit> izlasci = new ArrayList<>();
        for (PrijavaIspita prijava : prijave) {
            izlazakNaIspitRepository.findByPrijavaIspita(prijava)
                    .ifPresent(izlasci::add);
        }

        long brojPolaganja = izlasci.size();
        long brojPolozenih = izlasci.stream()
                .filter(IzlazakNaIspit::jePolozio)
                .count();

        Double prosecnaOcena = izlasci.stream()
                .filter(IzlazakNaIspit::jePolozio)
                .mapToInt(IzlazakNaIspit::getOcena)
                .average()
                .orElse(0.0);

        ProsecnaOcenaIspitResponse response = new ProsecnaOcenaIspitResponse();
        response.setIspitId(ispit.getId());
        response.setNazivPredmeta(ispit.getPredmet().getNaziv());
        response.setNazivRoka(ispit.getIspitniRok().getNaziv());
        response.setProsecnaOcena(prosecnaOcena);
        response.setBrojPolaganja(brojPolaganja);
        response.setBrojPolozenih(brojPolozenih);

        return response;
    }

    /**
     * 3. Prijava ispita za rok i predmet
     */
    @Transactional
    public PrijavaIspitaResponse prijaviIspit(PrijavaIspitaRequest request) {
        StudentIndeks studentIndeks = studentIndeksRepository.findById(request.getStudentIndeksId())
                .orElseThrow(() -> new RuntimeException("Student indeks ne postoji"));

        Ispit ispit = ispitiRepository.findById(request.getIspitId())
                .orElseThrow(() -> new RuntimeException("Ispit ne postoji"));

        // Proveri da li je student već prijavio ovaj ispit
        if (prijavaIspitaRepository.existsByStudentIndeksAndIspit(studentIndeks, ispit)) {
            throw new RuntimeException("Student je već prijavio ovaj ispit");
        }

        PrijavaIspita prijava = new PrijavaIspita();
        prijava.setStudentIndeks(studentIndeks);
        prijava.setIspit(ispit);
        prijava.setDatumPrijave(request.getDatumPrijave() != null ?
                request.getDatumPrijave() : LocalDate.now());
        prijava.setIzasao(false);

        prijava = prijavaIspitaRepository.save(prijava);

        return convertToPrijavaResponse(prijava);
    }

    /**
     * 4. Dodavanje izlaska na ispit + poeni
     */
    @Transactional
    public IzlazakNaIspitResponse dodajIzlazakNaIspit(IzlazakNaIspitRequest request) {
        PrijavaIspita prijava = prijavaIspitaRepository.findById(request.getPrijavaIspitaId())
                .orElseThrow(() -> new RuntimeException("Prijava ispita ne postoji"));

        // Proveri da li student već ima izlazak na ovaj ispit
        if (izlazakNaIspitRepository.existsByPrijavaIspita(prijava)) {
            throw new RuntimeException("Student već ima izlazak na ovaj ispit");
        }

        // Povuci predispitne poene
        Integer predispitniPoeni = getPredispitniPoeniZaStudenta(
                prijava.getStudentIndeks().getId(),
                prijava.getIspit().getPredmet().getId(),
                prijava.getIspit().getIspitniRok().getSkolskaGodina().getId()
        );

        // Kreiraj izlazak
        IzlazakNaIspit izlazak = new IzlazakNaIspit(
                prijava,
                predispitniPoeni,
                request.getPoeniIspit()
        );

        izlazak.setNapomena(request.getNapomena());
        if (request.getPonisteno() != null) {
            izlazak.setPonisteno(request.getPonisteno());
        }

        izlazak = izlazakNaIspitRepository.save(izlazak);

        // Oznaci da je student izasao na ispit
        prijava.setIzasao(true);
        prijavaIspitaRepository.save(prijava);

        return convertToIzlazakResponse(izlazak);
    }

    /**
     * 5. Rezultati ispita sortirano
     */
    @Transactional(readOnly = true)
    public List<RezultatIspitaResponse> getRezultatiIspita(Long ispitId, String sortBy) {
        Ispit ispit = ispitiRepository.findById(ispitId)
                .orElseThrow(() -> new RuntimeException("Ispit ne postoji"));

        List<PrijavaIspita> prijave = prijavaIspitaRepository.findByIspitAndIzasao(ispit, true);

        List<RezultatIspitaResponse> rezultati = new ArrayList<>();

        for (PrijavaIspita prijava : prijave) {
            izlazakNaIspitRepository.findByPrijavaIspita(prijava).ifPresent(izlazak -> {
                RezultatIspitaResponse response = new RezultatIspitaResponse();
                StudentIndeks si = prijava.getStudentIndeks();

                response.setStudentIndeksId(si.getId());
                response.setStudijskiProgram(si.getStudProgramOznaka());
                response.setGodinaUpisa(si.getGodina());
                response.setBrojIndeksa(si.getBroj());
                response.setImeStudenta(si.getStudent().getIme());
                response.setPrezimeStudenta(si.getStudent().getPrezime());
                response.setPoeniPredispitne(izlazak.getPoeniPredispitne());
                response.setPoeniIspit(izlazak.getPoeniIspit());
                response.setUkupnoPoeni(izlazak.getUkupnoPoeni());
                response.setOcena(izlazak.getOcena());
                response.setPolozio(izlazak.jePolozio());

                rezultati.add(response);
            });
        }

        // Sortiranje
        if ("prezime".equalsIgnoreCase(sortBy)) {
            rezultati.sort(Comparator.comparing(RezultatIspitaResponse::getPrezimeStudenta));
        } else if ("ocena".equalsIgnoreCase(sortBy)) {
            rezultati.sort(Comparator.comparing(RezultatIspitaResponse::getOcena).reversed());
        } else {
            // Default: sortiranje po ukupnim poenima
            rezultati.sort(Comparator.comparing(RezultatIspitaResponse::getUkupnoPoeni).reversed());
        }

        return rezultati;
    }

    /**
     * 6. Predispitni poeni studenta na predmetu
     */
    @Transactional(readOnly = true)
    public List<OsvojeniPoeniResponse> getPredispitniPoeniStudenta(
            Long studentIndeksId, Long predmetId, Long skolskaGodinaId) {

        StudentIndeks studentIndeks = studentIndeksRepository.findById(studentIndeksId)
                .orElseThrow(() -> new RuntimeException("Student indeks ne postoji"));

        List<OsvojeniPoeni> osvojeni = osvojeniPoeniRepository.findByStudentPredmetGodina(
                studentIndeksId, predmetId, skolskaGodinaId
        );

        return osvojeni.stream()
                .map(op -> {
                    OsvojeniPoeniResponse response = new OsvojeniPoeniResponse();
                    response.setId(op.getId());
                    response.setStudentIndeksId(studentIndeksId);
                    response.setStudentIme(studentIndeks.getStudent().getIme());
                    response.setStudentPrezime(studentIndeks.getStudent().getPrezime());
                    response.setStudentBrojIndeksa(studentIndeks.getBroj());
                    response.setStudentGodinaIndeksa(studentIndeks.getGodina());
                    response.setPredispitnaObavezaId(op.getPredispitnaObaveza().getId());
                    response.setObavezaVrsta(op.getPredispitnaObaveza().getVrsta());
                    response.setMaxPoena(op.getPredispitnaObaveza().getMaxPoena());
                    response.setPredmetId(op.getPredispitnaObaveza().getPredmet().getId());
                    response.setPredmetSifra(op.getPredispitnaObaveza().getPredmet().getSifra());
                    response.setPredmetNaziv(op.getPredispitnaObaveza().getPredmet().getNaziv());
                    response.setSkolskaGodinaId(op.getPredispitnaObaveza().getSkolskaGodina().getId());
                    response.setSkolskaGodinaNaziv(op.getPredispitnaObaveza().getSkolskaGodina().getNaziv());
                    response.setPoeni(op.getPoeni());
                    return response;
                })
                .collect(Collectors.toList());
    }

    /**
     * 7. Broj pokušaja polaganja predmeta
     */
    @Transactional(readOnly = true)
    public BrojPokusajaResponse getBrojPokusajaPolaganja(Long studentIndeksId, Long predmetId) {
        StudentIndeks studentIndeks = studentIndeksRepository.findById(studentIndeksId)
                .orElseThrow(() -> new RuntimeException("Student indeks ne postoji"));

        Long brojPokusaja = prijavaIspitaRepository.countPokusajaPolaganja(studentIndeksId, predmetId);

        BrojPokusajaResponse response = new BrojPokusajaResponse();
        response.setStudentIndeksId(studentIndeksId);
        response.setImeStudenta(studentIndeks.getStudent().getIme());
        response.setPrezimeStudenta(studentIndeks.getStudent().getPrezime());
        response.setPredmetId(predmetId);

        // Dohvati naziv predmeta
        if (brojPokusaja > 0) {
            List<PrijavaIspita> prijave = prijavaIspitaRepository.findByStudentIndeks(studentIndeks);
            prijave.stream()
                    .filter(p -> p.getIspit().getPredmet().getId().equals(predmetId))
                    .findFirst()
                    .ifPresent(p -> response.setNazivPredmeta(p.getIspit().getPredmet().getNaziv()));
        }

        response.setBrojPokusaja(brojPokusaja);

        return response;
    }


    private Integer getPredispitniPoeniZaStudenta(Long studentIndeksId, Long predmetId, Long skolskaGodinaId) {
        List<OsvojeniPoeni> osvojeni = osvojeniPoeniRepository.findByStudentPredmetGodina(
                studentIndeksId, predmetId, skolskaGodinaId
        );

        return osvojeni.stream()
                .mapToInt(OsvojeniPoeni::getPoeni)
                .sum();
    }

    private PrijavaIspitaResponse convertToPrijavaResponse(PrijavaIspita prijava) {
        PrijavaIspitaResponse response = new PrijavaIspitaResponse();
        response.setId(prijava.getId());
        response.setStudentIndeksId(prijava.getStudentIndeks().getId());
        response.setBrojIndeksa(prijava.getStudentIndeks().getBroj());
        response.setGodinaIndeksa(prijava.getStudentIndeks().getGodina());
        response.setStudProgramOznaka(prijava.getStudentIndeks().getStudProgramOznaka());
        response.setImeStudenta(prijava.getStudentIndeks().getStudent().getIme());
        response.setPrezimeStudenta(prijava.getStudentIndeks().getStudent().getPrezime());
        response.setIspitId(prijava.getIspit().getId());
        response.setNazivPredmeta(prijava.getIspit().getPredmet().getNaziv());
        response.setSifraPredmeta(prijava.getIspit().getPredmet().getSifra());
        response.setDatumOdrzavanjaIspita(prijava.getIspit().getDatumOdrzavanja());
        response.setDatumPrijave(prijava.getDatumPrijave());
        response.setIzasao(prijava.getIzasao());
        return response;
    }

    private IzlazakNaIspitResponse convertToIzlazakResponse(IzlazakNaIspit izlazak) {
        IzlazakNaIspitResponse response = new IzlazakNaIspitResponse();
        StudentIndeks si = izlazak.getPrijavaIspita().getStudentIndeks();

        response.setId(izlazak.getId());
        response.setStudentIndeksId(si.getId());
        response.setImeStudenta(si.getStudent().getIme());
        response.setPrezimeStudenta(si.getStudent().getPrezime());
        response.setBrojIndeksa(si.getBroj());
        response.setGodinaIndeksa(si.getGodina());
        response.setStudProgramOznaka(si.getStudProgramOznaka());
        response.setNazivPredmeta(izlazak.getPrijavaIspita().getIspit().getPredmet().getNaziv());
        response.setSifraPredmeta(izlazak.getPrijavaIspita().getIspit().getPredmet().getSifra());
        response.setPoeniPredispitne(izlazak.getPoeniPredispitne());
        response.setPoeniIspit(izlazak.getPoeniIspit());
        response.setUkupnoPoeni(izlazak.getUkupnoPoeni());
        response.setOcena(izlazak.getOcena());
        response.setNapomena(izlazak.getNapomena());
        response.setPonisteno(izlazak.getPonisteno());
        response.setPolozio(izlazak.jePolozio());

        return response;
    }
}