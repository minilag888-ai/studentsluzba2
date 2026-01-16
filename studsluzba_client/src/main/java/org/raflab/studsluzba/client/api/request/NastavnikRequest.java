package org.raflab.studsluzba.client.api.request;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.Set;

@Data
public class NastavnikRequest {

    @NonNull
    private String ime;
    @NonNull
    private String prezime;
    @NonNull
    private String srednjeIme;
    @NonNull
    private String email;
    private String brojTelefona;
    private String adresa;
    private Set<String> zvanja;

    private LocalDate datumRodjenja;
    private Character pol;
    private String jmbg;
}
