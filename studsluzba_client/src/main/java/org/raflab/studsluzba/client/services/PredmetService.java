package org.raflab.studsluzba.client.services;

import lombok.extern.slf4j.Slf4j;
import org.raflab.studsluzba.client.api.PredmetApiClient;
import org.raflab.studsluzba.client.api.request.PredmetRequest;
import org.raflab.studsluzba.client.api.response.PredmetResponse;
import org.raflab.studsluzba.client.dto.PredmetDTO;
import org.raflab.studsluzba.client.dto.ProsecnaOcenaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
public class PredmetService {

    @Autowired
    private PredmetApiClient apiClient;

    /**
     * Svi predmeti
     */
    public Mono<List<PredmetResponse>> getAllPredmeti() {
        log.info("Fetching all predmeti");
        return apiClient.getAllPredmeti();
    }

    /**
     * Predmeti na studijskom programu
     */
    public Mono<List<PredmetDTO>> getPredmetiNaStudijskomProgramu(Long studijskiProgramId) {
        log.info("Fetching predmeti for studijski program ID: {}", studijskiProgramId);
        return apiClient.getPredmetiNaStudijskomProgramu(studijskiProgramId);
    }

    /**
     * Prosečna ocena na predmetu
     */
    public Mono<ProsecnaOcenaDTO> getProsecnaOcena(Long predmetId, Integer odGodine, Integer doGodine) {
        log.info("Fetching prosecna ocena for predmet {}, period {}-{}", predmetId, odGodine, doGodine);

        // Validacija
        if (odGodine > doGodine) {
            return Mono.error(new RuntimeException("Godina 'od' ne može biti veća od godine 'do'!"));
        }

        return apiClient.getProsecnaOcena(predmetId, odGodine, doGodine);
    }

    /**
     * Dodaj novi predmet
     */
    public Mono<PredmetResponse> addPredmet(PredmetRequest request) {
        log.info("Adding new predmet: {}", request.getNaziv());

        // Validacija
        if (request.getSifra() == null || request.getSifra().isEmpty()) {
            return Mono.error(new RuntimeException("Šifra predmeta je obavezna!"));
        }

        if (request.getEspb() == null || request.getEspb() < 1 || request.getEspb() > 12) {
            return Mono.error(new RuntimeException("ESPB mora biti između 1 i 12!"));
        }

        return apiClient.addPredmet(request);
    }
}