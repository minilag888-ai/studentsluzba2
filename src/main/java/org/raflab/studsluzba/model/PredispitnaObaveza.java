package org.raflab.studsluzba.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "predispitna_obaveza")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PredispitnaObaveza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Predmet predmet;

    @ManyToOne
    private SkolskaGodina skolskaGodina;

    @Column(nullable = false)
    private String vrsta; // "test", "kolokvijum", "seminarski"...

    @Column(nullable = false)
    private Integer maxPoena;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PredispitnaObaveza that = (PredispitnaObaveza) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}