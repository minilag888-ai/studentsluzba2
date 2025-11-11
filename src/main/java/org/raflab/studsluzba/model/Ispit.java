package org.raflab.studsluzba.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ispit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ispit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "predmet_id", nullable = false)
    private Predmet predmet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ispitni_rok_id", nullable = false)
    private IspitniRok ispitniRok;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drzi_predmet_id", nullable = false)
    private DrziPredmet drziPredmet;  // Nastavnik koji drži ispit

    @Column(name = "datum_odrzavanja", nullable = false)
    private LocalDate datumOdrzavanja;

    @Column(name = "vreme_pocetka")
    private LocalDateTime vremePocetka;

    @Column(name = "zakljucen", nullable = false)
    private Boolean zakljucen = false;

    @Column(length = 500)
    private String napomena;
}