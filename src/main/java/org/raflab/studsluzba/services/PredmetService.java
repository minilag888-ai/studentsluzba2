package org.raflab.studsluzba.services;

import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.model.StudijskiProgram;
import org.raflab.studsluzba.mappers.PredmetMapper;
import org.raflab.studsluzba.repositories.PredmetRepository;
import org.raflab.studsluzba.repositories.StudijskiProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.raflab.studsluzba.shared.dtos.PredmetDTO;
import org.raflab.studsluzba.shared.dtos.ProsecnaOcenaDTO;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PredmetService {

    @Autowired
    private PredmetRepository repository;

    @Autowired
    private StudijskiProgramRepository studijskiProgramRepository;

    @Autowired
    private PredmetMapper predmetMapper;

    public List<Predmet> findAll() {
        return (List<Predmet>) repository.findAll();
    }

    public List<Predmet> findByGodinaAkreditacije(Integer godinaAkreditacije) {
        return repository.getPredmetForGodinaAkreditacije(godinaAkreditacije);
    }

    public Optional<Predmet> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<Predmet> findBySifra(String sifra) {
        return repository.findBySifra(sifra);
    }

    public List<Predmet> findByStudProgramAndObavezan(StudijskiProgram program, boolean obavezan) {
        return repository.getPredmetsByStudProgramAndObavezan(program, obavezan);
    }

    public Predmet save(Predmet predmet) {
        return repository.save(predmet);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    public boolean existsBySifra(String sifra) {
        return repository.existsBySifra(sifra);
    }



    /**
      Spisak predmeta na studijskom programu
     */
    @Transactional(readOnly = true)
    public List<PredmetDTO> getPredmetiNaStudijskomProgramu(Long studijskiProgramId) {
        StudijskiProgram studijskiProgram = studijskiProgramRepository.findById(studijskiProgramId)
                .orElseThrow(() -> new RuntimeException("Studijski program ne postoji"));

        List<Predmet> predmeti = repository.findByStudProgram(studijskiProgram);

        return predmeti.stream()
                .map(predmetMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
      Prosečna ocena studenata na predmetu za raspon godina
     */
    @Transactional(readOnly = true)
    public ProsecnaOcenaDTO getProsecnaOcenaNaPredmetu(Long predmetId, Integer odGodine, Integer doGodine) {
        Predmet predmet = repository.findById(predmetId)
                .orElseThrow(() -> new RuntimeException("Predmet ne postoji"));

        Double prosecnaOcena = repository.getAverageOcenaForPredmetInRange(predmetId, odGodine, doGodine);
        Long brojPolaganja = repository.countPolaganjaForPredmetInRange(predmetId, odGodine, doGodine);

        // Ako nema polaganja, prosek je null
        if (prosecnaOcena == null) {
            prosecnaOcena = 0.0;
        }

        ProsecnaOcenaDTO dto = new ProsecnaOcenaDTO();
        dto.setPredmetId(predmet.getId());
        dto.setSifraPredmeta(predmet.getSifra());
        dto.setNazivPredmeta(predmet.getNaziv());
        dto.setOdGodine(odGodine);
        dto.setDoGodine(doGodine);
        dto.setProsecnaOcena(prosecnaOcena);
        dto.setBrojPolaganja(brojPolaganja);

        return dto;
    }
}