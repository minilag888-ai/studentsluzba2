package org.raflab.studsluzba.mappers;

import org.raflab.studsluzba.controllers.request.IspitniRokRequest;
import org.raflab.studsluzba.controllers.response.IspitniRokResponse;
import org.raflab.studsluzba.model.IspitniRok;
import org.raflab.studsluzba.model.SkolskaGodina;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class IspitniRokMapper {

    public IspitniRok toEntity(IspitniRokRequest request, SkolskaGodina godina) {
        if (request == null) return null;

        IspitniRok rok = new IspitniRok();
        rok.setNaziv(request.getNaziv());
        rok.setSkolskaGodina(godina);
        rok.setPocetak(request.getPocetak());
        rok.setKraj(request.getKraj());
        rok.setAktivan(request.getAktivan() != null ? request.getAktivan() : false);
        return rok;
    }

    public IspitniRokResponse toResponse(IspitniRok rok) {
        if (rok == null) return null;

        IspitniRokResponse response = new IspitniRokResponse();
        response.setId(rok.getId());
        response.setNaziv(rok.getNaziv());
        response.setPocetak(rok.getPocetak());
        response.setKraj(rok.getKraj());
        response.setAktivan(rok.getAktivan());

        if (rok.getSkolskaGodina() != null) {
            response.setSkolskaGodinaId(rok.getSkolskaGodina().getId());
            response.setSkolskaGodinaNaziv(rok.getSkolskaGodina().getNaziv());
        }

        return response;
    }

    public List<IspitniRokResponse> toResponseList(List<IspitniRok> rokovi) {
        if (rokovi == null) return null;
        return rokovi.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}