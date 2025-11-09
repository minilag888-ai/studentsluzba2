package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.Indeks;
import org.raflab.studsluzba.model.UpisGodine;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UpisGodineRepository extends CrudRepository<UpisGodine, Long> {

    List<UpisGodine> findByIndeks(Indeks indeks);

    List<UpisGodine> findByIndeksOrderByGodinaStudijaAsc(Indeks indeks);
}