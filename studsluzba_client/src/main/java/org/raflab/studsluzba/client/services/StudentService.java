package org.raflab.studsluzba.client.services;

import lombok.extern.slf4j.Slf4j;
import org.raflab.studsluzba.client.api.PageDTO;
import org.raflab.studsluzba.client.api.StudentApiClient;
import org.raflab.studsluzba.client.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
public class StudentService {

    @Autowired
    private StudentApiClient studentApiClient;

    // ============================================
    // STUDENT PROFILE & BASIC INFO
    // ============================================

    public Mono<StudentProfileDTO> getStudentProfile(Long studentIndeksId) {
        return studentApiClient.getStudentProfile(studentIndeksId);
    }

    // ============================================
    // SEARCH METHODS
    // ============================================

    /**
     * Pretraga studenata po različitim kriterijumima
     */
    public Mono<Page<StudentPodaciDTO>> searchStudents(String ime, String prezime, int page, int size) {
        log.debug("Searching students: ime={}, prezime={}, page={}, size={}", ime, prezime, page, size);
        return studentApiClient.searchStudents(ime, prezime, page, size);
    }

    /**
     * Pretraga po indeksu (godina/broj)
     */
    public Mono<Page<StudentPodaciDTO>> findByIndeks(int godina, int broj, String oznaka) {
        log.debug("Finding student by indeks: {}/{} - {}", godina, broj, oznaka);
        return studentApiClient.findByIndeks(godina, broj, oznaka);
    }

    /**
     * Pretraga po srednjoj školi
     */
    public Mono<List<StudentPodaciDTO>> findBySrednjaSkola(Long srednjaSkolaId) {
        log.debug("Finding students by srednja skola: {}", srednjaSkolaId);
        return studentApiClient.findBySrednjaSkola(srednjaSkolaId);
    }

    // ============================================
    // EXAM RESULTS (POLOZENI & NEPOLOZENI)
    // ============================================

    public Mono<PageDTO<PolozenPredmetDTO>> getPolozeniPredmeti(Long studentIndeksId, int page, int size) {
        return studentApiClient.getPolozeniPredmeti(studentIndeksId, page, size);
    }

    public Mono<PageDTO<NepolozenPredmetDTO>> getNepolozeniPredmeti(Long studentIndeksId, int page, int size) {
        return studentApiClient.getNepolozeniPredmeti(studentIndeksId, page, size);
    }

    // ============================================
    // ENROLLMENT (UPIS & OBNOVA GODINE)
    // ============================================

    /**
     *  DEBUG: Učitaj upisane godine sa detaljnim logom
     */
    public Mono<List<UpisGodineDTO>> getUpisaneGodine(Long studentIndeksId) {
        log.info("🔍 [DEBUG] Calling API: GET /students/{}/upisane-godine", studentIndeksId);

        return studentApiClient.getUpisaneGodine(studentIndeksId)
                .doOnNext(response -> {
                    log.info(" [DEBUG] RAW RESPONSE from backend:");
                    log.info("   - Type: {}", response.getClass().getName());
                    log.info("   - Size: {}", response.size());
                    log.info("   - Is Empty: {}", response.isEmpty());

                    if (!response.isEmpty()) {
                        log.info(" [DEBUG] Response content:");
                        for (int i = 0; i < response.size(); i++) {
                            UpisGodineDTO dto = response.get(i);
                            log.info("   [{}] ID={}, Godina={}, ESPB={}, Skolska={}, Datum={}",
                                    i, dto.getId(), dto.getGodinaStudija(), dto.getUkupnoESPB(),
                                    dto.getSkolskaGodina(), dto.getDatumUpisa());
                        }
                    } else {
                        log.warn(" [DEBUG] Backend returned EMPTY list!");
                    }
                })
                .doOnError(error -> {
                    log.error(" [DEBUG] API call FAILED!", error);
                    log.error("   - Error type: {}", error.getClass().getName());
                    log.error("   - Error message: {}", error.getMessage());
                });
    }

    /**
     *  DEBUG: Učitaj obnovljene godine sa detaljnim logom
     */
    public Mono<List<ObnovaGodineDTO>> getObnovljeneGodine(Long studentIndeksId) {
        log.info("🔍 [DEBUG] Calling API: GET /students/{}/obnovljene-godine", studentIndeksId);

        return studentApiClient.getObnovljeneGodine(studentIndeksId)
                .doOnNext(response -> {
                    log.info(" [DEBUG] Received {} obnovljene godine", response.size());
                })
                .doOnError(error -> {
                    log.error(" [DEBUG] Failed to load obnovljene godine", error);
                });
    }

    /**
     * Upiši studenta na godinu
     */
    public Mono<UpisGodineDTO> upisNaGodinu(Long studentIndeksId, UpisGodineRequestDTO request) {
        log.info(" [DEBUG] Sending POST /students/{}/upis-godine", studentIndeksId);
        log.info("   Request: godina={}, skolskaId={}, predmeti={}",
                request.getGodinaStudija(), request.getSkolskaGodinaId(), request.getPredmetIds().size());

        return studentApiClient.upisNaGodinu(studentIndeksId, request)
                .doOnNext(response -> {
                    log.info(" [DEBUG] UPIS SUCCESS!");
                    log.info("   Response: ID={}, godina={}, ESPB={}, skolska={}",
                            response.getId(), response.getGodinaStudija(), response.getUkupnoESPB(), response.getSkolskaGodina());
                })
                .doOnError(error -> {
                    log.error(" [DEBUG] UPIS FAILED!", error);
                });
    }

