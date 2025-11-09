package org.raflab.studsluzba.model;

import javax.persistence.*;

@Entity
@Table(name = "srednja_skola")
public class SrednjaSkola {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "srednja_skola_id")
    private Long id;

    @Column(name = "naziv", nullable = false)
    private String naziv;

    @Column(name = "mesto", nullable = false)
    private String mesto;

    @Column(name = "vrsta", nullable = false)
    private String vrsta; // Gimnazija, Srednja stručna škola, Tehnička škola...

    // Constructors
    public SrednjaSkola() {
    }

    public SrednjaSkola(String naziv, String mesto, String vrsta) {
        this.naziv = naziv;
        this.mesto = mesto;
        this.vrsta = vrsta;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getMesto() {
        return mesto;
    }

    public void setMesto(String mesto) {
        this.mesto = mesto;
    }

    public String getVrsta() {
        return vrsta;
    }

    public void setVrsta(String vrsta) {
        this.vrsta = vrsta;
    }
}