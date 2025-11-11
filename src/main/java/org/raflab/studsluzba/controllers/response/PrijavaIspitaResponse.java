package org.raflab.studsluzba.controllers.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrijavaIspitaResponse {
    private Long id;

    // Student podaci
    private Long studentIndeksId;
    private Integer brojIndeksa;
    private Integer godinaIndeksa;
    private String studProgramOznaka;
    private String imeStudenta;
    private String prezimeStudenta;

    // Ispit podaci
    private Long ispitId;
    private String nazivPredmeta;
    private String sifraPredmeta;
    private LocalDate datumOdrzavanjaIspita;

    // Prijava podaci
    private LocalDate datumPrijave;
    private Boolean izasao;
}