package org.raflab.studsluzba.client.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IspitniRokResponse {
    private Long id;
    private String naziv;
    private Long skolskaGodinaId;
    private String skolskaGodinaNaziv;
    private LocalDate pocetak;
    private LocalDate kraj;
    private Boolean aktivan;
}