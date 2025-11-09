package org.raflab.studsluzba.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "uplata")
public class Uplata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uplata_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "datum_uplate", nullable = false)
    private LocalDate datumUplate;

    @Column(name = "iznos_rsd", nullable = false)
    private Double iznosRsd;

    @Column(name = "srednji_kurs", nullable = false)
    private Double srednjiKurs;

    @Column(name = "iznos_eur")
    private Double iznosEur; // Izračunato: iznosRsd / srednjiKurs

    @Column(name = "napomena")
    private String napomena;

    // Constructors
    public Uplata() {
    }

    public Uplata(Student student, LocalDate datumUplate, Double iznosRsd, Double srednjiKurs) {
        this.student = student;
        this.datumUplate = datumUplate;
        this.iznosRsd = iznosRsd;
        this.srednjiKurs = srednjiKurs;
        this.iznosEur = iznosRsd / srednjiKurs;
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

    public LocalDate getDatumUplate() {
        return datumUplate;
    }

    public void setDatumUplate(LocalDate datumUplate) {
        this.datumUplate = datumUplate;
    }

    public Double getIznosRsd() {
        return iznosRsd;
    }

    public void setIznosRsd(Double iznosRsd) {
        this.iznosRsd = iznosRsd;
        if (this.srednjiKurs != null) {
            this.iznosEur = iznosRsd / this.srednjiKurs;
        }
    }

    public Double getSrednjiKurs() {
        return srednjiKurs;
    }

    public void setSrednjiKurs(Double srednjiKurs) {
        this.srednjiKurs = srednjiKurs;
        if (this.iznosRsd != null) {
            this.iznosEur = this.iznosRsd / srednjiKurs;
        }
    }

    public Double getIznosEur() {
        return iznosEur;
    }

    public void setIznosEur(Double iznosEur) {
        this.iznosEur = iznosEur;
    }

    public String getNapomena() {
        return napomena;
    }

    public void setNapomena(String napomena) {
        this.napomena = napomena;
    }
}