package org.raflab.studsluzba.client.api;

import lombok.extern.slf4j.Slf4j;
import org.raflab.studsluzba.client.dto.ProsecnaOcenaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
public class PredmetApiClient {

    @Autowired
    private WebClient webClient;

    /**
     * Svi predmeti
     */
    public Mono<List<PredmetResponse>> getAllPredmeti() {
        return webClient.get()
                .uri("/predmeti/all")
                .retrieve()
                .bodyToFlux(PredmetResponse.class)
                .collectList()
                .doOnError(e -> log.error("Error fetching all predmeti", e));
    }

    /**
     * Predmeti na studijskom programu
     */
    public Mono<List<PredmetDTO>> getPredmetiNaStudijskomProgramu(Long studijskiProgramId) {
        return webClient.get()
                .uri("/predmeti/studijski-program/{id}", studijskiProgramId)
                .retrieve()
                .bodyToFlux(PredmetDTO.class)
                .collectList()
                .doOnError(e -> log.error("Error fetching predmeti na studijskom programu", e));
    }

    /**
     * Prosečna ocena na predmetu
     */
    public Mono<ProsecnaOcenaDTO> getProsecnaOcena(Long predmetId, Integer odGodine, Integer doGodine) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/predmeti/{id}/prosecna-ocena")
                        .queryParam("odGodine", odGodine)
                        .queryParam("doGodine", doGodine)
                        .build(predmetId))
                .retrieve()
                .bodyToMono(ProsecnaOcenaDTO.class)
                .doOnError(e -> log.error("Error fetching prosecna ocena", e));
    }

    /**
     * Dodaj novi predmet
     */
    public Mono<PredmetResponse> addPredmet(PredmetRequest request) {
        return webClient.post()
                .uri("/predmeti/add")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(PredmetResponse.class)
                .doOnError(e -> log.error("Error adding predmet", e));
    }
}