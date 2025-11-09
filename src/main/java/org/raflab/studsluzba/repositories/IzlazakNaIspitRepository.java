package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.IzlazakNaIspit;
import org.raflab.studsluzba.model.PrijavaIspita;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IzlazakNaIspitRepository extends CrudRepository<IzlazakNaIspit, Long> {

    Optional<IzlazakNaIspit> findByPrijavaIspita(PrijavaIspita prijavaIspita);

    boolean existsByPrijavaIspita(PrijavaIspita prijavaIspita);
}