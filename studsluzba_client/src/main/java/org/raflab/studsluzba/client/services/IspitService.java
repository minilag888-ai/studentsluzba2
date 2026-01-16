package org.raflab.studsluzba.client.services;

import lombok.extern.slf4j.Slf4j;
import org.raflab.studsluzba.client.api.IspitApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class IspitService {

    @Autowired
    private IspitApiClient apiClient;

    /**
     * Svi ispiti
     */
    public Mono<List<IspitResponse>> getAllIspiti() {
        log.info("Fetching all ispiti");
        return apiClient.getAllIspiti();
    }

    /**
     * Prijavljeni studenti za ispit
     */
    public Mono<List<PrijavaIspitaResponse>> getPrijavljeniStudenti(Long ispitId) {
        log.info("Fetching prijavljeni studenti for ispit ID: {}", ispitId);
        return apiClient.getPrijavljeniStudenti(ispitId);
    }

    /**
     * Prosečna ocena na ispitu
     */
    public Mono<ProsecnaOcenaIspitResponse> getProsecnaOcena(Long ispitId) {
        log.info("Fetching prosecna ocena for ispit ID: {}", ispitId);
        return apiClient.getProsecnaOcenaNaIspitu(ispitId);
    }

    /**
     * Rezultati ispita
     */
    public Mono<List<RezultatIspitaResponse>> getRezultati(Long ispitId, String sortBy) {
        log.info("Fetching rezultati for ispit ID: {}, sortBy: {}", ispitId, sortBy);
        return apiClient.getRezultatiIspita(ispitId, sortBy != null ? sortBy : "poeni");
    }

    /**
     * Prijavi studenta na ispit
     */
    public Mono<PrijavaIspitaResponse> prijaviIspit(Long studentIndeksId, Long ispitId) {
        log.info("Prijava studenta {} na ispit {}", studentIndeksId, ispitId);

        PrijavaIspitaRequest request = new PrijavaIspitaRequest();
        request.setStudentIndeksId(studentIndeksId);
        request.setIspitId(ispitId);
        request.setDatumPrijave(LocalDate.now());

        return apiClient.prijaviIspit(request);
    }

    /**
     * Dodaj izlazak na ispit
     */
    public Mono<IzlazakNaIspitResponse> dodajIzlazak(Long prijavaIspitaId, Integer poeniIspit, String napomena) {
        log.info("Dodavanje izlaska za prijavu {}, poeni: {}", prijavaIspitaId, poeniIspit);

        // Validacija
        if (poeniIspit == null || poeniIspit < 0 || poeniIspit > 100) {
            return Mono.error(new RuntimeException("Poeni moraju biti između 0 i 100!"));
        }

        IzlazakNaIspitRequest request = new IzlazakNaIspitRequest();
        request.setPrijavaIspitaId(prijavaIspitaId);
        request.setPoeniIspit(poeniIspit);
        request.setNapomena(napomena);
        request.setPonisteno(false);

        return apiClient.dodajIzlazak(request);
    }

    /**
     * Svi ispitni rokovi
     */
    public Mono<List<IspitniRokResponse>> getAllIspitniRokovi() {
        log.info("Fetching all ispitni rokovi");
        return apiClient.getAllIspitniRokovi();
    }

    /**
     * Dodaj ispitni rok
     */
    public Mono<IspitniRokResponse> addIspitniRok(IspitniRokRequest request) {
        log.info("Adding new ispitni rok: {}", request.getNaziv());

        // Validacija
        if (request.getPocetak().isAfter(request.getKraj())) {
            return Mono.error(new RuntimeException("Datum početka ne može biti nakon datuma kraja!"));
        }

        return apiClient.addIspitniRok(request);
    }

    /**
     * Dodaj ispit
     */
    public Mono<IspitResponse> addIspit(IspitRequest request) {
        log.info("Adding new ispit for predmet ID: {}", request.getPredmetId());

        // Validacija
        if (request.getDatumOdrzavanja().isBefore(LocalDate.now())) {
            return Mono.error(new RuntimeException("Datum održavanja ne može biti u prošlosti!"));
        }

        return apiClient.addIspit(request);
    }
}