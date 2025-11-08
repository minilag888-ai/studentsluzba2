package org.raflab.studsluzba.services;

import org.raflab.studsluzba.model.StudentIndeks;
import org.raflab.studsluzba.repositories.StudentIndeksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;


import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class StudentIndeksService {
    
    @Autowired
    private StudentIndeksRepository studentIndeksRepository;

    @Transactional(readOnly = true)
    public int findBroj(int godina, String studProgramOznaka) {
        List<Integer> brojeviList = studentIndeksRepository.
                findBrojeviByGodinaAndStudProgramOznaka(godina, studProgramOznaka);

        return findNextAvailableNumber(brojeviList);
    }

    private int findNextAvailableNumber(List<Integer> brojeviList) {
        if (brojeviList == null || brojeviList.isEmpty()) return 1;

        List<Integer> sorted = brojeviList.stream()
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .collect(java.util.stream.Collectors.toList()); // <= works on JDK 8+

        int expected = 1;
        for (int num : sorted) {
            if (num != expected) return expected;
            expected++;
        }
        return expected;
    }

    public StudentIndeks findByStudentIdAndAktivan(Long studentPodaciId) {
        return studentIndeksRepository.findAktivanStudentIndeksiByStudentPodaciId(studentPodaciId);
    }
    public List<StudentIndeks> findAll() {
        return studentIndeksRepository.findAll();
    }

    public Optional<StudentIndeks> findById(Long id) {
        return studentIndeksRepository.findById(id);
    }

    @Transactional
    public StudentIndeks save(StudentIndeks indeks) {
        return studentIndeksRepository.save(indeks);
    }

    @Transactional
    public void deleteById(Long id) {
        studentIndeksRepository.deleteById(id);
    }
}
