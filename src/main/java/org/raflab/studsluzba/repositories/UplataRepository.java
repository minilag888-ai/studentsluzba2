package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.StudentPodaci;  // ← PROMENJENO
import org.raflab.studsluzba.model.Uplata;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UplataRepository extends CrudRepository<Uplata, Long> {

    List<Uplata> findByStudent(StudentPodaci student);  // ← PROMENJENO

    List<Uplata> findByStudentOrderByDatumUplateDesc(StudentPodaci student);  // ← PROMENJENO

    @Query("SELECT SUM(u.iznosEur) FROM Uplata u WHERE u.student.id = :studentId")
    Double sumIznosEurByStudentId(@Param("studentId") Long studentId);



}