package org.raflab.studsluzba.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentPodaciDTO {
    private Long id;
    private String ime;
    private String prezime;
    private String srednjeIme;
    private String jmbg;
    private LocalDate datumRodjenja;
    private String mestoRodjenja;
    private String drzavaRodjenja;
    private String drzavljanstvo;
    private String pol;
    private String email;
    private String brojTelefona;
    private String srednjaSkolaNaziv;
}