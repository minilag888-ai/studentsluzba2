package org.raflab.studsluzba.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.raflab.studsluzba.model.dtos.PredmetDTO;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ObnovaGodineDTO {
    private Long id;
    private Integer godinaStudija;
    private LocalDate datumObnove;
    private String napomena;
    private String skolskaGodina;
    private List<PredmetDTO> predmeti;
    private Integer ukupnoESPB;
}