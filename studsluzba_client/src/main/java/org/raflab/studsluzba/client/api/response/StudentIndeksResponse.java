package org.raflab.studsluzba.client.api.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentIndeksResponse {
    private Long id;
    private Integer broj;
    private Integer godina;
    private String studProgramOznaka;
    private String nacinFinansiranja;
    private boolean aktivan;
    private LocalDate vaziOd;

    
    private String studentIme;
    private String studentPrezime;
    private String studentEmail;
    private String studijskiProgramNaziv;

    private Integer ostvarenoEspb;
}