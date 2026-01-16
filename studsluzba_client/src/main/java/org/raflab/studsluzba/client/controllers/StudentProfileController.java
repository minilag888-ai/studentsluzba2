package org.raflab.studsluzba.client.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.extern.slf4j.Slf4j;
import org.raflab.studsluzba.client.dto.*;
import org.raflab.studsluzba.client.services.ReportService;
import org.raflab.studsluzba.client.services.StudentService;
import org.raflab.studsluzba.client.utils.AlertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class StudentProfileController {

    @FXML
    private Label lblIme;

    @FXML
    private Label lblPrezime;

    @FXML
    private Label lblIndeks;

    @FXML
    private Label lblEmail;

    @FXML
    private Label lblEspb;

    @FXML
    private Label lblProsek;

    @FXML
    private Label lblPreostaliIznos;

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab tabPolozeni;

    @FXML
    private Tab tabNepolozeni;

    @FXML
    private Tab tabUpisaneGodine;

    @FXML
    private Tab tabObnovljeneGodine;

    @FXML
    private Tab tabUplate;

    @FXML
    private TableView<PolozenPredmetDTO> tablePolozeni;

    @FXML
    private TableColumn<PolozenPredmetDTO, String> colSifra;

    @FXML
    private TableColumn<PolozenPredmetDTO, String> colNaziv;

    @FXML
    private TableColumn<PolozenPredmetDTO, Integer> colEspb;

    @FXML
    private TableColumn<PolozenPredmetDTO, Integer> colOcena;

    @FXML
    private TableView<NepolozenPredmetDTO> tableNepolozeni;

    @FXML
    private TableColumn<NepolozenPredmetDTO, String> colSifraNepolozeni;

    @FXML
    private TableColumn<NepolozenPredmetDTO, String> colNazivNepolozeni;

    @FXML
    private TableColumn<NepolozenPredmetDTO, Integer> colEspbNepolozeni;

    @FXML
    private Button btnUverenjeOStudiranju;

    @FXML
    private Button btnUverenjeOPolozenim;

    @FXML
    private Button btnDodajUplatu;

    @Autowired
    private StudentService studentService;

    @Autowired
    private ReportService reportService;

    private StudentProfileDTO currentStudent;
    private final ObservableList<PolozenPredmetDTO> polozeniData = FXCollections.observableArrayList();
    private final ObservableList<NepolozenPredmetDTO> nepolozeniData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        log.info("StudentProfileController initialized");

        setupTables();
    }

    /**
     * Setup tabela
     */
    private void setupTables() {
        // Položeni predmeti
        colSifra.setCellValueFactory(new PropertyValueFactory<>("sifraPredmeta"));
        colNaziv.setCellValueFactory(new PropertyValueFactory<>("nazivPredmeta"));
        colEspb.setCellValueFactory(new PropertyValueFactory<>("espb"));
        colOcena.setCellValueFactory(new PropertyValueFactory<>("ocena"));
        tablePolozeni.setItems(polozeniData);

        // Nepoloženi predmeti
        colSifraNepolozeni.setCellValueFactory(new PropertyValueFactory<>("sifraPredmeta"));
        colNazivNepolozeni.setCellValueFactory(new PropertyValueFactory<>("nazivPredmeta"));
        colEspbNepolozeni.setCellValueFactory(new PropertyValueFactory<>("espb"));
        tableNepolozeni.setItems(nepolozeniData);
    }

    /**
     * Učitaj studenta
     */
    public void loadStudent(Long studentIndeksId) {
        log.info("Loading student profile for ID: {}", studentIndeksId);

        studentService.getStudentProfile(studentIndeksId)
                .subscribe(
                        profile -> Platform.runLater(() -> {
                            this.currentStudent = profile;
                            displayStudentInfo(profile);
                            loadPolozeniPredmeti(studentIndeksId);
                            loadNepolozeniPredmeti(studentIndeksId);
                            loadPreostaliIznos(studentIndeksId);
                        }),
                        error -> Platform.runLater(() -> {
                            log.error("Failed to load student", error);
                            AlertUtil.showException("Greška", (Exception) error);
                        })
                );
    }

    /**
     * Prikaži osnovne podatke studenta
     */
    private void displayStudentInfo(StudentProfileDTO student) {
        lblIme.setText(student.getIme());
        lblPrezime.setText(student.getPrezime());
        lblIndeks.setText(student.getGodina() + "/" + student.getBroj() + " - " + student.getStudProgramOznaka());
        lblEmail.setText(student.getEmail());
    }

    /**
     * Učitaj položene predmete
     */
    private void loadPolozeniPredmeti(Long studentIndeksId) {
        studentService.getPolozeniPredmeti(studentIndeksId, 0, 100)
                .subscribe(
                        page -> Platform.runLater(() -> {
                            polozeniData.clear();
                            polozeniData.addAll(page.getContent());

                            // Izračunaj ESPB i prosek
                            int ukupnoEspb = studentService.calculateTotalESPB(page.getContent());
                            double prosek = studentService.calculateAverageGrade(page.getContent());

                            lblEspb.setText(String.valueOf(ukupnoEspb));
                            lblProsek.setText(String.format("%.2f", prosek));
                        }),
                        error -> log.error("Failed to load polozeni predmeti", error)
                );
    }

    /**
     * Učitaj nepoložene predmete
     */
    private void loadNepolozeniPredmeti(Long studentIndeksId) {
        studentService.getNepolozeniPredmeti(studentIndeksId, 0, 100)
                .subscribe(
                        page -> Platform.runLater(() -> {
                            nepolozeniData.clear();
                            nepolozeniData.addAll(page.getContent());
                        }),
                        error -> log.error("Failed to load nepolozeni predmeti", error)
                );
    }

    /**
     * Učitaj preostali iznos za uplatu
     */
    private void loadPreostaliIznos(Long studentIndeksId) {
        studentService.getPreostaliIznos(studentIndeksId)
                .subscribe(
                        iznos -> Platform.runLater(() -> {
                            lblPreostaliIznos.setText(
                                    String.format("%.2f EUR (%.2f RSD)", iznos.getPreostaliIznosEur(), iznos.getPreostaliIznosRsd())
                            );
                        }),
                        error -> log.error("Failed to load preostali iznos", error)
                );
    }

    /**
     * Generiši uverenje o studiranju
     */
    @FXML
    private void onGenerateUverenjeOStudiranju() {
        if (currentStudent == null) {
            AlertUtil.showError("Greška", "Student nije učitan");
            return;
        }

        try {
            reportService.generateUverenjeOStudiranju(currentStudent);
            AlertUtil.showInfo("Uspeh", "Uverenje je generisano i otvoreno");
        } catch (Exception e) {
            log.error("Failed to generate uverenje", e);
            AlertUtil.showException("Greška", e);
        }
    }

    /**
     * Generiši uverenje o položenim ispitima
     */
    @FXML
    private void onGenerateUverenjeOPolozenim() {
        if (currentStudent == null) {
            AlertUtil.showError("Greška", "Student nije učitan");
            return;
        }

        try {
            List<PolozenPredmetDTO> polozeni = polozeniData;
            reportService.generateUverenjeOPolozenimIspitima(currentStudent, polozeni);
            AlertUtil.showInfo("Uspeh", "Uverenje je generisano i otvoreno");
        } catch (Exception e) {
            log.error("Failed to generate uverenje", e);
            AlertUtil.showException("Greška", e);
        }
    }

    /**
     * Dodaj uplatu
     */
    @FXML
    private void onDodajUplatu() {
        if (currentStudent == null) {
            AlertUtil.showError("Greška", "Student nije učitan");
            return;
        }

        // TODO: Otvori dijalog za dodavanje uplate
        AlertUtil.showInfo("TODO", "Funkcionalnost u izradi");
    }
}