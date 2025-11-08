package org.raflab.studsluzba.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

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
    @JoinColumn(name = "student_indeks_id", nullable = false)
    private StudentIndeks studentIndeks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "predmet_id", nullable = false)
    private Predmet predmet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ispitni_rok_id", nullable = false)
    private IspitniRok ispitniRok;

    @Column(nullable = false)
    private Integer ocena;  // 5-10

    @Column(name = "datum_polaganja", nullable = false)
    private LocalDate datumPolaganja;

    @Column(length = 500)
    private String napomena;
}