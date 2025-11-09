package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.Indeks;
import org.raflab.studsluzba.model.ObnovaGodine;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ObnovaGodineRepository extends CrudRepository<ObnovaGodine, Long> {

    List<ObnovaGodine> findByIndeks(Indeks indeks);

    List<ObnovaGodine> findByIndeksOrderByGodinaStudijaAsc(Indeks indeks);
}