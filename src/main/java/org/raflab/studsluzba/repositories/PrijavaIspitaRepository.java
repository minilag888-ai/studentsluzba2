package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.Ispit;
import org.raflab.studsluzba.model.PrijavaIspita;
import org.raflab.studsluzba.model.Student;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PrijavaIspitaRepository extends CrudRepository<PrijavaIspita, Long> {

    List<PrijavaIspita> findByIspit(Ispit ispit);

    List<PrijavaIspita> findByStudent(Student student);

    List<PrijavaIspita> findByIspitAndIzasao(Ispit ispit, Boolean izasao);

    Optional<PrijavaIspita> findByStudentAndIspit(Student student, Ispit ispit);

    boolean existsByStudentAndIspit(Student student, Ispit ispit);
}