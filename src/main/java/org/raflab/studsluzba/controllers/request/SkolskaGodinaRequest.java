package org.raflab.studsluzba.controllers.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class SkolskaGodinaRequest {

    @NotNull
    private String naziv;

    @NotNull
    private LocalDate pocetakZimskog;

    @NotNull
    private LocalDate krajZimskog;

    @NotNull
    private LocalDate pocetakLetnjeg;

    @NotNull
    private LocalDate krajLetnjeg;

    private Boolean aktivna;
}