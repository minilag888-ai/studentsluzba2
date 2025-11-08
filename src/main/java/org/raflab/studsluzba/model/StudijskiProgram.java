package org.raflab.studsluzba.model;

import java.util.List;
import java.util.Objects;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "predmeti")
public class StudijskiProgram {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String oznaka;  // RN, RM
	private String naziv;   
	private Integer godinaAkreditacije;
	private String zvanje;
	private Integer trajanjeGodina;
	private Integer trajanjeSemestara;
	private String vrstaStudija; // OAS - osnovne akademske studje, OSS - osnovne strukovne, 	MAS - master akademske studije
	private Integer ukupnoEspb;
	

	@OneToMany(mappedBy = "studProgram")
    @JsonIgnore
	private List<Predmet> predmeti;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudijskiProgram that = (StudijskiProgram) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
