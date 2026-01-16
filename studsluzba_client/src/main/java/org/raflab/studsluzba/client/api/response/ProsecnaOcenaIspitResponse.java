package org.raflab.studsluzba.client.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProsecnaOcenaIspitResponse {
    private Long ispitId;
    private String nazivPredmeta;
    private String nazivRoka;
    private Double prosecnaOcena;
    private Long brojPolaganja;
    private Long brojPolozenih;
}