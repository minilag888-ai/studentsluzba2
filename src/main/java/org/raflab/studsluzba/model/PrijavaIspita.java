package org.raflab.studsluzba.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "prijava_ispita")
public class PrijavaIspita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prijava_ispita_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "ispit_id", nullable = false)
    private Ispit ispit;

    @Column(name = "datum_prijave", nullable = false)
    private LocalDate datumPrijave;

    @Column(name = "izasao", nullable = false)
    private Boolean izasao = false; // Da li je student stvarno izašao na ispit

    // Constructors
    public PrijavaIspita() {
    }

    public PrijavaIspita(Student student, Ispit ispit, LocalDate datumPrijave) {
        this.student = student;
        this.ispit = ispit;
        this.datumPrijave = datumPrijave;
        this.izasao = false;
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

    public Ispit getIspit() {
        return ispit;
    }

    public void setIspit(Ispit ispit) {
        this.ispit = ispit;
    }

    public LocalDate getDatumPrijave() {
        return datumPrijave;
    }

    public void setDatumPrijave(LocalDate datumPrijave) {
        this.datumPrijave = datumPrijave;
    }

    public Boolean getIzasao() {
        return izasao;
    }

    public void setIzasao(Boolean izasao) {
        this.izasao = izasao;
    }
}