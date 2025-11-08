package org.raflab.studsluzba.services;

import org.raflab.studsluzba.model.SkolskaGodina;
import org.raflab.studsluzba.repositories.SkolskaGodinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class SkolskaGodinaService {

    @Autowired
    private SkolskaGodinaRepository repository;

    public List<SkolskaGodina> findAll() {
        return repository.findAll();
    }

    public Optional<SkolskaGodina> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<SkolskaGodina> findAktivna() {
        return repository.findByAktivnaTrue();
    }

    @Transactional
    public SkolskaGodina save(SkolskaGodina skolskaGodina) {
        if (Boolean.TRUE.equals(skolskaGodina.getAktivna())) {
            deaktivirajSve();
        }
        return repository.save(skolskaGodina);
    }

    @Transactional
    public SkolskaGodina postaviAktivnu(Long id) {
        Optional<SkolskaGodina> optional = repository.findById(id);
        if (optional.isPresent()) {
            deaktivirajSve();
            SkolskaGodina godina = optional.get();
            godina.setAktivna(true);
            return repository.save(godina);
        }
        return null;
    }

    @Transactional
    public void deaktivirajSve() {
        List<SkolskaGodina> sve = repository.findAll();
        sve.forEach(g -> g.setAktivna(false));
        repository.saveAll(sve);
    }

    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}