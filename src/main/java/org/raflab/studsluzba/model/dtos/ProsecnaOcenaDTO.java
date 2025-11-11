package org.raflab.studsluzba.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProsecnaOcenaDTO {
    private Long predmetId;
    private String sifraPredmeta;
    private String nazivPredmeta;
    private Integer odGodine;
    private Integer doGodine;
    private Double prosecnaOcena;
    private Long brojPolaganja;
}