package org.raflab.studsluzba.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "indeks")
public class Indeks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "indeks_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "studijski_program_id", nullable = false)
    private StudijskiProgram studijskiProgram;

    @Column(name = "godina_upisa", nullable = false)
    private Integer godinaUpisa;

    @Column(name = "broj", nullable = false)
    private Integer broj;

    @Column(name = "aktivan", nullable = false)
    private Boolean aktivan = true;

    @Column(name = "datum_od")
    private LocalDate datumOd;

    // Constructors
    public Indeks() {
    }

    public Indeks(Student student, StudijskiProgram studijskiProgram, Integer godinaUpisa, Integer broj) {
        this.student = student;
        this.studijskiProgram = studijskiProgram;
        this.godinaUpisa = godinaUpisa;
        this.broj = broj;
        this.aktivan = true;
        this.datumOd = LocalDate.now();
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

    public StudijskiProgram getStudijskiProgram() {
        return studijskiProgram;
    }

    public void setStudijskiProgram(StudijskiProgram studijskiProgram) {
        this.studijskiProgram = studijskiProgram;
    }

    public Integer getGodinaUpisa() {
        return godinaUpisa;
    }

    public void setGodinaUpisa(Integer godinaUpisa) {
        this.godinaUpisa = godinaUpisa;
    }

    public Integer getBroj() {
        return broj;
    }

    public void setBroj(Integer broj) {
        this.broj = broj;
    }

    public Boolean getAktivan() {
        return aktivan;
    }

    public void setAktivan(Boolean aktivan) {
        this.aktivan = aktivan;
    }

    public LocalDate getDatumOd() {
        return datumOd;
    }

    public void setDatumOd(LocalDate datumOd) {
        this.datumOd = datumOd;
    }

    // Helper method - formatiraj indeks kao string (npr. "2021/123")
    public String getIndeksBroj() {
        return godinaUpisa + "/" + broj;
    }
}