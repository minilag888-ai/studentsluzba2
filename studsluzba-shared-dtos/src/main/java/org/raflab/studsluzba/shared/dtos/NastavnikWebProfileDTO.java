package org.raflab.studsluzba.shared.dtos;

import lombok.Data;
import org.raflab.studsluzba.shared.dtos.PredmetDTO;
import org.raflab.studsluzba.shared.dtos.StudentProfileDTO;

import java.util.List;
import java.util.Map;

@Data
public class NastavnikWebProfileDTO {
    private List<PredmetDTO> predmeti;
    private Map<Long, List<StudentProfileDTO>> slusajuPredmete;}