package org.raflab.studsluzba.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreostaliIznosDTO {
    private Double preostaliIznosEur;
    private Double preostaliIznosRsd;
    private Double srednjiKurs;
    private Double skolarinaNaCenu; // 3000 EUR
}