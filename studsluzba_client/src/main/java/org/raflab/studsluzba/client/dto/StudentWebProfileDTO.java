package org.raflab.studsluzba.client.dto;

import lombok.Data;
import java.util.List;

@Data
public class StudentWebProfileDTO {
    // Umesto celog objekta, samo ID
    private Long aktivanIndeksId;
    private Integer brojIndeksa;
    private Integer godinaIndeksa;
    private String studProgramOznaka;

    // Lista ID-jeva predmeta koje slusa
    private List<Long> slusaPredmeteIds;
}