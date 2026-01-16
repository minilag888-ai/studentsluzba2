package org.raflab.studsluzba.client.controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import lombok.extern.slf4j.Slf4j;
import org.raflab.studsluzba.client.dto.*;
import org.raflab.studsluzba.client.services.ReportService;
import org.raflab.studsluzba.client.services.StudentService;
import org.raflab.studsluzba.client.utils.AlertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
public class StudentProfileController {

    // Header labels
    @FXML private Label lblIme;
    @FXML private Label lblPrezime;
    @FXML private Label lblIndeks;
    @FXML private Label lblEmail;
    @FXML private Label lblEspb;
    @FXML private Label lblProsek;
    @FXML private Label lblPreostaliIznos;

    // TabPane
    @FXML private TabPane tabPane;

    // Položeni predmeti
    @FXML private TableView<PolozenPredmetDTO> tablePolozeni;
    @FXML private TableColumn<PolozenPredmetDTO, String> colSifra;
    @FXML private TableColumn<PolozenPredmetDTO, String> colNaziv;
    @FXML private TableColumn<PolozenPredmetDTO, Integer> colEspb;
    @FXML private TableColumn<PolozenPredmetDTO, Integer> colOcena;
    @FXML private TableColumn<PolozenPredmetDTO, LocalDate> colDatum;

    // Nepoloženi predmeti
    @FXML private TableView<NepolozenPredmetDTO> tableNepolozeni;
    @FXML private TableColumn<NepolozenPredmetDTO, String> colSifraNepolozeni;
    @FXML private TableColumn<NepolozenPredmetDTO, String> colNazivNepolozeni;
    @FXML private TableColumn<NepolozenPredmetDTO, Integer> colEspbNepolozeni;
    @FXML private TableColumn<NepolozenPredmetDTO, Integer> colBrojPokusaja;

    // Upisane godine
    @FXML private TableView<UpisGodineDTO> tableUpisaneGodine;
    @FXML private TableColumn<UpisGodineDTO, Integer> colGodinaStudija;
    @FXML private TableColumn<UpisGodineDTO, String> colSkolskaGodina;
    @FXML private TableColumn<UpisGodineDTO, LocalDate> colDatumUpisa;
    @FXML private TableColumn<UpisGodineDTO, Integer> colUkupnoESPB;
    @FXML private TableColumn<UpisGodineDTO, String> colNapomena;

    // Obnovljene godine
    @FXML private TableView<ObnovaGodineDTO> tableObnovljeneGodine;
    @FXML private TableColumn<ObnovaGodineDTO, Integer> colGodinaObnovljene;
    @FXML private TableColumn<ObnovaGodineDTO, String> colSkolskaGodinaObnova;
    @FXML private TableColumn<ObnovaGodineDTO, LocalDate> colDatumObnove;
    @FXML private TableColumn<ObnovaGodineDTO, Integer> colUkupnoESPBObnova;
    @FXML private TableColumn<ObnovaGodineDTO, String> colNapomenaObnova;

    // Uplate
    @FXML private TableView<UplataDTO> tableUplate;
    @FXML private TableColumn<UplataDTO, LocalDate> colDatumUplate;
    @FXML private TableColumn<UplataDTO, Double> colIznosEur;
    @FXML private TableColumn<UplataDTO, Double> colSrednjiKurs;
    @FXML private TableColumn<UplataDTO, Double> colIznosRsd;
    @FXML private Label lblUkupnoUplaceno;

    @Autowired private StudentService studentService;
    @Autowired private ReportService reportService;

    private StudentProfileDTO currentStudent;
    private Long currentStudentIndeksId;

