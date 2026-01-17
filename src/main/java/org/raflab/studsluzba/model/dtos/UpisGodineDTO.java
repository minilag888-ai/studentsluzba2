package org.raflab.studsluzba.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpisGodineDTO {
    private Long id;
    private Integer godinaStudija;
    private LocalDate datumUpisa;
    private String napomena;
    private String skolskaGodina;
    private List<PredmetDTO> predmeti;
    private Integer ukupnoESPB;
}