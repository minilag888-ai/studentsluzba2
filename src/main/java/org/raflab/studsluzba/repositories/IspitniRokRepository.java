package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.IspitniRok;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IspitniRokRepository extends CrudRepository<IspitniRok, Long> {
    List<IspitniRok> findBySkolskaGodinaId(Long godinaId);
}