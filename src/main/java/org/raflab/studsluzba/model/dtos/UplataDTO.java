package org.raflab.studsluzba.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UplataDTO {
    private Long id;
    private LocalDate datumUplate;
    private Double iznosEur;
    private Double srednjiKurs;
    private Double iznosRsd;
}