package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.ObnovaGodine;
import org.raflab.studsluzba.model.StudentIndeks;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ObnovaGodineRepository extends CrudRepository<ObnovaGodine, Long> {

    @Query("SELECT DISTINCT og FROM ObnovaGodine og " +
            "LEFT JOIN FETCH og.predmeti " +
            "WHERE og.studentIndeks = :studentIndeks " +
            "ORDER BY og.godinaStudija ASC")
    List<ObnovaGodine> findByStudentIndeksOrderByGodinaStudijaAsc(@Param("studentIndeks") StudentIndeks studentIndeks);
}