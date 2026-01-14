package org.raflab.studsluzba.controllers;

import java.util.List;
import java.util.Optional;

import org.raflab.studsluzba.controllers.request.NastavnikRequest;
import org.raflab.studsluzba.controllers.response.NastavnikResponse;
import org.raflab.studsluzba.mappers.NastavnikMapper;
import org.raflab.studsluzba.model.Nastavnik;
import org.raflab.studsluzba.services.NastavnikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/nastavnik")
public class NastavnikController {

    @Autowired
    NastavnikService nastavnikService;

    @Autowired
    NastavnikMapper nastavnikMapper;

    @PostMapping(path = "/add")
    public Long addNewNastavnik(@RequestBody @Valid NastavnikRequest nastavnikRequest) {
        Nastavnik nastavnik = nastavnikMapper.toEntity(nastavnikRequest);
        nastavnik = nastavnikService.save(nastavnik);
        return nastavnik.getId();
    }

    @GetMapping(path = "/all")
    public List<NastavnikResponse> getAllNastavnik() {
        return nastavnikMapper.toResponseList(nastavnikService.findAll());
    }

    @GetMapping(path = "/{id}")
    public NastavnikResponse getNastavnikById(@PathVariable Long id) {
        Optional<Nastavnik> rez = nastavnikService.findById(id);
        return rez.map(nastavnikMapper::toResponse).orElse(null);
    }

    @GetMapping(path = "path/{id}")
    public NastavnikResponse getNastavnikPath(@PathVariable Long id) {
        //TODO
        return null;
    }

    @GetMapping(path = "/search")
    public List<NastavnikResponse> search(
            @RequestParam(required = false) String ime,
            @RequestParam(required = false) String prezime){
        return nastavnikMapper.toResponseList(nastavnikService.findByImeAndPrezime(ime, prezime));
    }
}