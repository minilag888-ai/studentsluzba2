import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.raflab.studsluzba.model.StudentIndeks;
import org.raflab.studsluzba.repositories.StudentIndeksRepository;
import org.raflab.studsluzba.services.StudentIndeksService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class StudentIndeksServiceTest {

    @Mock
    private StudentIndeksRepository studentIndeksRepository;

    @InjectMocks
    private StudentIndeksService studentIndeksService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testFindBrojWithGap() {
        // Arrange
        int godina = 2023;
        String studProgramOznaka = "IT";
        List<Integer> brojeviList = Arrays.asList(1, 2, 4, 5);

        when(studentIndeksRepository.findBrojeviByGodinaAndStudProgramOznaka(godina, studProgramOznaka))
                .thenReturn(brojeviList);

        // Act
        int result = studentIndeksService.findBroj(godina, studProgramOznaka);

        // Assert
        assertEquals(3, result);
        verify(studentIndeksRepository, times(1)).findBrojeviByGodinaAndStudProgramOznaka(godina, studProgramOznaka);
    }

    @Test
    void testFindBrojWithoutGap() {
        // Arrange
        int godina = 2023;
        String studProgramOznaka = "IT";
        List<Integer> brojeviList = Arrays.asList(1, 2, 3);

        when(studentIndeksRepository.findBrojeviByGodinaAndStudProgramOznaka(godina, studProgramOznaka))
                .thenReturn(brojeviList);

        // Act
        int result = studentIndeksService.findBroj(godina, studProgramOznaka);

        // Assert
        assertEquals(4, result);
        verify(studentIndeksRepository, times(1)).findBrojeviByGodinaAndStudProgramOznaka(godina, studProgramOznaka);
    }

    @Test
    void testFindBrojWithEmptyList() {
        // Arrange
        int godina = 2023;
        String studProgramOznaka = "IT";
        List<Integer> brojeviList = Collections.emptyList();

        when(studentIndeksRepository.findBrojeviByGodinaAndStudProgramOznaka(godina, studProgramOznaka))
                .thenReturn(brojeviList);

        // Act
        int result = studentIndeksService.findBroj(godina, studProgramOznaka);

        // Assert
        assertEquals(1, result);
        verify(studentIndeksRepository, times(1)).findBrojeviByGodinaAndStudProgramOznaka(godina, studProgramOznaka);
    }

    @Test
    void testFindByStudentIdAndAktivan() {
        // Arrange
        Long studentPodaciId = 1L;
        StudentIndeks expectedStudentIndeks = new StudentIndeks();
        expectedStudentIndeks.setId(1L);

        when(studentIndeksRepository.findAktivanStudentIndeksiByStudentPodaciId(studentPodaciId))
                .thenReturn(expectedStudentIndeks);

        // Act
        StudentIndeks result = studentIndeksService.findByStudentIdAndAktivan(studentPodaciId);

        // Assert
        assertEquals(expectedStudentIndeks, result);
        verify(studentIndeksRepository, times(1)).findAktivanStudentIndeksiByStudentPodaciId(studentPodaciId);
    }
}
