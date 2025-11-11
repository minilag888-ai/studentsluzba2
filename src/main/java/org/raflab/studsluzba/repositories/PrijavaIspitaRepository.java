package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.Ispit;
import org.raflab.studsluzba.model.PrijavaIspita;
import org.raflab.studsluzba.model.StudentIndeks;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PrijavaIspitaRepository extends CrudRepository<PrijavaIspita, Long> {

    List<PrijavaIspita> findByIspit(Ispit ispit);

    List<PrijavaIspita> findByStudentIndeks(StudentIndeks studentIndeks);

    List<PrijavaIspita> findByIspitAndIzasao(Ispit ispit, Boolean izasao);

    Optional<PrijavaIspita> findByStudentIndeksAndIspit(StudentIndeks studentIndeks, Ispit ispit);

    boolean existsByStudentIndeksAndIspit(StudentIndeks studentIndeks, Ispit ispit);

    // Dodatne metode za potrebe operacija

    @Query("SELECT pi FROM PrijavaIspita pi " +
            "JOIN FETCH pi.studentIndeks si " +
            "JOIN FETCH si.student " +
            "WHERE pi.ispit.id = :ispitId")
    List<PrijavaIspita> findByIspitIdWithStudentData(@Param("ispitId") Long ispitId);

    @Query("SELECT COUNT(pi) FROM PrijavaIspita pi " +
            "WHERE pi.studentIndeks.id = :studentIndeksId " +
            "AND pi.ispit.predmet.id = :predmetId " +
            "AND pi.izasao = true")
    Long countPokusajaPolaganja(@Param("studentIndeksId") Long studentIndeksId,
                                @Param("predmetId") Long predmetId);
}