    private final ObservableList<PolozenPredmetDTO> polozeniData = FXCollections.observableArrayList();
    private final ObservableList<NepolozenPredmetDTO> nepolozeniData = FXCollections.observableArrayList();
    private final ObservableList<UpisGodineDTO> upisaneData = FXCollections.observableArrayList();
    private final ObservableList<ObnovaGodineDTO> obnovljeneData = FXCollections.observableArrayList();
    private final ObservableList<UplataDTO> uplateData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        log.info("StudentProfileController initialized");
        setupTables();
    }

    private void setupTables() {
        // Položeni predmeti
        colSifra.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSifraPredmeta()));
        colNaziv.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNazivPredmeta()));
        colEspb.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getEspb()).asObject());
        colOcena.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getOcena()).asObject());
        colDatum.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDatumPolaganja()));
        tablePolozeni.setItems(polozeniData);

        // Nepoloženi predmeti
        colSifraNepolozeni.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSifraPredmeta()));
        colNazivNepolozeni.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNazivPredmeta()));
        colEspbNepolozeni.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getEspb()).asObject());
        colBrojPokusaja.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getBrojPokusaja()).asObject());
        tableNepolozeni.setItems(nepolozeniData);

        // Upisane godine
        colGodinaStudija.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getGodinaStudija()).asObject());
        colSkolskaGodina.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSkolskaGodina()));
        colDatumUpisa.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDatumUpisa()));
        colUkupnoESPB.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getUkupnoESPB()).asObject());
        colNapomena.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNapomena()));
        tableUpisaneGodine.setItems(upisaneData);

        // Obnovljene godine
        colGodinaObnovljene.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getGodinaStudija()).asObject());
        colSkolskaGodinaObnova.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSkolskaGodina()));
        colDatumObnove.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDatumObnove()));
        colUkupnoESPBObnova.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getUkupnoESPB()).asObject());
        colNapomenaObnova.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNapomena()));
        tableObnovljeneGodine.setItems(obnovljeneData);

        // Uplate
        colDatumUplate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDatumUplate()));
        colIznosEur.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getIznosEur()).asObject());
        colSrednjiKurs.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getSrednjiKurs()).asObject());
        colIznosRsd.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getIznosRsd()).asObject());
        tableUplate.setItems(uplateData);
    }

    /**
     * Učitaj studenta
     */
    public void loadStudent(Long studentIndeksId) {
        this.currentStudentIndeksId = studentIndeksId;
        log.info("Loading student profile for ID: {}", studentIndeksId);

        studentService.getStudentProfile(studentIndeksId)
                .subscribe(
                        profile -> Platform.runLater(() -> {
                            this.currentStudent = profile;
                            displayStudentInfo(profile);
                            loadPolozeniPredmeti(studentIndeksId);
                            loadNepolozeniPredmeti(studentIndeksId);
                            loadUpisaneGodine(studentIndeksId);
                            loadObnovljeneGodine(studentIndeksId);
                            loadPreostaliIznos(studentIndeksId);
                            // TODO: loadUplate(studentIndeksId);
                        }),
                        error -> Platform.runLater(() -> {
                            log.error("Failed to load student", error);
                            AlertUtil.showException("Greška", (Exception) error);
                        })
                );
    }

    private void displayStudentInfo(StudentProfileDTO student) {
        lblIme.setText(student.getIme());
        lblPrezime.setText(student.getPrezime());
        lblIndeks.setText(student.getGodina() + "/" + student.getBroj() + " - " + student.getStudProgramOznaka());
        lblEmail.setText(student.getEmail());
    }

    private void loadPolozeniPredmeti(Long studentIndeksId) {
        studentService.getPolozeniPredmeti(studentIndeksId, 0, 100)
                .subscribe(
                        page -> Platform.runLater(() -> {
                            polozeniData.clear();
                            polozeniData.addAll(page.getContent());

                            int ukupnoEspb = studentService.calculateTotalESPB(page.getContent());
                            double prosek = studentService.calculateAverageGrade(page.getContent());

                            lblEspb.setText(String.valueOf(ukupnoEspb));
                            lblProsek.setText(String.format("%.2f", prosek));
                        }),
                        error -> log.error("Failed to load polozeni predmeti", error)
                );
    }

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

    private void loadUpisaneGodine(Long studentIndeksId) {
        studentService.getUpisaneGodine(studentIndeksId)
                .subscribe(
                        godine -> Platform.runLater(() -> {
                            upisaneData.clear();
                            upisaneData.addAll(godine);
                        }),
                        error -> log.error("Failed to load upisane godine", error)
                );
    }

    private void loadObnovljeneGodine(Long studentIndeksId) {
        studentService.getObnovljeneGodine(studentIndeksId)
                .subscribe(
                        godine -> Platform.runLater(() -> {
                            obnovljeneData.clear();
                            obnovljeneData.addAll(godine);
                        }),
                        error -> log.error("Failed to load obnovljene godine", error)
                );
    }

    private void loadPreostaliIznos(Long studentIndeksId) {
        studentService.getPreostaliIznos(studentIndeksId)
                .subscribe(
                        iznos -> Platform.runLater(() -> {
                            lblPreostaliIznos.setText(
                                    String.format("%.2f EUR", iznos.getPreostaliIznosEur())
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
     * Upiši godinu
     */
    @FXML
    private void onUpisGodine() {
        if (currentStudentIndeksId == null) {
            AlertUtil.showError("Greška", "Student nije učitan");
            return;
        }

        // TODO: Otvori dijalog za upis godine
        AlertUtil.showInfo("TODO", "Dijalog za upis godine u izradi");
    }

    /**
     * Obnovi godinu
     */
    @FXML
    private void onObnovaGodine() {
        if (currentStudentIndeksId == null) {
            AlertUtil.showError("Greška", "Student nije učitan");
            return;
        }

        // TODO: Otvori dijalog za obnovu godine
        AlertUtil.showInfo("TODO", "Dijalog za obnovu godine u izradi");
    }

    /**
     * Dodaj uplatu
     */
    @FXML
    private void onDodajUplatu() {
        if (currentStudentIndeksId == null) {
            AlertUtil.showError("Greška", "Student nije učitan");
            return;
        }

        // Otvori dijalog za dodavanje uplate
        showUplataDialog();
    }

    /**
     * Dijalog za dodavanje uplate
     */
    private void showUplataDialog() {
        Dialog<UplataRequestDTO> dialog = new Dialog<>();
        dialog.setTitle("Dodaj uplatu");
        dialog.setHeaderText("Unesite podatke o uplati");

        // Buttons
        ButtonType dodajButtonType = new ButtonType("Dodaj", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(dodajButtonType, ButtonType.CANCEL);

        // Form
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField txtIznos = new TextField();
        txtIznos.setPromptText("Unesite iznos (EUR)");
        DatePicker datePicker = new DatePicker(LocalDate.now());

        grid.add(new Label("Datum uplate:"), 0, 0);
        grid.add(datePicker, 1, 0);
        grid.add(new Label("Iznos (EUR):"), 0, 1);
        grid.add(txtIznos, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Converter
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == dodajButtonType) {
                try {
                    double iznos = Double.parseDouble(txtIznos.getText());
                    return new UplataRequestDTO(datePicker.getValue(), iznos);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(request -> {
            if (request != null) {
                dodajUplatu(request);
            } else {
                AlertUtil.showError("Greška", "Unesite validan iznos");
            }
        });
    }

    private void dodajUplatu(UplataRequestDTO request) {
        studentService.dodajUplatu(currentStudentIndeksId, request)
                .subscribe(
                        uplata -> Platform.runLater(() -> {
                            AlertUtil.showInfo("Uspeh", "Uplata je evidentirana");
                            // TODO: Reload uplate
                            loadPreostaliIznos(currentStudentIndeksId);
                        }),
                        error -> Platform.runLater(() -> {
                            log.error("Failed to add uplata", error);
                            AlertUtil.showException("Greška", (Exception) error);
                        })
                );
    }
}