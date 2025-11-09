package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.UpisGodine;
import org.raflab.studsluzba.model.StudentIndeks;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UpisGodineRepository extends CrudRepository<UpisGodine, Long> {

    @Query("SELECT DISTINCT ug FROM UpisGodine ug " +
            "LEFT JOIN FETCH ug.predmeti " +
            "WHERE ug.studentIndeks = :studentIndeks " +
            "ORDER BY ug.godinaStudija ASC")
    List<UpisGodine> findByStudentIndeksOrderByGodinaStudijaAsc(@Param("studentIndeks") StudentIndeks studentIndeks);
}