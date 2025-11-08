package org.raflab.studsluzba.services;

import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.repositories.PredmetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PredmetService {

    @Autowired
    private PredmetRepository repository;

    public List<Predmet> findAll() {
        return (List<Predmet>) repository.findAll();
    }

    public Optional<Predmet> findById(Long id) {
        return repository.findById(id);
    }
}