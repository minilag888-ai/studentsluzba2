package org.raflab.studsluzba.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NepolozenPredmetDTO {
    private Long id;
    private String sifraPredmeta;
    private String nazivPredmeta;
    private Integer espb;
    private String imeNastavnika;
    private String prezimeNastavnika;
    private Integer brojPokusaja; // Koliko puta je student izlazio na ispit
}