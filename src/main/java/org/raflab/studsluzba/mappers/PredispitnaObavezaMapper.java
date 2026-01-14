package org.raflab.studsluzba.mappers;

import org.raflab.studsluzba.controllers.request.PredispitnaObavezaRequest;
import org.raflab.studsluzba.controllers.response.PredispitnaObavezaResponse;
import org.raflab.studsluzba.model.PredispitnaObaveza;
import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.model.SkolskaGodina;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PredispitnaObavezaMapper {

    public PredispitnaObaveza toEntity(PredispitnaObavezaRequest request, Predmet predmet, SkolskaGodina godina) {
        if (request == null) return null;

        PredispitnaObaveza obaveza = new PredispitnaObaveza();
        obaveza.setPredmet(predmet);
        obaveza.setSkolskaGodina(godina);
        obaveza.setVrsta(request.getVrsta());
        obaveza.setMaxPoena(request.getMaxPoena());
        return obaveza;
    }

    public PredispitnaObavezaResponse toResponse(PredispitnaObaveza obaveza) {
        if (obaveza == null) return null;

        PredispitnaObavezaResponse response = new PredispitnaObavezaResponse();
        response.setId(obaveza.getId());
        response.setVrsta(obaveza.getVrsta());
        response.setMaxPoena(obaveza.getMaxPoena());

        if (obaveza.getPredmet() != null) {
            response.setPredmetId(obaveza.getPredmet().getId());
            response.setPredmetNaziv(obaveza.getPredmet().getNaziv());
        }

        if (obaveza.getSkolskaGodina() != null) {
            response.setSkolskaGodinaId(obaveza.getSkolskaGodina().getId());
            response.setSkolskaGodinaNaziv(obaveza.getSkolskaGodina().getNaziv());
        }

        return response;
    }

    public List<PredispitnaObavezaResponse> toResponseList(List<PredispitnaObaveza> obaveze) {
        if (obaveze == null) return null;
        return obaveze.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}