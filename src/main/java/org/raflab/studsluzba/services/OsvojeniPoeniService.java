package org.raflab.studsluzba.services;

import org.raflab.studsluzba.model.OsvojeniPoeni;
import org.raflab.studsluzba.repositories.OsvojeniPoeniRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class OsvojeniPoeniService {

    @Autowired
    private OsvojeniPoeniRepository repository;

    public List<OsvojeniPoeni> findAll() {
        return repository.findAll();
    }

    public Optional<OsvojeniPoeni> findById(Long id) {
        return repository.findById(id);
    }

    public List<OsvojeniPoeni> findByStudent(Long studentIndeksId) {
        return repository.findByStudentIndeksId(studentIndeksId);
    }

    public List<OsvojeniPoeni> findByStudentPredmetGodina(Long studentId, Long predmetId, Long godinaId) {
        return repository.findByStudentPredmetGodina(studentId, predmetId, godinaId);
    }

    @Transactional
    public OsvojeniPoeni save(OsvojeniPoeni poeni) {
        return repository.save(poeni);
    }

    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}