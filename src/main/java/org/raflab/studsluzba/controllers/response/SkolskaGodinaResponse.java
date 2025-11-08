package org.raflab.studsluzba.controllers.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class SkolskaGodinaResponse {

    private Long id;
    private String naziv;
    private LocalDate pocetakZimskog;
    private LocalDate krajZimskog;
    private LocalDate pocetakLetnjeg;
    private LocalDate krajLetnjeg;
    private Boolean aktivna;
}