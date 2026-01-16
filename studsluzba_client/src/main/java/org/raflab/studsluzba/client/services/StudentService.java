package org.raflab.studsluzba.client.services;

import lombok.extern.slf4j.Slf4j;
import org.raflab.studsluzba.client.api.PageDTO;
import org.raflab.studsluzba.client.api.StudentApiClient;
import org.raflab.studsluzba.client.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import org.raflab.studsluzba.client.api.request.*;

import java.util.List;

@Slf4j
@Service
public class StudentService {

    @Autowired
    private StudentApiClient apiClient;

    /**
     * Preuzmi profil studenta
     */
    public Mono<StudentProfileDTO> getStudentProfile(Long studentIndeksId) {
        log.info("Fetching student profile for indeks ID: {}", studentIndeksId);
        return apiClient.getStudentProfile(studentIndeksId);
    }

    /**
     * Preuzmi studenta po broju indeksa
     */
    public Mono<StudentProfileDTO> findByIndeks(Integer godina, Integer broj, String oznaka) {
        log.info("Searching student by indeks: {}/{}/{}", godina, broj, oznaka);
        return apiClient.getStudentByIndeks(godina, broj, oznaka);
    }

    /**
     * Pretraga studenata
     */
    public Mono<PageDTO<StudentPodaciDTO>> searchStudents(String ime, String prezime, int page, int size) {
        log.info("Searching students: ime={}, prezime={}, page={}", ime, prezime, page);
        return apiClient.searchStudents(ime, prezime, page, size);
    }

    /**
     * Studenti po srednjoj školi
     */
    public Mono<List<StudentPodaciDTO>> findBySrednjaSkola(Long srednjaSkolaId) {
        log.info("Fetching students by srednja skola ID: {}", srednjaSkolaId);
        return apiClient.getStudentsBySrednjaSkola(srednjaSkolaId);
    }

    /**
     * Položeni predmeti
     */
    public Mono<PageDTO<PolozenPredmetDTO>> getPolozeniPredmeti(Long studentIndeksId, int page, int size) {
        log.info("Fetching polozeni predmeti for student {}, page {}", studentIndeksId, page);
        return apiClient.getPolozeniPredmeti(studentIndeksId, page, size);
    }

    /**
     * Nepoloženi predmeti
     */
    public Mono<PageDTO<NepolozenPredmetDTO>> getNepolozeniPredmeti(Long studentIndeksId, int page, int size) {
        log.info("Fetching nepolozeni predmeti for student {}, page {}", studentIndeksId, page);
        return apiClient.getNepolozeniPredmeti(studentIndeksId, page, size);
    }

    /**
     * Upisane godine
     */
    public Mono<List<UpisGodineDTO>> getUpisaneGodine(Long studentIndeksId) {
        log.info("Fetching upisane godine for student {}", studentIndeksId);
        return apiClient.getUpisaneGodine(studentIndeksId);
    }

    /**
     * Obnovljene godine
     */
    public Mono<List<ObnovaGodineDTO>> getObnovljeneGodine(Long studentIndeksId) {
        log.info("Fetching obnovljene godine for student {}", studentIndeksId);
        return apiClient.getObnovljeneGodine(studentIndeksId);
    }

    /**
     * Preostali iznos za uplatu
     */
    public Mono<PreostaliIznosDTO> getPreostaliIznos(Long studentIndeksId) {
        log.info("Fetching preostali iznos for student {}", studentIndeksId);
        return apiClient.getPreostaliIznos(studentIndeksId);
    }

    /**
     * Upis studenta na godinu
     */
    public Mono<UpisGodineDTO> upisNaGodinu(Long studentIndeksId, UpisGodineRequestDTO request) {
        log.info("Upis studenta {} na godinu {}", studentIndeksId, request.getGodinaStudija());
        return apiClient.upisStudentaNaGodinu(studentIndeksId, request);
    }

    /**
     * Obnova godine
     */
    public Mono<ObnovaGodineDTO> obnovaGodine(Long studentIndeksId, ObnovaGodineRequestDTO request) {
        log.info("Obnova godine za studenta {}, godina {}", studentIndeksId, request.getGodinaStudija());

        // Validacija ESPB (max 60)
        int ukupnoESPB = request.getPredmetIds().stream()
                .mapToInt(id -> 6) // Pretpostavljamo prosečno 6 ESPB
                .sum();

        if (ukupnoESPB > 60) {
            return Mono.error(new RuntimeException("Ukupan ESPB ne može biti veći od 60!"));
        }

        return apiClient.obnovaGodine(studentIndeksId, request);
    }

    /**
     * Dodaj uplatu
     */
    public Mono<UplataDTO> dodajUplatu(Long studentIndeksId, UplataRequestDTO request) {
        log.info("Dodavanje uplate za studenta {}, iznos: {} EUR", studentIndeksId, request.getIznosEur());

        // Validacija
        if (request.getIznosEur() == null || request.getIznosEur() <= 0) {
            return Mono.error(new RuntimeException("Iznos mora biti veći od 0!"));
        }

        return apiClient.dodajUplatu(studentIndeksId, request);
    }

    /**
     * Izračunaj ukupan ESPB
     */
    public int calculateTotalESPB(List<PolozenPredmetDTO> polozeni) {
        return polozeni.stream()
                .mapToInt(PolozenPredmetDTO::getEspb)
                .sum();
    }

    /**
     * Izračunaj prosečnu ocenu
     */
    public double calculateAverageGrade(List<PolozenPredmetDTO> polozeni) {
        if (polozeni.isEmpty()) {
            return 0.0;
        }

        return polozeni.stream()
                .mapToInt(PolozenPredmetDTO::getOcena)
                .average()
                .orElse(0.0);
    }
}