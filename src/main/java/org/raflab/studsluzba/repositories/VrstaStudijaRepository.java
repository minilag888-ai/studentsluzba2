package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.VrstaStudija;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface VrstaStudijaRepository extends CrudRepository<VrstaStudija, Long> {

    Optional<VrstaStudija> findBySkraceniNaziv(String skraceniNaziv);

    boolean existsBySkraceniNaziv(String skraceniNaziv);
}