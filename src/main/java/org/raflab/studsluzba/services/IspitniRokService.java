package org.raflab.studsluzba.services;

import org.raflab.studsluzba.model.IspitniRok;
import org.raflab.studsluzba.repositories.IspitniRokRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class IspitniRokService {

    @Autowired
    private IspitniRokRepository repository;

    public List<IspitniRok> findAll() {
        return (List<IspitniRok>) repository.findAll();
    }

    public Optional<IspitniRok> findById(Long id) {
        return repository.findById(id);
    }

    public List<IspitniRok> findBySkolskaGodina(Long godinaId) {
        return repository.findBySkolskaGodinaId(godinaId);
    }

    public IspitniRok save(IspitniRok rok) {
        return repository.save(rok);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}