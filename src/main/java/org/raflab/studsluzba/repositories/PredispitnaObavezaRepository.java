package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.PredispitnaObaveza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PredispitnaObavezaRepository extends JpaRepository<PredispitnaObaveza, Long> {

    List<PredispitnaObaveza> findByPredmetIdAndSkolskaGodinaId(Long predmetId, Long skolskaGodinaId);
}