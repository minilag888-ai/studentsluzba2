package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.Indeks;
import org.raflab.studsluzba.model.Student;
import org.raflab.studsluzba.model.StudijskiProgram;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface IndeksRepository extends CrudRepository<Indeks, Long> {

    List<Indeks> findByStudent(Student student);

    List<Indeks> findByStudentAndAktivan(Student student, Boolean aktivan);

    Optional<Indeks> findByGodinaUpisaAndBrojAndStudijskiProgram(
            Integer godinaUpisa, Integer broj, StudijskiProgram studijskiProgram);

    boolean existsByGodinaUpisaAndBrojAndStudijskiProgram(
            Integer godinaUpisa, Integer broj, StudijskiProgram studijskiProgram);
}