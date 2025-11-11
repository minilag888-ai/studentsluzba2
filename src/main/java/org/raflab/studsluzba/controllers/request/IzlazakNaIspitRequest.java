package org.raflab.studsluzba.controllers.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IzlazakNaIspitRequest {

    @NotNull(message = "Prijava ispita je obavezna")
    private Long prijavaIspitaId;

    @NotNull(message = "Poeni sa ispita su obavezni")
    @Min(value = 0, message = "Minimalno 0 poena")
    @Max(value = 100, message = "Maksimalno 100 poena")
    private Integer poeniIspit;

    private String napomena;

    private Boolean ponisteno;  // Da li student poništava ispit
}