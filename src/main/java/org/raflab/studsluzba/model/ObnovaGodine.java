package org.raflab.studsluzba.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "obnova_godine")
public class ObnovaGodine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "obnova_godine_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_indeks_id", nullable = false)
    private StudentIndeks studentIndeks;  // PROMENJENO SA Indeks NA StudentIndeks

    @ManyToOne
    @JoinColumn(name = "skolska_godina_id", nullable = false)
    private SkolskaGodina skolskaGodina;

    @Column(name = "godina_studija", nullable = false)
    private Integer godinaStudija;

    @Column(name = "datum_obnove", nullable = false)
    private LocalDate datumObnove;

    @Column(name = "napomena")
    private String napomena;

    @ManyToMany
    @JoinTable(
            name = "obnova_godine_predmet",
            joinColumns = @JoinColumn(name = "obnova_godine_id"),
            inverseJoinColumns = @JoinColumn(name = "predmet_id")
    )
    private List<Predmet> predmeti = new ArrayList<>();

    // Constructors
    public ObnovaGodine() {
    }

    public ObnovaGodine(StudentIndeks studentIndeks, SkolskaGodina skolskaGodina, Integer godinaStudija, LocalDate datumObnove) {
        this.studentIndeks = studentIndeks;
        this.skolskaGodina = skolskaGodina;
        this.godinaStudija = godinaStudija;
        this.datumObnove = datumObnove;
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

    public LocalDate getDatumObnove() {
        return datumObnove;
    }

    public void setDatumObnove(LocalDate datumObnove) {
        this.datumObnove = datumObnove;
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

    public int getUkupnoESPB() {
        return predmeti.stream()
                .mapToInt(Predmet::getEspb)
                .sum();
    }
}