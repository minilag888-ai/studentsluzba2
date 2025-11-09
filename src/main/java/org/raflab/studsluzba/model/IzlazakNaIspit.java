package org.raflab.studsluzba.model;

import javax.persistence.*;

@Entity
@Table(name = "izlazak_na_ispit")
public class IzlazakNaIspit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "izlazak_na_ispit_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "prijava_ispita_id", nullable = false, unique = true)
    private PrijavaIspita prijavaIspita;

    @Column(name = "poeni_predispitne")
    private Integer poeniPredispitne; // Poeni sa predispitnih obaveza

    @Column(name = "poeni_ispit")
    private Integer poeniIspit; // Poeni sa ispita

    @Column(name = "ukupno_poeni")
    private Integer ukupnoPoeni; // Zbir predispitnih i ispitnih poena

    @Column(name = "ocena")
    private Integer ocena; // 5-10 (ako je položio)

    @Column(name = "napomena")
    private String napomena;

    @Column(name = "ponisteno", nullable = false)
    private Boolean ponisteno = false; // Da li student poništava ispit

    // Constructors
    public IzlazakNaIspit() {
    }

    public IzlazakNaIspit(PrijavaIspita prijavaIspita, Integer poeniPredispitne, Integer poeniIspit) {
        this.prijavaIspita = prijavaIspita;
        this.poeniPredispitne = poeniPredispitne;
        this.poeniIspit = poeniIspit;
        this.ukupnoPoeni = poeniPredispitne + poeniIspit;
        this.ocena = izracunajOcenu(this.ukupnoPoeni);
        this.ponisteno = false;
    }

    // Helper - izračunaj ocenu na osnovu poena
    private Integer izracunajOcenu(Integer ukupnoPoeni) {
        if (ukupnoPoeni < 51) return 5; // Pao
        if (ukupnoPoeni < 61) return 6;
        if (ukupnoPoeni < 71) return 7;
        if (ukupnoPoeni < 81) return 8;
        if (ukupnoPoeni < 91) return 9;
        return 10;
    }

    // Helper - da li je student položio (ocena >= 6)
    public boolean jePolozio() {
        return !ponisteno && ocena != null && ocena >= 6;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PrijavaIspita getPrijavaIspita() {
        return prijavaIspita;
    }

    public void setPrijavaIspita(PrijavaIspita prijavaIspita) {
        this.prijavaIspita = prijavaIspita;
    }

    public Integer getPoeniPredispitne() {
        return poeniPredispitne;
    }

    public void setPoeniPredispitne(Integer poeniPredispitne) {
        this.poeniPredispitne = poeniPredispitne;
        updateUkupnoPoeni();
    }

    public Integer getPoeniIspit() {
        return poeniIspit;
    }

    public void setPoeniIspit(Integer poeniIspit) {
        this.poeniIspit = poeniIspit;
        updateUkupnoPoeni();
    }

    public Integer getUkupnoPoeni() {
        return ukupnoPoeni;
    }

    public void setUkupnoPoeni(Integer ukupnoPoeni) {
        this.ukupnoPoeni = ukupnoPoeni;
    }

    public Integer getOcena() {
        return ocena;
    }

    public void setOcena(Integer ocena) {
        this.ocena = ocena;
    }

    public String getNapomena() {
        return napomena;
    }

    public void setNapomena(String napomena) {
        this.napomena = napomena;
    }

    public Boolean getPonisteno() {
        return ponisteno;
    }

    public void setPonisteno(Boolean ponisteno) {
        this.ponisteno = ponisteno;
    }

    // Helper - update ukupno i ocenu
    private void updateUkupnoPoeni() {
        if (poeniPredispitne != null && poeniIspit != null) {
            this.ukupnoPoeni = poeniPredispitne + poeniIspit;
            this.ocena = izracunajOcenu(this.ukupnoPoeni);
        }
    }
}