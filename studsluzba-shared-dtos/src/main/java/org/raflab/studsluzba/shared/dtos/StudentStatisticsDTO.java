package org.raflab.studsluzba.shared.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentStatisticsDTO {
    private Integer ukupnoESPB;
    private Double prosecnaOcena;
    private Integer brojPolozenihPredmeta;
    private Integer brojNepolozenihPredmeta;
}