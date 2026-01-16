package org.raflab.studsluzba.client.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DrziPredmetPoklapanjeInitResponse {
    private Long predmetId;
    private String predmetNaziv;
    private String fajlPredmetNaziv;
    private String fajlNastavnikEmail;
}