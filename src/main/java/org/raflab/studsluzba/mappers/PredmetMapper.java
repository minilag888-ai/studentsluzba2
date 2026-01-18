package org.raflab.studsluzba.mappers;

import org.raflab.studsluzba.model.Predmet;
import org.springframework.stereotype.Component;
import org.raflab.studsluzba.shared.dtos.PredmetDTO;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PredmetMapper {

    public PredmetDTO toDTO(Predmet predmet) {
        if (predmet == null) {
            return null;
        }

        PredmetDTO dto = new PredmetDTO();
        dto.setId(predmet.getId());
        dto.setSifra(predmet.getSifra());
        dto.setNaziv(predmet.getNaziv());
        dto.setOpis(predmet.getOpis());
        dto.setEspb(predmet.getEspb());
        dto.setObavezan(predmet.isObavezan());


        if (predmet.getStudProgram() != null) {
            dto.setStudijskiProgramNaziv(predmet.getStudProgram().getNaziv());
        }

        return dto;
    }

    public List<PredmetDTO> toDTOList(List<Predmet> predmeti) {
        if (predmeti == null) {
            return null;
        }
        return predmeti.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}