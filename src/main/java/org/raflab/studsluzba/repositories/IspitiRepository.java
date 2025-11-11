package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.Ispit;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface IspitiRepository extends CrudRepository<Ispit, Long> {

    @EntityGraph(attributePaths = {"predmet", "ispitniRok", "ispitniRok.skolskaGodina", "drziPredmet", "drziPredmet.nastavnik"})
    @Override
    Iterable<Ispit> findAll();

    @EntityGraph(attributePaths = {"predmet", "ispitniRok", "ispitniRok.skolskaGodina", "drziPredmet", "drziPredmet.nastavnik"})
    @Override
    Optional<Ispit> findById(Long id);

    @EntityGraph(attributePaths = {"predmet", "ispitniRok", "ispitniRok.skolskaGodina", "drziPredmet", "drziPredmet.nastavnik"})
    List<Ispit> findByPredmetIdAndIspitniRokId(Long predmetId, Long rokId);
}