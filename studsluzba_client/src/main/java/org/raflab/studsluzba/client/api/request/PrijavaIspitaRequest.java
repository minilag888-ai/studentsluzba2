package org.raflab.studsluzba.client.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrijavaIspitaRequest {

    @NotNull(message = "Student indeks je obavezan")
    private Long studentIndeksId;

    @NotNull(message = "Ispit je obavezan")
    private Long ispitId;

    private LocalDate datumPrijave;  // Ako je null, koristi trenutni datum
}