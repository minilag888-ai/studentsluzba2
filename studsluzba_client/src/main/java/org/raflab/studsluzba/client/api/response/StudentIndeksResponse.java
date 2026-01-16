package org.raflab.studsluzba.client.api.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentIndeksResponse {
    private Long id;
    private int broj;
    private int godina;
    private String studProgramOznaka;
    private String nacinFinansiranja;
    private boolean aktivan;
    private LocalDate vaziOd;
    private String student;
    private String studijskiProgram;   // na koji studijski program je upisan
    private Integer ostvarenoEspb;
}
