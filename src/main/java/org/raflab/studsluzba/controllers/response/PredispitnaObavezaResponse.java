package org.raflab.studsluzba.controllers.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PredispitnaObavezaResponse {

    private Long id;
    private Long predmetId;
    private String predmetNaziv;
    private Long skolskaGodinaId;
    private String skolskaGodinaNaziv;
    private String vrsta;
    private Integer maxPoena;
}