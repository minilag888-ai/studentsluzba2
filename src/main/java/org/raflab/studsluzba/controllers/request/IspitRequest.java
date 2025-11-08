package org.raflab.studsluzba.controllers.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IspitRequest {

    @NotNull(message = "Student indeks je obavezan")
    private Long studentIndeksId;

    @NotNull(message = "Predmet je obavezan")
    private Long predmetId;

    @NotNull(message = "Ispitni rok je obavezan")
    private Long ispitniRokId;

    @NotNull(message = "Ocena je obavezna")
    @Min(value = 5, message = "Minimalna ocena je 5")
    @Max(value = 10, message = "Maksimalna ocena je 10")
    private Integer ocena;

    @NotNull(message = "Datum polaganja je obavezan")
    private LocalDate datumPolaganja;

    private String napomena;
}