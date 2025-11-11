package org.raflab.studsluzba.controllers.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IspitResponse {
    private Long id;

    private Long predmetId;
    private String predmetSifra;
    private String predmetNaziv;

    private Long ispitniRokId;
    private String ispitniRokNaziv;

    private Long skolskaGodinaId;
    private String skolskaGodinaNaziv;

    private Long nastavnikId;
    private String nastavnikIme;
    private String nastavnikPrezime;

    private LocalDate datumOdrzavanja;
    private LocalDateTime vremePocetka;
    private Boolean zakljucen;
    private String napomena;

}