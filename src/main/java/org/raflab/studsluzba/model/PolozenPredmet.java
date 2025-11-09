package org.raflab.studsluzba.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "polozen_predmet")
public class PolozenPredmet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "polozen_predmet_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "predmet_id", nullable = false)
    private Predmet predmet;

    @Column(name = "ocena", nullable = false)
    private Integer ocena; // 6-10

    @Column(name = "datum_polaganja", nullable = false)
    private LocalDate datumPolaganja;

    @OneToOne
    @JoinColumn(name = "izlazak_na_ispit_id")
    private IzlazakNaIspit izlazakNaIspit; // Ako je položen na ispitu

    @Column(name = "priznat", nullable = false)
    private Boolean priznat = false; // Da li je priznat sa druge ustanove

    @Column(name = "napomena")
    private String napomena;

    // Constructors
    public PolozenPredmet() {
    }

    public PolozenPredmet(Student student, Predmet predmet, Integer ocena, LocalDate datumPolaganja) {
        this.student = student;
        this.predmet = predmet;
        this.ocena = ocena;
        this.datumPolaganja = datumPolaganja;
        this.priznat = false;
    }

    // Constructor za priznati predmet
    public PolozenPredmet(Student student, Predmet predmet, Integer ocena, LocalDate datumPolaganja, Boolean priznat) {
        this.student = student;
        this.predmet = predmet;
        this.ocena = ocena;
        this.datumPolaganja = datumPolaganja;
        this.priznat = priznat;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Predmet getPredmet() {
        return predmet;
    }

    public void setPredmet(Predmet predmet) {
        this.predmet = predmet;
    }

    public Integer getOcena() {
        return ocena;
    }

    public void setOcena(Integer ocena) {
        this.ocena = ocena;
    }

    public LocalDate getDatumPolaganja() {
        return datumPolaganja;
    }

    public void setDatumPolaganja(LocalDate datumPolaganja) {
        this.datumPolaganja = datumPolaganja;
    }

    public IzlazakNaIspit getIzlazakNaIspit() {
        return izlazakNaIspit;
    }

    public void setIzlazakNaIspit(IzlazakNaIspit izlazakNaIspit) {
        this.izlazakNaIspit = izlazakNaIspit;
    }

    public Boolean getPriznat() {
        return priznat;
    }

    public void setPriznat(Boolean priznat) {
        this.priznat = priznat;
    }

    public String getNapomena() {
        return napomena;
    }

    public void setNapomena(String napomena) {
        this.napomena = napomena;
    }
}