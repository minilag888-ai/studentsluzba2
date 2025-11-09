package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.PolozenPredmet;
import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PolozenPredmetRepository extends CrudRepository<PolozenPredmet, Long> {

    List<PolozenPredmet> findByStudent(Student student);

    Page<PolozenPredmet> findByStudent(Student student, Pageable pageable);

    Optional<PolozenPredmet> findByStudentAndPredmet(Student student, Predmet predmet);

    boolean existsByStudentAndPredmet(Student student, Predmet predmet);

    List<PolozenPredmet> findByPredmet(Predmet predmet);
}