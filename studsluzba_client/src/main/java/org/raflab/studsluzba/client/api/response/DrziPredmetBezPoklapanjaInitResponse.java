package org.raflab.studsluzba.client.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DrziPredmetBezPoklapanjaInitResponse {
    private String fajlPredmetNazivBezPoklapanja;
    private String fajlNastavnikEmailBezPoklapanja;
}