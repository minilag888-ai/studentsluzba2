package org.raflab.studsluzba.client.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IzlazakNaIspitResponse {
    private Long id;

    // Student podaci
    private Long studentIndeksId;
    private String imeStudenta;
    private String prezimeStudenta;
    private Integer brojIndeksa;
    private Integer godinaIndeksa;
    private String studProgramOznaka;

    // Predmet podaci
    private String nazivPredmeta;
    private String sifraPredmeta;

    // Rezultat
    private Integer poeniPredispitne;
    private Integer poeniIspit;
    private Integer ukupnoPoeni;
    private Integer ocena;
    private String napomena;
    private Boolean ponisteno;
    private Boolean polozio;
}