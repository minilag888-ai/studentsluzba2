package org.raflab.studsluzba.shared.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.raflab.studsluzba.shared.dtos.TeacherDTO;

@Data
@AllArgsConstructor
public class DrziPredmetDTO {

    private int id;
    private TeacherDTO teacher;
    private SubjectDTO subject;
    private String classType;
    private String sessionCount;
}