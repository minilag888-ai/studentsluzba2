package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.PolozenPredmet;
import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.model.StudentIndeks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PolozenPredmetRepository extends CrudRepository<PolozenPredmet, Long> {

    List<PolozenPredmet> findByStudentIndeks(StudentIndeks studentIndeks);

    Page<PolozenPredmet> findByStudentIndeks(StudentIndeks studentIndeks, Pageable pageable);

    Optional<PolozenPredmet> findByStudentIndeksAndPredmet(StudentIndeks studentIndeks, Predmet predmet);

    boolean existsByStudentIndeksAndPredmet(StudentIndeks studentIndeks, Predmet predmet);

    List<PolozenPredmet> findByPredmet(Predmet predmet);
}