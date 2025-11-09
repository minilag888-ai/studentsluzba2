package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.ObnovaGodine;
import org.raflab.studsluzba.model.StudentIndeks;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ObnovaGodineRepository extends CrudRepository<ObnovaGodine, Long> {

    List<ObnovaGodine> findByStudentIndeks(StudentIndeks studentIndeks);

    List<ObnovaGodine> findByStudentIndeksOrderByGodinaStudijaAsc(StudentIndeks studentIndeks);
}