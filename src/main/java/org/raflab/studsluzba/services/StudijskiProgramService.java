package org.raflab.studsluzba.services;

import org.raflab.studsluzba.model.StudijskiProgram;
import org.raflab.studsluzba.repositories.StudijskiProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudijskiProgramService {

    @Autowired
    private StudijskiProgramRepository repository;

    public List<StudijskiProgram> findAll() {
        return (List<StudijskiProgram>) repository.findAll();
    }

    public Optional<StudijskiProgram> findById(Long id) {
        return repository.findById(id);
    }

    public StudijskiProgram save(StudijskiProgram program) {
        return repository.save(program);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}