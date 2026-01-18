package org.raflab.studsluzba.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
@Data
@NoArgsConstructor
@Entity
@Table(name = "polozen_predmet")
public class PolozenPredmet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "polozen_predmet_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_indeks_id", nullable = false)
    private StudentIndeks studentIndeks;  // PROMENJENO SA Student NA StudentIndeks!

    @ManyToOne
    @JoinColumn(name = "predmet_id", nullable = false)
    private Predmet predmet;

    @Column(name = "ocena", nullable = false)
    private Integer ocena;

    @Column(name = "datum_polaganja", nullable = false)
    private LocalDate datumPolaganja;

    @OneToOne
    @JoinColumn(name = "izlazak_na_ispit_id")
    private IzlazakNaIspit izlazakNaIspit;

    @Column(name = "priznat", nullable = false)
    private Boolean priznat = false;

    @Column(name = "napomena")
    private String napomena;



    public PolozenPredmet(StudentIndeks studentIndeks, Predmet predmet, Integer ocena, LocalDate datumPolaganja) {
        this.studentIndeks = studentIndeks;
        this.predmet = predmet;
        this.ocena = ocena;
        this.datumPolaganja = datumPolaganja;
        this.priznat = false;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StudentIndeks getStudentIndeks() {
        return studentIndeks;
    }

    public void setStudentIndeks(StudentIndeks studentIndeks) {
        this.studentIndeks = studentIndeks;
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