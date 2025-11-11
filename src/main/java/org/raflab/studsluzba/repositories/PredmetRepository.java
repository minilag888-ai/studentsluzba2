package org.raflab.studsluzba.repositories;

import java.util.List;
import java.util.Optional;

import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.model.StudijskiProgram;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PredmetRepository extends CrudRepository<Predmet, Long> {

    @Query("select p from Predmet p where p.studProgram.godinaAkreditacije = :godinaAkreditacije")
    List<Predmet> getPredmetForGodinaAkreditacije(Integer godinaAkreditacije);

    List<Predmet> getPredmetsByStudProgramAndObavezan(StudijskiProgram studProgram, boolean obavezan);

    List<Predmet> findByIdIn(List<Long> ids);
    List<Predmet> findByNazivIn(List<String> nazivi);

    Optional<Predmet> findBySifra(String sifra);
    boolean existsBySifra(String sifra);


    List<Predmet> findByStudProgram(StudijskiProgram studProgram);

    @Query("SELECT AVG(pp.ocena) FROM PolozenPredmet pp " +
            "WHERE pp.predmet.id = :predmetId " +
            "AND YEAR(pp.datumPolaganja) BETWEEN :odGodine AND :doGodine")
    Double getAverageOcenaForPredmetInRange(@Param("predmetId") Long predmetId,
                                            @Param("odGodine") Integer odGodine,
                                            @Param("doGodine") Integer doGodine);

    @Query("SELECT COUNT(pp) FROM PolozenPredmet pp " +
            "WHERE pp.predmet.id = :predmetId " +
            "AND YEAR(pp.datumPolaganja) BETWEEN :odGodine AND :doGodine")
    Long countPolaganjaForPredmetInRange(@Param("predmetId") Long predmetId,
                                         @Param("odGodine") Integer odGodine,
                                         @Param("doGodine") Integer doGodine);
}