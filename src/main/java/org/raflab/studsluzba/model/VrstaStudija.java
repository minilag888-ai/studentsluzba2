package org.raflab.studsluzba.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
@Data
@Entity
@AllArgsConstructor
@Table(name = "vrsta_studija")
public class VrstaStudija {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vrsta_studija_id")
    private Long id;

    @Column(name = "skraceni_naziv", nullable = false, unique = true, length = 10)
    private String skraceniNaziv; // OAS, MAS, DAS, SSS...

    @Column(name = "pun_naziv", nullable = false)
    private String punNaziv; // Osnovne akademske studije, Master akademske studije...

    // Constructors
    public VrstaStudija() {
    }

    public VrstaStudija(String skraceniNaziv, String punNaziv) {
        this.skraceniNaziv = skraceniNaziv;
        this.punNaziv = punNaziv;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSkraceniNaziv() {
        return skraceniNaziv;
    }

    public void setSkraceniNaziv(String skraceniNaziv) {
        this.skraceniNaziv = skraceniNaziv;
    }

    public String getPunNaziv() {
        return punNaziv;
    }

    public void setPunNaziv(String punNaziv) {
        this.punNaziv = punNaziv;
    }
}