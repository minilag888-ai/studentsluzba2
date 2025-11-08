package org.raflab.studsluzba.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "ispitni_rok")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IspitniRok {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String naziv;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skolska_godina_id", nullable = false)
    private SkolskaGodina skolskaGodina;

    @Column(nullable = false)
    private LocalDate pocetak;

    @Column(nullable = false)
    private LocalDate kraj;

    @Column(nullable = false)
    private Boolean aktivan = false;
}