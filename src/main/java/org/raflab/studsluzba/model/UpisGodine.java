package org.raflab.studsluzba.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "upis_godine")
public class UpisGodine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "upis_godine_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "indeks_id", nullable = false)
    private Indeks indeks;

    @ManyToOne
    @JoinColumn(name = "skolska_godina_id", nullable = false)
    private SkolskaGodina skolskaGodina;

    @Column(name = "godina_studija", nullable = false)
    private Integer godinaStudija; // 1, 2, 3, 4...

    @Column(name = "datum_upisa", nullable = false)
    private LocalDate datumUpisa;

    @Column(name = "napomena")
    private String napomena;

    @ManyToMany
    @JoinTable(
            name = "upis_godine_predmet",
            joinColumns = @JoinColumn(name = "upis_godine_id"),
            inverseJoinColumns = @JoinColumn(name = "predmet_id")
    )
    private List<Predmet> predmeti = new ArrayList<>();

    // Constructors
    public UpisGodine() {
    }

    public UpisGodine(Indeks indeks, SkolskaGodina skolskaGodina, Integer godinaStudija, LocalDate datumUpisa) {
        this.indeks = indeks;
        this.skolskaGodina = skolskaGodina;
        this.godinaStudija = godinaStudija;
        this.datumUpisa = datumUpisa;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Indeks getIndeks() {
        return indeks;
    }

    public void setIndeks(Indeks indeks) {
        this.indeks = indeks;
    }

    public SkolskaGodina getSkolskaGodina() {
        return skolskaGodina;
    }

    public void setSkolskaGodina(SkolskaGodina skolskaGodina) {
        this.skolskaGodina = skolskaGodina;
    }

    public Integer getGodinaStudija() {
        return godinaStudija;
    }

    public void setGodinaStudija(Integer godinaStudija) {
        this.godinaStudija = godinaStudija;
    }

    public LocalDate getDatumUpisa() {
        return datumUpisa;
    }

    public void setDatumUpisa(LocalDate datumUpisa) {
        this.datumUpisa = datumUpisa;
    }

    public String getNapomena() {
        return napomena;
    }

    public void setNapomena(String napomena) {
        this.napomena = napomena;
    }

    public List<Predmet> getPredmeti() {
        return predmeti;
    }

    public void setPredmeti(List<Predmet> predmeti) {
        this.predmeti = predmeti;
    }

    public void addPredmet(Predmet predmet) {
        this.predmeti.add(predmet);
    }
}