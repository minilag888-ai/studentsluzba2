package org.raflab.studsluzba.client.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DrziPredmetBezPoklapanjaInitResponse {

    private String fajlPredmetNazivBezPoklapanja;
    private String fajlNastavnikEmailBezPoklapanja;
}