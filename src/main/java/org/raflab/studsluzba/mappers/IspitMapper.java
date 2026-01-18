package org.raflab.studsluzba.mappers;

import org.raflab.studsluzba.controllers.response.IspitResponse;
import org.raflab.studsluzba.model.Ispit;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.stream.Collectors;

@Component
public class IspitMapper {

    public IspitResponse toResponse(Ispit ispit) {
        if (ispit == null) return null;

        IspitResponse response = new IspitResponse();
        response.setId(ispit.getId());

        if (ispit.getPredmet() != null) {
            response.setPredmetId(ispit.getPredmet().getId());
            response.setPredmetSifra(ispit.getPredmet().getSifra());
            response.setPredmetNaziv(ispit.getPredmet().getNaziv());
        }

        if (ispit.getIspitniRok() != null) {
            response.setIspitniRokId(ispit.getIspitniRok().getId());
            response.setIspitniRokNaziv(ispit.getIspitniRok().getNaziv());

            if (ispit.getIspitniRok().getSkolskaGodina() != null) {
                response.setSkolskaGodinaId(ispit.getIspitniRok().getSkolskaGodina().getId());
                response.setSkolskaGodinaNaziv(ispit.getIspitniRok().getSkolskaGodina().getNaziv());
            }
        }

        if (ispit.getDrziPredmet() != null && ispit.getDrziPredmet().getNastavnik() != null) {
            response.setNastavnikId(ispit.getDrziPredmet().getNastavnik().getId());
            response.setNastavnikIme(ispit.getDrziPredmet().getNastavnik().getIme());
            response.setNastavnikPrezime(ispit.getDrziPredmet().getNastavnik().getPrezime());
        }

        response.setDatumOdrzavanja(ispit.getDatumOdrzavanja());
        response.setVremePocetka(ispit.getVremePocetka());
        response.setZakljucen(ispit.getZakljucen());
        response.setNapomena(ispit.getNapomena());

        return response;
    }

    public List<IspitResponse> toResponseList(List<Ispit> ispiti) {
        if (ispiti == null) return null;
        return ispiti.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}