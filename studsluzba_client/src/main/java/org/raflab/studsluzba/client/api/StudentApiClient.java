package org.raflab.studsluzba.client.api;

import lombok.extern.slf4j.Slf4j;
import org.raflab.studsluzba.client.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
public class StudentApiClient {

    private final WebClient webClient;

    public StudentApiClient(@Value("${api.base.url}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
        log.info("StudentApiClient initialized with base URL: {}", baseUrl);
    }

    // ============================================
    // STUDENT PROFILE & BASIC INFO
    // ============================================

    public Mono<StudentProfileDTO> getStudentProfile(Long studentIndeksId) {
        return webClient.get()
                .uri("/students/{id}/profile", studentIndeksId)
                .retrieve()
                .bodyToMono(StudentProfileDTO.class);
    }

    // ============================================
    // STATISTICS - ✅ NOVO!
    // ============================================

    public Mono<StudentStatisticsDTO> getStatistics(Long studentIndeksId) {
        log.debug("Fetching statistics for student: {}", studentIndeksId);

        return webClient.get()
                .uri("/students/{id}/statistics", studentIndeksId)
                .retrieve()
                .bodyToMono(StudentStatisticsDTO.class)
                .doOnError(error -> log.error("Failed to fetch statistics", error));
    }

    // ============================================
    // SEARCH ENDPOINTS
    // ============================================

    public Mono<Page<StudentPodaciDTO>> searchStudents(String ime, String prezime, int page, int size) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/students/ime-prezime")
                        .queryParam("ime", ime)
                        .queryParam("prezime", prezime)
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Page<StudentPodaciDTO>>() {})
                .doOnError(error -> log.error("Failed to search students", error));
    }

    public Mono<Page<StudentPodaciDTO>> findByIndeks(int godina, int broj, String oznaka) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/students/indeks")
                        .queryParam("godina", godina)
                        .queryParam("broj", broj)
                        .queryParam("oznaka", oznaka)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Page<StudentPodaciDTO>>() {})
                .doOnError(error -> log.error("Failed to find by indeks", error));
    }

    public Mono<List<StudentPodaciDTO>> findBySrednjaSkola(Long srednjaSkolaId) {
        return webClient.get()
                .uri("/students/srednja-skola/{id}", srednjaSkolaId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<StudentPodaciDTO>>() {})
                .doOnError(error -> log.error("Failed to find by srednja skola", error));
    }

    // ============================================
    // EXAM RESULTS
    // ============================================

    public Mono<PageDTO<PolozenPredmetDTO>> getPolozeniPredmeti(Long studentIndeksId, int page, int size) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/students/{id}/polozeni-predmeti")
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build(studentIndeksId))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PageDTO<PolozenPredmetDTO>>() {});    }

    public Mono<PageDTO<NepolozenPredmetDTO>> getNepolozeniPredmeti(Long studentIndeksId, int page, int size) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/students/{id}/nepolozeni-predmeti")
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build(studentIndeksId))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PageDTO<NepolozenPredmetDTO>>() {});
    }

    // ============================================
    // ENROLLMENT (UPIS & OBNOVA GODINE)
    // ============================================

    public Mono<List<UpisGodineDTO>> getUpisaneGodine(Long studentIndeksId) {
        return webClient.get()
                .uri("/students/{id}/upisane-godine", studentIndeksId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<UpisGodineDTO>>() {})
                .doOnError(error -> log.error("Failed to fetch upisane godine", error));
    }

    public Mono<List<ObnovaGodineDTO>> getObnovljeneGodine(Long studentIndeksId) {
        return webClient.get()
                .uri("/students/{id}/obnovljene-godine", studentIndeksId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ObnovaGodineDTO>>() {})
                .doOnError(error -> log.error("Failed to fetch obnovljene godine", error));
    }

    public Mono<UpisGodineDTO> upisNaGodinu(Long studentIndeksId, UpisGodineRequestDTO request) {
        return webClient.post()
                .uri("/students/{id}/upis-godine", studentIndeksId)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(UpisGodineDTO.class)
                .doOnError(error -> log.error("Failed to enroll student", error));
    }

    public Mono<ObnovaGodineDTO> obnovaGodine(Long studentIndeksId, ObnovaGodineRequestDTO request) {
        return webClient.post()
                .uri("/students/{id}/obnova-godine", studentIndeksId)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ObnovaGodineDTO.class)
                .doOnError(error -> log.error("Failed to renew year", error));
    }

    // ============================================
    // PAYMENTS (UPLATE)
    // ============================================

    public Mono<List<UplataDTO>> getUplate(Long studentIndeksId) {
        return webClient.get()
                .uri("/students/{id}/uplate", studentIndeksId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<UplataDTO>>() {})
                .doOnError(error -> log.error("Failed to fetch uplate", error));
    }

    public Mono<PreostaliIznosDTO> getPreostaliIznos(Long studentIndeksId) {
        return webClient.get()
                .uri("/students/{id}/preostali-iznos-za-uplatu", studentIndeksId)
                .retrieve()
                .bodyToMono(PreostaliIznosDTO.class)
                .doOnError(error -> log.error("Failed to fetch preostali iznos", error));
    }

    public Mono<UplataDTO> dodajUplatu(Long studentIndeksId, UplataRequestDTO request) {
        return webClient.post()
                .uri("/students/{id}/uplate", studentIndeksId)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(UplataDTO.class)
                .doOnError(error -> log.error("Failed to add payment", error));
    }
}