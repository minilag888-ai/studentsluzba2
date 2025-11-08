package org.raflab.studsluzba.controllers.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OsvojeniPoeniResponse {
    private Long id;

    // Student podaci
    private Long studentIndeksId;
    private String studentIme;
    private String studentPrezime;
    private Integer studentBrojIndeksa;
    private Integer studentGodinaIndeksa;

    // Predispitna obaveza
    private Long predispitnaObavezaId;
    private String obavezaVrsta;
    private Integer maxPoena;

    // Predmet podaci
    private Long predmetId;
    private String predmetSifra;
    private String predmetNaziv;

    // Školska godina
    private Long skolskaGodinaId;
    private String skolskaGodinaNaziv;

    // Osvojeni poeni
    private Integer poeni;
}