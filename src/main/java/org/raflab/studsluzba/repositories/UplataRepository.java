package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.Student;
import org.raflab.studsluzba.model.Uplata;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UplataRepository extends CrudRepository<Uplata, Long> {

    List<Uplata> findByStudent(Student student);

    List<Uplata> findByStudentOrderByDatumUplateDesc(Student student);
}