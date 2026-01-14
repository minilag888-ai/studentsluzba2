package org.raflab.studsluzba.mappers;

import org.raflab.studsluzba.controllers.request.SkolskaGodinaRequest;
import org.raflab.studsluzba.controllers.response.SkolskaGodinaResponse;
import org.raflab.studsluzba.model.SkolskaGodina;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SkolskaGodinaMapper {

    public SkolskaGodina toEntity(SkolskaGodinaRequest request) {
        if (request == null) return null;

        SkolskaGodina godina = new SkolskaGodina();
        godina.setNaziv(request.getNaziv());
        godina.setPocetakZimskog(request.getPocetakZimskog());
        godina.setKrajZimskog(request.getKrajZimskog());
        godina.setPocetakLetnjeg(request.getPocetakLetnjeg());
        godina.setKrajLetnjeg(request.getKrajLetnjeg());
        godina.setAktivna(request.getAktivna() != null ? request.getAktivna() : false);
        return godina;
    }

    public SkolskaGodinaResponse toResponse(SkolskaGodina godina) {
        if (godina == null) return null;

        SkolskaGodinaResponse response = new SkolskaGodinaResponse();
        response.setId(godina.getId());
        response.setNaziv(godina.getNaziv());
        response.setPocetakZimskog(godina.getPocetakZimskog());
        response.setKrajZimskog(godina.getKrajZimskog());
        response.setPocetakLetnjeg(godina.getPocetakLetnjeg());
        response.setKrajLetnjeg(godina.getKrajLetnjeg());
        response.setAktivna(godina.getAktivna());
        return response;
    }

    public List<SkolskaGodinaResponse> toResponseList(List<SkolskaGodina> godine) {
        if (godine == null) return null;
        return godine.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}