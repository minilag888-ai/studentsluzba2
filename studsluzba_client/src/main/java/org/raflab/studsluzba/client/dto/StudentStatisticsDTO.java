package org.raflab.studsluzba.client.dto;

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