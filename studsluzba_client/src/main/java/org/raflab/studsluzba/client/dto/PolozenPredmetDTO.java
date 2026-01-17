package org.raflab.studsluzba.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

public class PolozenPredmetDTO {

    private Long id;

    @JsonProperty("sifraPredmeta")
    private String sifraPredmeta;

    @JsonProperty("nazivPredmeta")
    private String nazivPredmeta;

    @JsonProperty("espb")
    private Integer espb;

    @JsonProperty("ocena")
    private Integer ocena;

    @JsonProperty("datumPolaganja")
    private LocalDate datumPolaganja;

    @JsonProperty("tipPolaganja")
    private String tipPolaganja;

    // Constructors
    public PolozenPredmetDTO() {
    }

    public PolozenPredmetDTO(Long id, String sifraPredmeta, String nazivPredmeta, Integer espb,
                             Integer ocena, LocalDate datumPolaganja, String tipPolaganja) {
        this.id = id;
        this.sifraPredmeta = sifraPredmeta;
        this.nazivPredmeta = nazivPredmeta;
        this.espb = espb;
        this.ocena = ocena;
        this.datumPolaganja = datumPolaganja;
        this.tipPolaganja = tipPolaganja;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSifraPredmeta() {
        return sifraPredmeta;
    }

    public void setSifraPredmeta(String sifraPredmeta) {
        this.sifraPredmeta = sifraPredmeta;
    }

    public String getNazivPredmeta() {
        return nazivPredmeta;
    }

    public void setNazivPredmeta(String nazivPredmeta) {
        this.nazivPredmeta = nazivPredmeta;
    }

    public Integer getEspb() {
        return espb;
    }

    public void setEspb(Integer espb) {
        this.espb = espb;
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

    public String getTipPolaganja() {
        return tipPolaganja;
    }

    public void setTipPolaganja(String tipPolaganja) {
        this.tipPolaganja = tipPolaganja;
    }

    @Override
    public String toString() {
        return "PolozenPredmetDTO{" +
                "id=" + id +
                ", sifraPredmeta='" + sifraPredmeta + '\'' +
                ", nazivPredmeta='" + nazivPredmeta + '\'' +
                ", espb=" + espb +
                ", ocena=" + ocena +
                ", datumPolaganja=" + datumPolaganja +
                ", tipPolaganja='" + tipPolaganja + '\'' +
                '}';
    }
}