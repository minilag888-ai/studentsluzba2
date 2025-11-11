package org.raflab.studsluzba.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "prijava_ispita")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrijavaIspita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_indeks_id", nullable = false)
    private StudentIndeks studentIndeks;

    @ManyToOne
    @JoinColumn(name = "ispit_id", nullable = false)
    private Ispit ispit;

    @Column(name = "datum_prijave", nullable = false)
    private LocalDate datumPrijave;

    @Column(name = "izasao", nullable = false)
    private Boolean izasao = false;
}