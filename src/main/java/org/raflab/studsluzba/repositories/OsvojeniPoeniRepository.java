package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.OsvojeniPoeni;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OsvojeniPoeniRepository extends JpaRepository<OsvojeniPoeni, Long> {

    List<OsvojeniPoeni> findByStudentIndeksId(Long studentIndeksId);

    @Query("SELECT op FROM OsvojeniPoeni op WHERE op.studentIndeks.id = ?1 " +
            "AND op.predispitnaObaveza.predmet.id = ?2 " +
            "AND op.predispitnaObaveza.skolskaGodina.id = ?3")
    List<OsvojeniPoeni> findByStudentPredmetGodina(Long studentId, Long predmetId, Long godinaId);

    List<OsvojeniPoeni> findByPredispitnaObavezaPredmetIdAndPredispitnaObavezaSkolskaGodinaId(
            Long predmetId, Long godinaId
    );
}