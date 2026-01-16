package org.raflab.studsluzba.client.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RezultatIspitaResponse {
    private Long studentIndeksId;
    private String studijskiProgram;
    private Integer godinaUpisa;
    private Integer brojIndeksa;
    private String imeStudenta;
    private String prezimeStudenta;
    private Integer poeniPredispitne;
    private Integer poeniIspit;
    private Integer ukupnoPoeni;
    private Integer ocena;
    private Boolean polozio;
}