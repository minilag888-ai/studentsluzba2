package org.raflab.studsluzba.controllers.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IspitniRokRequest {

    @NotNull(message = "Naziv je obavezan")
    private String naziv;

    @NotNull(message = "Školska godina je obavezna")
    private Long skolskaGodinaId;

    @NotNull(message = "Datum početka je obavezan")
    private LocalDate pocetak;

    @NotNull(message = "Datum kraja je obavezan")
    private LocalDate kraj;

    private Boolean aktivan;
}