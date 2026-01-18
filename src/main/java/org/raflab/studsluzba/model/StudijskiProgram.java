package org.raflab.studsluzba.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@Entity
@Table(name = "studijski_program")
public class StudijskiProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "studijski_program_id")
    private Long id;

    @Column(name = "oznaka", nullable = false, unique = true, length = 10)
    private String oznaka; // RI, RN, SI, SN...

    @Column(name = "naziv", nullable = false)
    private String naziv;

    @Column(name = "godina_akreditacije", nullable = false)
    private Integer godinaAkreditacije;

    @Column(name = "naziv_zvanja")
    private String nazivZvanja; // Bachelor, Master...

    @Column(name = "trajanje_semestara", nullable = false)
    private Integer trajanjeSemestara; // 8, 4, 6...

    @ManyToOne
    @JoinColumn(name = "vrsta_studija_id")
    private VrstaStudija vrstaStudija; // SI, RI...

    @OneToMany(mappedBy = "studijskiProgram")
    private List<Predmet> predmeti = new ArrayList<>();

    // Constructors
    public StudijskiProgram() {
    }

    public StudijskiProgram(String oznaka, String naziv, Integer godinaAkreditacije, Integer trajanjeSemestara) {
        this.oznaka = oznaka;
        this.naziv = naziv;
        this.godinaAkreditacije = godinaAkreditacije;
        this.trajanjeSemestara = trajanjeSemestara;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOznaka() {
        return oznaka;
    }

    public void setOznaka(String oznaka) {
        this.oznaka = oznaka;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public Integer getGodinaAkreditacije() {
        return godinaAkreditacije;
    }

    public void setGodinaAkreditacije(Integer godinaAkreditacije) {
        this.godinaAkreditacije = godinaAkreditacije;
    }

    public String getNazivZvanja() {
        return nazivZvanja;
    }

    public void setNazivZvanja(String nazivZvanja) {
        this.nazivZvanja = nazivZvanja;
    }

    public Integer getTrajanjeSemestara() {
        return trajanjeSemestara;
    }

    public void setTrajanjeSemestara(Integer trajanjeSemestara) {
        this.trajanjeSemestara = trajanjeSemestara;
    }

    public VrstaStudija getVrstaStudija() {
        return vrstaStudija;
    }

    public void setVrstaStudija(VrstaStudija vrstaStudija) {
        this.vrstaStudija = vrstaStudija;
    }

    public List<Predmet> getPredmeti() {
        return predmeti;
    }

    public void setPredmeti(List<Predmet> predmeti) {
        this.predmeti = predmeti;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudijskiProgram that = (StudijskiProgram) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}