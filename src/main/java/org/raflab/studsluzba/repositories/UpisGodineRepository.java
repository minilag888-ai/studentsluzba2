package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.StudentIndeks;
import org.raflab.studsluzba.model.UpisGodine;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UpisGodineRepository extends CrudRepository<UpisGodine, Long> {

    List<UpisGodine> findByStudentIndeks(StudentIndeks studentIndeks);

    List<UpisGodine> findByStudentIndeksOrderByGodinaStudijaAsc(StudentIndeks studentIndeks);
}