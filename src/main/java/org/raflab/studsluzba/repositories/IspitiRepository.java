package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.Ispit;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface IspitiRepository extends CrudRepository<Ispit, Long> {

    @EntityGraph(attributePaths = {"studentIndeks", "studentIndeks.student", "predmet", "ispitniRok", "ispitniRok.skolskaGodina"})
    @Override
    Iterable<Ispit> findAll();

    @EntityGraph(attributePaths = {"studentIndeks", "studentIndeks.student", "predmet", "ispitniRok", "ispitniRok.skolskaGodina"})
    @Override
    Optional<Ispit> findById(Long id);

    @EntityGraph(attributePaths = {"studentIndeks", "studentIndeks.student", "predmet", "ispitniRok", "ispitniRok.skolskaGodina"})
    List<Ispit> findByStudentIndeksId(Long studentIndeksId);

    @EntityGraph(attributePaths = {"studentIndeks", "studentIndeks.student", "predmet", "ispitniRok", "ispitniRok.skolskaGodina"})
    List<Ispit> findByPredmetIdAndIspitniRokId(Long predmetId, Long rokId);
}