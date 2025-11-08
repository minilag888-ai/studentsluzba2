package org.raflab.studsluzba.services;

import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.model.StudijskiProgram;
import org.raflab.studsluzba.repositories.PredmetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PredmetService {

    @Autowired
    private PredmetRepository repository;

    public List<Predmet> findAll() {
        return (List<Predmet>) repository.findAll();
    }

    public List<Predmet> findByGodinaAkreditacije(Integer godinaAkreditacije) {
        return repository.getPredmetForGodinaAkreditacije(godinaAkreditacije);
    }

    public Optional<Predmet> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<Predmet> findBySifra(String sifra) {
        return repository.findBySifra(sifra);
    }

    public List<Predmet> findByStudProgramAndObavezan(StudijskiProgram program, boolean obavezan) {
        return repository.getPredmetsByStudProgramAndObavezan(program, obavezan);
    }

    public Predmet save(Predmet predmet) {
        return repository.save(predmet);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    public boolean existsBySifra(String sifra) {
        return repository.existsBySifra(sifra);
    }
}