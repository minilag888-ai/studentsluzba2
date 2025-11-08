package org.raflab.studsluzba.controllers.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PredmetResponse {
    private Long id;
    private String sifra;
    private String naziv;
    private String opis;
    private Integer espb;
    private Long studijskiProgramId;
    private String studijskiProgramNaziv;
    private Boolean obavezan;
}