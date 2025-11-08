package org.raflab.studsluzba.services;

import org.raflab.studsluzba.model.PredispitnaObaveza;
import org.raflab.studsluzba.repositories.PredispitnaObavezaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class PredispitnaObavezaService {

    @Autowired
    private PredispitnaObavezaRepository repository;

    public List<PredispitnaObaveza> findAll() {
        return repository.findAll();
    }

    public Optional<PredispitnaObaveza> findById(Long id) {
        return repository.findById(id);
    }

    public List<PredispitnaObaveza> findByPredmetAndSkolskaGodina(Long predmetId, Long skolskaGodinaId) {
        return repository.findByPredmetIdAndSkolskaGodinaId(predmetId, skolskaGodinaId);
    }

    @Transactional
    public PredispitnaObaveza save(PredispitnaObaveza obaveza) {
        return repository.save(obaveza);
    }

    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}