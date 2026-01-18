package org.raflab.studsluzba.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;
@Data
@Entity
@Table(name = "skolska_godina")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SkolskaGodina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String naziv;

    @Column(name = "pocetak_zimskog", nullable = false)
    private LocalDate pocetakZimskog;

    @Column(name = "kraj_zimskog", nullable = false)
    private LocalDate krajZimskog;

    @Column(name = "pocetak_letnjeg", nullable = false)
    private LocalDate pocetakLetnjeg;

    @Column(name = "kraj_letnjeg", nullable = false)
    private LocalDate krajLetnjeg;

    @Column(nullable = false)
    private Boolean aktivna = false;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SkolskaGodina that = (SkolskaGodina) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}