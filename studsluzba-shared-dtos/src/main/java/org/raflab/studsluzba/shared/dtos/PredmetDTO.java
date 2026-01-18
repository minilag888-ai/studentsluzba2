package org.raflab.studsluzba.shared.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PredmetDTO {
    private Long id;
    private String sifra;
    private String naziv;
    private String opis;
    private Integer espb;
    private boolean obavezan;
    private String studijskiProgramNaziv;  // Za prikaz naziva studijskog programa
}