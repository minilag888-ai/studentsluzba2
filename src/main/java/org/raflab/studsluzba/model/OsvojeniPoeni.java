package org.raflab.studsluzba.model;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;
@Data
@Entity

@Table(name = "osvojeni_poeni")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OsvojeniPoeni {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private StudentIndeks studentIndeks;

    @ManyToOne
    private PredispitnaObaveza predispitnaObaveza;

    @Column(nullable = false)
    private Integer poeni;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OsvojeniPoeni that = (OsvojeniPoeni) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}