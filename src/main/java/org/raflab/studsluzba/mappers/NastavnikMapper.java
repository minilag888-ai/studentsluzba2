package org.raflab.studsluzba.mappers;

import org.raflab.studsluzba.controllers.request.NastavnikRequest;
import org.raflab.studsluzba.controllers.response.NastavnikResponse;
import org.raflab.studsluzba.model.Nastavnik;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class NastavnikMapper {

    public Nastavnik toEntity(NastavnikRequest request) {
        if (request == null) return null;

        Nastavnik nastavnik = new Nastavnik();
        nastavnik.setIme(request.getIme());
        nastavnik.setPrezime(request.getPrezime());
        nastavnik.setSrednjeIme(request.getSrednjeIme());
        nastavnik.setEmail(request.getEmail());
        nastavnik.setBrojTelefona(request.getBrojTelefona());
        nastavnik.setAdresa(request.getAdresa());
        nastavnik.setDatumRodjenja(request.getDatumRodjenja());
        nastavnik.setPol(request.getPol());
        nastavnik.setJmbg(request.getJmbg());
        return nastavnik;
    }

    public NastavnikResponse toResponse(Nastavnik nastavnik) {
        if (nastavnik == null) return null;

        NastavnikResponse response = new NastavnikResponse();
        response.setId(nastavnik.getId());
        response.setIme(nastavnik.getIme());
        response.setPrezime(nastavnik.getPrezime());
        response.setSrednjeIme(nastavnik.getSrednjeIme());
        response.setEmail(nastavnik.getEmail());
        response.setBrojTelefona(nastavnik.getBrojTelefona());
        response.setAdresa(nastavnik.getAdresa());
        response.setDatumRodjenja(nastavnik.getDatumRodjenja());
        response.setPol(nastavnik.getPol());
        response.setJmbg(nastavnik.getJmbg());
        return response;
    }

    public List<NastavnikResponse> toResponseList(Iterable<Nastavnik> nastavnici) {
        if (nastavnici == null) return null;
        List<NastavnikResponse> responses = new java.util.ArrayList<>();
        nastavnici.forEach(n -> responses.add(toResponse(n)));
        return responses;
    }
}