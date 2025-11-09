package org.raflab.studsluzba.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long id;

    @Column(name = "ime", nullable = false)
    private String ime;

    @Column(name = "prezime", nullable = false)
    private String prezime;

    @Column(name = "srednje_ime")
    private String srednjeIme;

    @Column(name = "jmbg", unique = true, nullable = false, length = 13)
    private String jmbg;

    @Column(name = "datum_rodjenja", nullable = false)
    private LocalDate datumRodjenja;

    @Column(name = "mesto_rodjenja")
    private String mestoRodjenja;

    @Column(name = "drzava_rodjenja")
    private String drzavaRodjenja;

    @Column(name = "drzavljanstvo")
    private String drzavljanstvo;

    @Column(name = "nacionalnost")
    private String nacionalnost;

    @Column(name = "pol")
    private String pol;

    @Column(name = "adresa_mesto")
    private String adresaMesto;

    @Column(name = "adresa_ulica")
    private String adresaUlica;

    @Column(name = "adresa_broj")
    private String adresaBroj;

    @Column(name = "broj_telefona")
    private String brojTelefona;

    @Column(name = "email_fakultetski")
    private String emailFakultetski;

    @Column(name = "email_privatni")
    private String emailPrivatni;

    @ManyToOne
    @JoinColumn(name = "srednja_skola_id")
    private SrednjaSkola srednjaSkola;

    @Column(name = "broj_licne_karte")
    private String brojLicneKarte;

    @Column(name = "izdavalac_lk")
    private String izdavalacLk;

    @Column(name = "uspeh_srednja")
    private Double uspehSrednja;

    @Column(name = "uspeh_prijemni")
    private Double uspehPrijemni;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Indeks> indeksi = new ArrayList<>();

    // Constructors
    public Student() {
    }

    public Student(String ime, String prezime, String jmbg, LocalDate datumRodjenja) {
        this.ime = ime;
        this.prezime = prezime;
        this.jmbg = jmbg;
        this.datumRodjenja = datumRodjenja;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getSrednjeIme() {
        return srednjeIme;
    }

    public void setSrednjeIme(String srednjeIme) {
        this.srednjeIme = srednjeIme;
    }

    public String getJmbg() {
        return jmbg;
    }

    public void setJmbg(String jmbg) {
        this.jmbg = jmbg;
    }

    public LocalDate getDatumRodjenja() {
        return datumRodjenja;
    }

    public void setDatumRodjenja(LocalDate datumRodjenja) {
        this.datumRodjenja = datumRodjenja;
    }

    public String getMestoRodjenja() {
        return mestoRodjenja;
    }

    public void setMestoRodjenja(String mestoRodjenja) {
        this.mestoRodjenja = mestoRodjenja;
    }

    public String getDrzavaRodjenja() {
        return drzavaRodjenja;
    }

    public void setDrzavaRodjenja(String drzavaRodjenja) {
        this.drzavaRodjenja = drzavaRodjenja;
    }

    public String getDrzavljanstvo() {
        return drzavljanstvo;
    }

    public void setDrzavljanstvo(String drzavljanstvo) {
        this.drzavljanstvo = drzavljanstvo;
    }

    public String getNacionalnost() {
        return nacionalnost;
    }

    public void setNacionalnost(String nacionalnost) {
        this.nacionalnost = nacionalnost;
    }

    public String getPol() {
        return pol;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }

    public String getAdresaMesto() {
        return adresaMesto;
    }

    public void setAdresaMesto(String adresaMesto) {
        this.adresaMesto = adresaMesto;
    }

    public String getAdresaUlica() {
        return adresaUlica;
    }

    public void setAdresaUlica(String adresaUlica) {
        this.adresaUlica = adresaUlica;
    }

    public String getAdresaBroj() {
        return adresaBroj;
    }

    public void setAdresaBroj(String adresaBroj) {
        this.adresaBroj = adresaBroj;
    }

    public String getBrojTelefona() {
        return brojTelefona;
    }

    public void setBrojTelefona(String brojTelefona) {
        this.brojTelefona = brojTelefona;
    }

    public String getEmailFakultetski() {
        return emailFakultetski;
    }

    public void setEmailFakultetski(String emailFakultetski) {
        this.emailFakultetski = emailFakultetski;
    }

    public String getEmailPrivatni() {
        return emailPrivatni;
    }

    public void setEmailPrivatni(String emailPrivatni) {
        this.emailPrivatni = emailPrivatni;
    }

    public String getBrojLicneKarte() {
        return brojLicneKarte;
    }

    public void setBrojLicneKarte(String brojLicneKarte) {
        this.brojLicneKarte = brojLicneKarte;
    }

    public String getIzdavalacLk() {
        return izdavalacLk;
    }

    public void setIzdavalacLk(String izdavalacLk) {
        this.izdavalacLk = izdavalacLk;
    }

    public Double getUspehSrednja() {
        return uspehSrednja;
    }

    public void setUspehSrednja(Double uspehSrednja) {
        this.uspehSrednja = uspehSrednja;
    }

    public Double getUspehPrijemni() {
        return uspehPrijemni;
    }

    public void setUspehPrijemni(Double uspehPrijemni) {
        this.uspehPrijemni = uspehPrijemni;
    }

    public List<Indeks> getIndeksi() {
        return indeksi;
    }

    public void setIndeksi(List<Indeks> indeksi) {
        this.indeksi = indeksi;
    }
    public SrednjaSkola getSrednjaSkola() {
        return srednjaSkola;
    }

    public void setSrednjaSkola(SrednjaSkola srednjaSkola) {
        this.srednjaSkola = srednjaSkola;
    }
}
