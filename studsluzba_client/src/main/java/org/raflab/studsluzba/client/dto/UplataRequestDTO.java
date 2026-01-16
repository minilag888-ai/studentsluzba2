package org.raflab.studsluzba.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UplataRequestDTO {
    @NotNull(message = "Datum uplate je obavezan")
    private LocalDate datumUplate;

    @NotNull(message = "Iznos je obavezan")
    @DecimalMin(value = "0.01", message = "Iznos mora biti veći od 0")
    private Double iznosEur;
}