package org.raflab.studsluzba.client.api.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class NastavnikResponse {

    @NonNull
    private Long id;
    @NonNull
    private String ime;
    private String prezime;
    private String srednjeIme;
    private String email;
    private String brojTelefona;
    private String adresa;
    //private Set<NastavnikZvanje> zvanja;

    private LocalDate datumRodjenja;
    private Character pol;
    private String jmbg;
}
