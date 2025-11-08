package org.raflab.studsluzba.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "zvanja")  // ← Isključi iz toString!
public class Nastavnik {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String ime;
    private String prezime;
    private String srednjeIme;
    private String email;
    private String brojTelefona;
    private String adresa;

    @OneToMany(mappedBy = "nastavnik", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<NastavnikZvanje> zvanja = new HashSet<>();

    private LocalDate datumRodjenja;
    private Character pol;
    private String jmbg;

    // ✅ Custom hashCode i equals BEZ "zvanja" polja!
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nastavnik nastavnik = (Nastavnik) o;
        return Objects.equals(id, nastavnik.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}