    /**
     * Obnovi godinu studija
     */
    public Mono<ObnovaGodineDTO> obnovaGodine(Long studentIndeksId, ObnovaGodineRequestDTO request) {
        log.info(" [DEBUG] Sending POST /students/{}/obnova-godine", studentIndeksId);

        return studentApiClient.obnovaGodine(studentIndeksId, request)
                .doOnNext(response -> {
                    log.info(" [DEBUG] OBNOVA SUCCESS!");
                })
                .doOnError(error -> {
                    log.error(" [DEBUG] OBNOVA FAILED!", error);
                });
    }

    // ============================================
    // PAYMENTS (UPLATE)
    // ============================================

    /**
     *  DEBUG: Učitaj uplate sa detaljnim logom
     */
    public Mono<List<UplataDTO>> getUplate(Long studentIndeksId) {
        log.info(" [DEBUG] Calling API: GET /students/{}/uplate", studentIndeksId);

        return studentApiClient.getUplate(studentIndeksId)
                .doOnNext(response -> {
                    log.info(" [DEBUG] RAW RESPONSE from backend:");
                    log.info("   - Type: {}", response.getClass().getName());
                    log.info("   - Size: {}", response.size());

                    if (!response.isEmpty()) {
                        log.info(" [DEBUG] Uplate content:");
                        for (int i = 0; i < response.size(); i++) {
                            UplataDTO dto = response.get(i);
                            log.info("   [{}] ID={}, Datum={}, EUR={}, RSD={}",
                                    i, dto.getId(), dto.getDatumUplate(), dto.getIznosEur(), dto.getIznosRsd());
                        }
                    } else {
                        log.warn(" [DEBUG] Backend returned EMPTY list of uplate!");
                    }
                })
                .doOnError(error -> {
                    log.error(" [DEBUG] API call FAILED for uplate!", error);
                    log.error("   - Error type: {}", error.getClass().getName());
                    log.error("   - Error message: {}", error.getMessage());
                });
    }

    /**
     * Učitaj preostali iznos za uplatu
     */
    public Mono<PreostaliIznosDTO> getPreostaliIznos(Long studentIndeksId) {
        return studentApiClient.getPreostaliIznos(studentIndeksId);
    }

    /**
     * Evidentiraj uplatu
     */
    public Mono<UplataDTO> dodajUplatu(Long studentIndeksId, UplataRequestDTO request) {
        log.info(" [DEBUG] Sending POST /students/{}/uplate", studentIndeksId);
        log.info("   Request: datum={}, iznos={}", request.getDatumUplate(), request.getIznosEur());

        return studentApiClient.dodajUplatu(studentIndeksId, request)
                .doOnNext(response -> {
                    log.info(" [DEBUG] UPLATA SUCCESS!");
                    log.info("   Response: ID={}, EUR={}, kurs={}, RSD={}",
                            response.getId(), response.getIznosEur(), response.getSrednjiKurs(), response.getIznosRsd());
                })
                .doOnError(error -> {
                    log.error(" [DEBUG] UPLATA FAILED!", error);
                });
    }

    // ============================================
    // HELPER METHODS
    // ============================================

    /**
     * Izračunaj ukupan broj ESPB bodova
     */
    public int calculateTotalESPB(List<PolozenPredmetDTO> predmeti) {
        return predmeti.stream()
                .mapToInt(PolozenPredmetDTO::getEspb)
                .sum();
    }

    /**
     * Izračunaj prosečnu ocenu
     */
    public double calculateAverageGrade(List<PolozenPredmetDTO> predmeti) {
        if (predmeti.isEmpty()) {
            return 0.0;
        }
        return predmeti.stream()
                .mapToInt(PolozenPredmetDTO::getOcena)
                .average()
                .orElse(0.0);
    }
    // ============================================
    // STATISTICS
    // ============================================

    /**
     * Učitaj statistiku studenta (ESPB, prosek)
     */
    public Mono<StudentStatisticsDTO> getStatistics(Long studentIndeksId) {
        log.info("🔍 [DEBUG] Calling API: GET /students/{}/statistics", studentIndeksId);

        return studentApiClient.getStatistics(studentIndeksId)
                .doOnNext(response -> {
                    log.info(" [DEBUG] STATISTICS SUCCESS!");
                    log.info("   ESPB: {}, Prosek: {}, Položenih: {}, Nepoloženih: {}",
                            response.getUkupnoESPB(), response.getProsecnaOcena(),
                            response.getBrojPolozenihPredmeta(), response.getBrojNepolozenihPredmeta());
                })
                .doOnError(error -> {
                    log.error(" [DEBUG] STATISTICS FAILED!", error);
                });
    }
}