package org.raflab.studsluzba.client.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class NastavnikWebProfileDTO {
    private List<PredmetDTO> predmeti;
    private Map<Long, List<StudentProfileDTO>> slusajuPredmete;}