package org.raflab.studsluzba.client.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
public class IspitApiClient {

    @Autowired
    private WebClient webClient;

    /**
     * Svi ispiti
     */
    public Mono<List<IspitResponse>> getAllIspiti() {
        return webClient.get()
                .uri("/ispiti/all")
                .retrieve()
                .bodyToFlux(IspitResponse.class)
                .collectList()
                .doOnError(e -> log.error("Error fetching all ispiti", e));
    }

    /**
     * Prijavljeni studenti za ispit
     */
    public Mono<List<PrijavaIspitaResponse>> getPrijavljeniStudenti(Long ispitId) {
        return webClient.get()
                .uri("/ispiti/{id}/prijavljeni", ispitId)
                .retrieve()
                .bodyToFlux(PrijavaIspitaResponse.class)
                .collectList()
                .doOnError(e -> log.error("Error fetching prijavljeni studenti", e));
    }

    /**
     * Prosečna ocena na ispitu
     */
    public Mono<ProsecnaOcenaIspitResponse> getProsecnaOcenaNaIspitu(Long ispitId) {
        return webClient.get()
                .uri("/ispiti/{id}/prosecna-ocena", ispitId)
                .retrieve()
                .bodyToMono(ProsecnaOcenaIspitResponse.class)
                .doOnError(e -> log.error("Error fetching prosecna ocena na ispitu", e));
    }

    /**
     * Rezultati ispita
     */
    public Mono<List<RezultatIspitaResponse>> getRezultatiIspita(Long ispitId, String sortBy) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/ispiti/{id}/rezultati")
                        .queryParam("sortBy", sortBy)
                        .build(ispitId))
                .retrieve()
                .bodyToFlux(RezultatIspitaResponse.class)
                .collectList()
                .doOnError(e -> log.error("Error fetching rezultati ispita", e));
    }

    /**
     * Prijavi studenta na ispit
     */
    public Mono<PrijavaIspitaResponse> prijaviIspit(PrijavaIspitaRequest request) {
        return webClient.post()
                .uri("/ispiti/prijava")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(PrijavaIspitaResponse.class)
                .doOnError(e -> log.error("Error prijavi ispit", e));
    }

    /**
     * Dodaj izlazak na ispit
     */
    public Mono<IzlazakNaIspitResponse> dodajIzlazak(IzlazakNaIspitRequest request) {
        return webClient.post()
                .uri("/ispiti/izlazak")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(IzlazakNaIspitResponse.class)
                .doOnError(e -> log.error("Error dodaj izlazak", e));
    }

    /**
     * Svi ispitni rokovi
     */
    public Mono<List<IspitniRokResponse>> getAllIspitniRokovi() {
        return webClient.get()
                .uri("/ispitni-rokovi/all")
                .retrieve()
                .bodyToFlux(IspitniRokResponse.class)
                .collectList()
                .doOnError(e -> log.error("Error fetching ispitni rokovi", e));
    }

    /**
     * Dodaj ispitni rok
     */
    public Mono<IspitniRokResponse> addIspitniRok(IspitniRokRequest request) {
        return webClient.post()
                .uri("/ispitni-rokovi/add")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(IspitniRokResponse.class)
                .doOnError(e -> log.error("Error adding ispitni rok", e));
    }

    /**
     * Dodaj ispit
     */
    public Mono<IspitResponse> addIspit(IspitRequest request) {
        return webClient.post()
                .uri("/ispiti/add")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(IspitResponse.class)
                .doOnError(e -> log.error("Error adding ispit", e));
    }
}