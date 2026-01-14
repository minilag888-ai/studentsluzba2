package org.raflab.studsluzba.mappers;

import org.raflab.studsluzba.controllers.request.OsvojeniPoeniRequest;
import org.raflab.studsluzba.controllers.response.OsvojeniPoeniResponse;
import org.raflab.studsluzba.model.OsvojeniPoeni;
import org.raflab.studsluzba.model.PredispitnaObaveza;
import org.raflab.studsluzba.model.StudentIndeks;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OsvojeniPoeniMapper {

    public OsvojeniPoeni toEntity(OsvojeniPoeniRequest request, StudentIndeks indeks, PredispitnaObaveza obaveza) {
        if (request == null) return null;

        OsvojeniPoeni poeni = new OsvojeniPoeni();
        poeni.setStudentIndeks(indeks);
        poeni.setPredispitnaObaveza(obaveza);
        poeni.setPoeni(request.getPoeni());
        return poeni;
    }

    public OsvojeniPoeniResponse toResponse(OsvojeniPoeni osvojeniPoeni) {
        if (osvojeniPoeni == null) return null;

        OsvojeniPoeniResponse response = new OsvojeniPoeniResponse();
        response.setId(osvojeniPoeni.getId());
        response.setPoeni(osvojeniPoeni.getPoeni());

        if (osvojeniPoeni.getStudentIndeks() != null) {
            response.setStudentIndeksId(osvojeniPoeni.getStudentIndeks().getId());
            response.setStudentBrojIndeksa(osvojeniPoeni.getStudentIndeks().getBroj());
            response.setStudentGodinaIndeksa(osvojeniPoeni.getStudentIndeks().getGodina());

            if (osvojeniPoeni.getStudentIndeks().getStudent() != null) {
                response.setStudentIme(osvojeniPoeni.getStudentIndeks().getStudent().getIme());
                response.setStudentPrezime(osvojeniPoeni.getStudentIndeks().getStudent().getPrezime());
            }
        }

        if (osvojeniPoeni.getPredispitnaObaveza() != null) {
            response.setPredispitnaObavezaId(osvojeniPoeni.getPredispitnaObaveza().getId());
            response.setObavezaVrsta(osvojeniPoeni.getPredispitnaObaveza().getVrsta());
            response.setMaxPoena(osvojeniPoeni.getPredispitnaObaveza().getMaxPoena());

            if (osvojeniPoeni.getPredispitnaObaveza().getPredmet() != null) {
                response.setPredmetId(osvojeniPoeni.getPredispitnaObaveza().getPredmet().getId());
                response.setPredmetSifra(osvojeniPoeni.getPredispitnaObaveza().getPredmet().getSifra());
                response.setPredmetNaziv(osvojeniPoeni.getPredispitnaObaveza().getPredmet().getNaziv());
            }

            if (osvojeniPoeni.getPredispitnaObaveza().getSkolskaGodina() != null) {
                response.setSkolskaGodinaId(osvojeniPoeni.getPredispitnaObaveza().getSkolskaGodina().getId());
                response.setSkolskaGodinaNaziv(osvojeniPoeni.getPredispitnaObaveza().getSkolskaGodina().getNaziv());
            }
        }

        return response;
    }

    public List<OsvojeniPoeniResponse> toResponseList(List<OsvojeniPoeni> poeni) {
        if (poeni == null) return null;
        return poeni.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}