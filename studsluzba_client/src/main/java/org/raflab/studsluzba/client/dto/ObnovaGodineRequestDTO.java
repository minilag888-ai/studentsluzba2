package org.raflab.studsluzba.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ObnovaGodineRequestDTO {
    private Long skolskaGodinaId;
    private Integer godinaStudija;
    private LocalDate datumObnove;
    private String napomena;
    private List<Long> predmetIds; // Lista ID-jeva predmeta (max 60 ESPB)
}