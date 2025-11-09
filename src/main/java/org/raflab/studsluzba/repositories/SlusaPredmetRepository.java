package org.raflab.studsluzba.repositories;

import java.util.List;

import org.raflab.studsluzba.model.SlusaPredmet;
import org.raflab.studsluzba.model.StudentIndeks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SlusaPredmetRepository extends CrudRepository<SlusaPredmet, Long> {

    @Query("select sp from SlusaPredmet sp where sp.studentIndeks.id = :indeksId")
    List<SlusaPredmet> getSlusaPredmetForIndeksAktivnaGodina(Long indeksId);

    @Query("select sp.studentIndeks from SlusaPredmet sp where sp.drziPredmet.predmet.id = :idPredmeta "
            + "and sp.drziPredmet.nastavnik.id = :idNastavnika")
    List<StudentIndeks> getStudentiSlusaPredmetAktivnaGodina(Long idPredmeta, Long idNastavnika);

    @Query("select sp.studentIndeks from SlusaPredmet sp where sp.drziPredmet.id = :idDrziPredmet")
    List<StudentIndeks> getStudentiSlusaPredmetZaDrziPredmet(Long idDrziPredmet);

    @Query("select si from StudentIndeks si where not exists "
            + "(select sp from SlusaPredmet sp where sp.studentIndeks=si and sp.drziPredmet.id = :idDrziPredmet)")
    List<StudentIndeks> getStudentiNeSlusajuDrziPredmet(Long idDrziPredmet);

    @Query("SELECT sp FROM SlusaPredmet sp WHERE sp.studentIndeks = :studentIndeks " +
            "AND NOT EXISTS (" +
            "  SELECT pp FROM PolozenPredmet pp " +
            "  WHERE pp.studentIndeks.id = sp.studentIndeks.id " +
            "  AND pp.predmet.id = sp.drziPredmet.predmet.id" +
            ")")
    Page<SlusaPredmet> findNepolozeniPredmeti(@Param("studentIndeks") StudentIndeks studentIndeks, Pageable pageable);
}