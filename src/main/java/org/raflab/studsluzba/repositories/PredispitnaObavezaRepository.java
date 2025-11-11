package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.PredispitnaObaveza;
import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.model.SkolskaGodina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PredispitnaObavezaRepository extends JpaRepository<PredispitnaObaveza, Long> {

    List<PredispitnaObaveza> findByPredmetAndSkolskaGodina(Predmet predmet, SkolskaGodina godina);

    List<PredispitnaObaveza> findByPredmet_IdAndSkolskaGodina_Id(Long predmetId, Long skolskaGodinaId);



}