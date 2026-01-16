package org.raflab.studsluzba.client.api;

import lombok.extern.slf4j.Slf4j;
import org.raflab.studsluzba.client.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
public class StudentApiClient {

    @Autowired
    private WebClient webClient;

    /**
     * Preuzmi profil studenta po ID-u indeksa
     */
    public Mono<StudentProfileDTO> getStudentProfile(Long studentIndeksId) {
        return webClient.get()
                .uri("/students/{id}/profile", studentIndeksId)
                .retrieve()
                .bodyToMono(StudentProfileDTO.class)
                .doOnError(e -> log.error("Error fetching student profile", e));
    }

    /**
     * Preuzmi studenta po broju indeksa
     */
    public Mono<StudentProfileDTO> getStudentByIndeks(Integer godina, Integer broj, String oznaka) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/students/indeks")
                        .queryParam("godina", godina)
                        .queryParam("broj", broj)
                        .queryParam("oznaka", oznaka)
                        .build())
                .retrieve()
                .bodyToMono(StudentProfileDTO.class)
                .doOnError(e -> log.error("Error fetching student by indeks", e));
    }

    /**
     * Preuzmi položene predmete
     */
    public Mono<PageDTO<PolozenPredmetDTO>> getPolozeniPredmeti(Long studentIndeksId, int page, int size) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/students/{id}/polozeni-predmeti")
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build(studentIndeksId))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PageDTO<PolozenPredmetDTO>>() {})
                .doOnError(e -> log.error("Error fetching polozeni predmeti", e));
    }

    /**
     * Preuzmi nepoložene predmete
     */
    public Mono<PageDTO<NepolozenPredmetDTO>> getNepolozeniPredmeti(Long studentIndeksId, int page, int size) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/students/{id}/nepolozeni-predmeti")
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build(studentIndeksId))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PageDTO<NepolozenPredmetDTO>>() {})
                .doOnError(e -> log.error("Error fetching nepolozeni predmeti", e));
    }

    /**
     * Preuzmi upisane godine
     */
    public Mono<List<UpisGodineDTO>> getUpisaneGodine(Long studentIndeksId) {
        return webClient.get()
                .uri("/students/{id}/upisane-godine", studentIndeksId)
                .retrieve()
                .bodyToFlux(UpisGodineDTO.class)
                .collectList()
                .doOnError(e -> log.error("Error fetching upisane godine", e));
    }

    /**
     * Preuzmi obnovljene godine
     */
    public Mono<List<ObnovaGodineDTO>> getObnovljeneGodine(Long studentIndeksId) {
        return webClient.get()
                .uri("/students/{id}/obnovljene-godine", studentIndeksId)
                .retrieve()
                .bodyToFlux(ObnovaGodineDTO.class)
                .collectList()
                .doOnError(e -> log.error("Error fetching obnovljene godine", e));
    }

    /**
     * Preuzmi preostali iznos za uplatu
     */
    public Mono<PreostaliIznosDTO> getPreostaliIznos(Long studentIndeksId) {
        return webClient.get()
                .uri("/students/{id}/preostali-iznos-za-uplatu", studentIndeksId)
                .retrieve()
                .bodyToMono(PreostaliIznosDTO.class)
                .doOnError(e -> log.error("Error fetching preostali iznos", e));
    }

    /**
     * Pretraga studenata po imenu i prezimenu
     */
    public Mono<PageDTO<StudentPodaciDTO>> searchStudents(String ime, String prezime, int page, int size) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/students/ime-prezime")
                        .queryParam("ime", ime)
                        .queryParam("prezime", prezime)
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PageDTO<StudentPodaciDTO>>() {})
                .doOnError(e -> log.error("Error searching students", e));
    }

    /**
     * Pretraga studenata po srednjoj školi
     */
    public Mono<List<StudentPodaciDTO>> getStudentsBySrednjaSkola(Long srednjaSkolaId) {
        return webClient.get()
                .uri("/students/srednja-skola/{id}", srednjaSkolaId)
                .retrieve()
                .bodyToFlux(StudentPodaciDTO.class)
                .collectList()
                .doOnError(e -> log.error("Error fetching students by srednja skola", e));
    }

    /**
     * Upiši studenta na godinu
     */
    public Mono<UpisGodineDTO> upisStudentaNaGodinu(Long studentIndeksId, UpisGodineRequestDTO request) {
        return webClient.post()
                .uri("/students/{id}/upis-godine", studentIndeksId)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(UpisGodineDTO.class)
                .doOnError(e -> log.error("Error upis studenta na godinu", e));
    }

    /**
     * Obnovi godinu za studenta
     */
    public Mono<ObnovaGodineDTO> obnovaGodine(Long studentIndeksId, ObnovaGodineRequestDTO request) {
        return webClient.post()
                .uri("/students/{id}/obnova-godine", studentIndeksId)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ObnovaGodineDTO.class)
                .doOnError(e -> log.error("Error obnova godine", e));
    }

    /**
     * Dodaj uplatu
     */
    public Mono<UplataDTO> dodajUplatu(Long studentIndeksId, UplataRequestDTO request) {
        return webClient.post()
                .uri("/students/{id}/uplate", studentIndeksId)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(UplataDTO.class)
                .doOnError(e -> log.error("Error dodaj uplatu", e));
    }
}