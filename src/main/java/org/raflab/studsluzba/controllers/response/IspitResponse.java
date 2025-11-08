package org.raflab.studsluzba.controllers.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IspitResponse {
    private Long id;

    // Student podaci
    private Long studentIndeksId;
    private String studentIme;
    private String studentPrezime;
    private Integer studentBrojIndeksa;
    private Integer studentGodinaIndeksa;

    // Predmet podaci
    private Long predmetId;
    private String predmetSifra;
    private String predmetNaziv;

    // Ispitni rok podaci
    private Long ispitniRokId;
    private String ispitniRokNaziv;
    private Long skolskaGodinaId;
    private String skolskaGodinaNaziv;

    // Ispit podaci
    private Integer ocena;
    private LocalDate datumPolaganja;
    private String napomena;
}