package org.raflab.studsluzba.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DrziPredmetDTO {

    private int id;
    private TeacherDTO teacher;
    private SubjectDTO subject;
    private String classType;
    private String sessionCount;
}