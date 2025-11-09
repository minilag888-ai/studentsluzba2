package org.raflab.studsluzba.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolozenPredmetDTO {
    private Long id;
    private String sifraPredmeta;
    private String nazivPredmeta;
    private Integer espb;
    private Integer ocena;
    private LocalDate datumPolaganja;
    private String tipPolaganja; // "Ispit" ili "Priznat"
}