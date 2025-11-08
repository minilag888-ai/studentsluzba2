package org.raflab.studsluzba.controllers.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OsvojeniPoeniResponse {

    private Long id;
    private Long studentIndeksId;
    private String studentIme;
    private String studentPrezime;
    private Long predispitnaObavezaId;
    private String obavezaVrsta;
    private Integer maxPoena;
    private Integer poeni;
}