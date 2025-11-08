package org.raflab.studsluzba.utils;

import org.raflab.studsluzba.controllers.request.*;
import org.raflab.studsluzba.controllers.response.*;
import org.raflab.studsluzba.model.*;
import org.raflab.studsluzba.controllers.request.SkolskaGodinaRequest;
import org.raflab.studsluzba.controllers.response.SkolskaGodinaResponse;
import org.raflab.studsluzba.model.SkolskaGodina;
import org.raflab.studsluzba.controllers.request.PredispitnaObavezaRequest;
import org.raflab.studsluzba.controllers.response.PredispitnaObavezaResponse;
import org.raflab.studsluzba.model.PredispitnaObaveza;
import org.raflab.studsluzba.controllers.request.OsvojeniPoeniRequest;
import org.raflab.studsluzba.controllers.response.OsvojeniPoeniResponse;
import org.raflab.studsluzba.model.OsvojeniPoeni;
import org.raflab.studsluzba.model.StudentIndeks;

import java.util.ArrayList;
import java.util.List;

public class Converters {

    public static Nastavnik toNastavnik(NastavnikRequest nastavnikRequest) {
        Nastavnik nastavnik = new Nastavnik();
        nastavnik.setIme(nastavnikRequest.getIme());
        nastavnik.setPrezime(nastavnikRequest.getPrezime());
        nastavnik.setSrednjeIme(nastavnikRequest.getSrednjeIme());
        nastavnik.setEmail(nastavnikRequest.getEmail());
        nastavnik.setBrojTelefona(nastavnikRequest.getBrojTelefona());
        nastavnik.setAdresa(nastavnikRequest.getAdresa());
        //nastavnik.setZvanja(nastavnikRequest.getZvanja());
        nastavnik.setDatumRodjenja(nastavnikRequest.getDatumRodjenja());
        nastavnik.setPol(nastavnikRequest.getPol());
        nastavnik.setJmbg(nastavnikRequest.getJmbg());
        return nastavnik;
    }

    public static NastavnikResponse toNastavnikResponse(Nastavnik nastavnik) {
        NastavnikResponse response = new NastavnikResponse();
        response.setId(nastavnik.getId());
        response.setIme(nastavnik.getIme());
        response.setPrezime(nastavnik.getPrezime());
        response.setSrednjeIme(nastavnik.getSrednjeIme());
        response.setEmail(nastavnik.getEmail());
        response.setBrojTelefona(nastavnik.getBrojTelefona());
        response.setAdresa(nastavnik.getAdresa());
       //response.setZvanja(nastavnik.getZvanja());
        response.setDatumRodjenja(nastavnik.getDatumRodjenja());
        response.setPol(nastavnik.getPol());
        response.setJmbg(nastavnik.getJmbg());
        return response;
    }

    public static List<NastavnikResponse> toNastavnikResponseList(Iterable<Nastavnik> nastavnikIterable) {
        List<NastavnikResponse> nastavnikResponses = new ArrayList<>();

        nastavnikIterable.forEach((nastavnik) -> {
            nastavnikResponses.add(toNastavnikResponse(nastavnik));
        });
        return nastavnikResponses;
    }

    public static StudentPodaci toStudentPodaci(StudentPodaciRequest request) {
        StudentPodaci studentPodaci = new StudentPodaci();
        studentPodaci.setIme(request.getIme());
        studentPodaci.setPrezime(request.getPrezime());
        studentPodaci.setSrednjeIme(request.getSrednjeIme());
        studentPodaci.setJmbg(request.getJmbg());
        studentPodaci.setDatumRodjenja(request.getDatumRodjenja());
        studentPodaci.setMestoRodjenja(request.getMestoRodjenja());
        studentPodaci.setMestoPrebivalista(request.getMestoPrebivalista());
        studentPodaci.setDrzavaRodjenja(request.getDrzavaRodjenja());
        studentPodaci.setDrzavljanstvo(request.getDrzavljanstvo());
        studentPodaci.setNacionalnost(request.getNacionalnost());
        studentPodaci.setPol(request.getPol());
        studentPodaci.setAdresa(request.getAdresa());
        studentPodaci.setBrojTelefonaMobilni(request.getBrojTelefonaMobilni());
        studentPodaci.setBrojTelefonaFiksni(request.getBrojTelefonaFiksni());
        studentPodaci.setEmail(request.getEmail());
        studentPodaci.setBrojLicneKarte(request.getBrojLicneKarte());
        studentPodaci.setLicnuKartuIzdao(request.getLicnuKartuIzdao());
        studentPodaci.setMestoStanovanja(request.getMestoStanovanja());
        studentPodaci.setAdresaStanovanja(request.getAdresaStanovanja());
        return studentPodaci;
    }

