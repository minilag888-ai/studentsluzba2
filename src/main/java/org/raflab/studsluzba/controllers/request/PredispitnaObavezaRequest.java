package org.raflab.studsluzba.controllers.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class PredispitnaObavezaRequest {

    @NotNull
    private Long predmetId;

    @NotNull
    private Long skolskaGodinaId;

    @NotNull
    private String vrsta;

    @NotNull
    private Integer maxPoena;
}