package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.SrednjaSkola;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SrednjaSkolaRepository extends CrudRepository<SrednjaSkola, Long> {

    List<SrednjaSkola> findByMesto(String mesto);

    List<SrednjaSkola> findByVrsta(String vrsta);

    List<SrednjaSkola> findByNazivContainingIgnoreCase(String naziv);
}