    public static StudentIndeks toStudentIndeks(StudentIndeksRequest studentIndeksRequest) {
        StudentIndeks studentIndeks = new StudentIndeks();
        studentIndeks.setGodina(studentIndeksRequest.getGodina());
        studentIndeks.setStudProgramOznaka(studentIndeksRequest.getStudProgramOznaka());
        studentIndeks.setNacinFinansiranja(studentIndeksRequest.getNacinFinansiranja());
        studentIndeks.setAktivan(studentIndeksRequest.isAktivan());
        studentIndeks.setVaziOd(studentIndeksRequest.getVaziOd());
        return studentIndeks;
    }

    // SkolskaGodina converters
    public static SkolskaGodina toSkolskaGodina(SkolskaGodinaRequest request) {
        SkolskaGodina godina = new SkolskaGodina();
        godina.setNaziv(request.getNaziv());
        godina.setPocetakZimskog(request.getPocetakZimskog());
        godina.setKrajZimskog(request.getKrajZimskog());
        godina.setPocetakLetnjeg(request.getPocetakLetnjeg());
        godina.setKrajLetnjeg(request.getKrajLetnjeg());
        godina.setAktivna(request.getAktivna() != null ? request.getAktivna() : false);
        return godina;
    }

    public static SkolskaGodinaResponse toSkolskaGodinaResponse(SkolskaGodina godina) {
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

    public static List<SkolskaGodinaResponse> toSkolskaGodinaResponseList(Iterable<SkolskaGodina> godine) {
        List<SkolskaGodinaResponse> responses = new ArrayList<>();
        godine.forEach(godina -> responses.add(toSkolskaGodinaResponse(godina)));
        return responses;
    }
    // PredispitnaObaveza converters
    public static PredispitnaObaveza toPredispitnaObaveza(PredispitnaObavezaRequest request,
                                                          Predmet predmet,
                                                          SkolskaGodina godina) {
        PredispitnaObaveza obaveza = new PredispitnaObaveza();
        obaveza.setPredmet(predmet);
        obaveza.setSkolskaGodina(godina);
        obaveza.setVrsta(request.getVrsta());
        obaveza.setMaxPoena(request.getMaxPoena());
        return obaveza;
    }

    public static PredispitnaObavezaResponse toPredispitnaObavezaResponse(PredispitnaObaveza obaveza) {
        PredispitnaObavezaResponse response = new PredispitnaObavezaResponse();
        response.setId(obaveza.getId());
        response.setPredmetId(obaveza.getPredmet().getId());
        response.setPredmetNaziv(obaveza.getPredmet().getNaziv());
        response.setSkolskaGodinaId(obaveza.getSkolskaGodina().getId());
        response.setSkolskaGodinaNaziv(obaveza.getSkolskaGodina().getNaziv());
        response.setVrsta(obaveza.getVrsta());
        response.setMaxPoena(obaveza.getMaxPoena());
        return response;
    }

    public static List<PredispitnaObavezaResponse> toPredispitnaObavezaResponseList(Iterable<PredispitnaObaveza> obaveze) {
        List<PredispitnaObavezaResponse> responses = new ArrayList<>();
        obaveze.forEach(obaveza -> responses.add(toPredispitnaObavezaResponse(obaveza)));
        return responses;
    }
    // OsvojeniPoeni converters
    public static OsvojeniPoeni toOsvojeniPoeni(OsvojeniPoeniRequest request,
                                                StudentIndeks indeks,
                                                PredispitnaObaveza obaveza) {
        OsvojeniPoeni poeni = new OsvojeniPoeni();
        poeni.setStudentIndeks(indeks);
        poeni.setPredispitnaObaveza(obaveza);
        poeni.setPoeni(request.getPoeni());
        return poeni;
    }

    public static OsvojeniPoeniResponse toOsvojeniPoeniResponse(OsvojeniPoeni poeni) {
        OsvojeniPoeniResponse response = new OsvojeniPoeniResponse();
        response.setId(poeni.getId());
        response.setStudentIndeksId(poeni.getStudentIndeks().getId());
        response.setStudentIme(poeni.getStudentIndeks().getStudent().getIme());
        response.setStudentPrezime(poeni.getStudentIndeks().getStudent().getPrezime());
        response.setPredispitnaObavezaId(poeni.getPredispitnaObaveza().getId());
        response.setObavezaVrsta(poeni.getPredispitnaObaveza().getVrsta());
        response.setMaxPoena(poeni.getPredispitnaObaveza().getMaxPoena());
        response.setPoeni(poeni.getPoeni());
        return response;
    }

    public static List<OsvojeniPoeniResponse> toOsvojeniPoeniResponseList(Iterable<OsvojeniPoeni> poeni) {
        List<OsvojeniPoeniResponse> responses = new ArrayList<>();
        poeni.forEach(p -> responses.add(toOsvojeniPoeniResponse(p)));
        return responses;
    }
}
