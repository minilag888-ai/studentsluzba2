package org.raflab.studsluzba.services;

import org.raflab.studsluzba.model.Ispit;
import org.raflab.studsluzba.repositories.IspitiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class IspitCrudService {  //  PROMENIO IME

    @Autowired
    private IspitiRepository repository;

    public List<Ispit> findAll() {
        return (List<Ispit>) repository.findAll();
    }

    public Optional<Ispit> findById(Long id) {
        return repository.findById(id);
    }


    public List<Ispit> findByPredmetAndRok(Long predmetId, Long rokId) {
        return repository.findByPredmetIdAndIspitniRokId(predmetId, rokId);
    }

    public Ispit save(Ispit ispit) {
        return repository.save(ispit);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}