package org.raflab.studsluzba.controllers.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrojPokusajaResponse {
    private Long studentIndeksId;
    private String imeStudenta;
    private String prezimeStudenta;
    private Long predmetId;
    private String nazivPredmeta;
    private Long brojPokusaja;
}