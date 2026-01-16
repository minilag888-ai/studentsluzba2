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
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;
import org.raflab.studsluzba.client.dto.*;
import org.raflab.studsluzba.client.services.PredmetService;
import org.raflab.studsluzba.client.services.ReportService;
import org.raflab.studsluzba.client.services.StudentService;
import org.raflab.studsluzba.client.utils.AlertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired private PredmetService predmetService;
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

    public void loadStudent(Long studentIndeksId) {
        this.currentStudentIndeksId = studentIndeksId;
        log.info("Loading student profile for ID: {}", studentIndeksId);

        studentService.getStudentProfile(studentIndeksId)
                .subscribe(
                        profile -> Platform.runLater(() -> {
                            this.currentStudent = profile;
                            displayStudentInfo(profile);
                            loadAllData();
                        }),
                        error -> Platform.runLater(() -> {
                            log.error("Failed to load student", error);
                            AlertUtil.showException("Greška", (Exception) error);
                        })
                );
    }

    /**
     * ✅ Učitaj SVE podatke odjednom
     */
    private void loadAllData() {
        loadPolozeniPredmeti(currentStudentIndeksId);
        loadNepolozeniPredmeti(currentStudentIndeksId);
        loadUpisaneGodine(currentStudentIndeksId);
        loadObnovljeneGodine(currentStudentIndeksId);
        loadPreostaliIznos(currentStudentIndeksId);
        loadUplate(currentStudentIndeksId);
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
        log.info("=== LOADING UPISANE GODINE for student {} ===", studentIndeksId);

        studentService.getUpisaneGodine(studentIndeksId)
                .subscribe(
                        godine -> Platform.runLater(() -> {
                            log.info("✅ RECEIVED {} upisane godine from backend", godine.size());

                            if (godine.isEmpty()) {
                                log.warn("⚠️ Backend returned EMPTY list for upisane godine!");
                            } else {
                                for (UpisGodineDTO g : godine) {
                                    log.info("  - Godina: {}, ESPB: {}, Skolska: {}",
                                            g.getGodinaStudija(), g.getUkupnoESPB(), g.getSkolskaGodina());
                                }
                            }

                            upisaneData.clear();
                            upisaneData.addAll(godine);
                            tableUpisaneGodine.refresh();

                            log.info("✅ TableView now has {} items", upisaneData.size());
                        }),
                        error -> {
                            log.error("❌ FAILED to load upisane godine", error);
                            Platform.runLater(() ->
                                    AlertUtil.showError("Greška", "Nije moguće učitati upisane godine: " + error.getMessage())
                            );
                        }
                );
    }

    private void loadObnovljeneGodine(Long studentIndeksId) {
        log.info("=== LOADING OBNOVLJENE GODINE for student {} ===", studentIndeksId);

        studentService.getObnovljeneGodine(studentIndeksId)
                .subscribe(
                        godine -> Platform.runLater(() -> {
                            log.info("✅ RECEIVED {} obnovljene godine from backend", godine.size());

                            obnovljeneData.clear();
                            obnovljeneData.addAll(godine);
                            tableObnovljeneGodine.refresh();

                            log.info("✅ TableView now has {} items", obnovljeneData.size());
                        }),
                        error -> {
                            log.error("❌ FAILED to load obnovljene godine", error);
                            Platform.runLater(() ->
                                    AlertUtil.showError("Greška", "Nije moguće učitati obnovljene godine: " + error.getMessage())
                            );
                        }
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
     * ✅ DODATO: Učitavanje uplata
     */
    private void loadUplate(Long studentIndeksId) {
        log.info("=== LOADING UPLATE for student {} ===", studentIndeksId);

        // TODO: Implement API call kada backend bude spreman
        // Za sada samo log
        log.info("⚠️ API endpoint za uplate nije implementiran - tableUplate ostaje prazan");

        // Placeholder:
        // studentService.getUplate(studentIndeksId)
        //     .subscribe(
        //         uplate -> Platform.runLater(() -> {
        //             uplateData.clear();
        //             uplateData.addAll(uplate);
        //
        //             double ukupno = uplate.stream()
        //                     .mapToDouble(UplataDTO::getIznosEur)
        //                     .sum();
        //             lblUkupnoUplaceno.setText(String.format("%.2f EUR", ukupno));
        //         }),
        //         error -> log.error("Failed to load uplate", error)
        //     );
    }

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

    @FXML
    private void onGenerateUverenjeOPolozenim() {
        if (currentStudent == null) {
            AlertUtil.showError("Greška", "Student nije učitan");
            return;
        }

        try {
            List<PolozenPredmetDTO> polozeni = new ArrayList<>(polozeniData);
            reportService.generateUverenjeOPolozenimIspitima(currentStudent, polozeni);
            AlertUtil.showInfo("Uspeh", "Uverenje je generisano i otvoreno");
        } catch (Exception e) {
            log.error("Failed to generate uverenje", e);
            AlertUtil.showException("Greška", e);
        }
    }

    // ============================================
    // UPIS I OBNOVA GODINE
    // ============================================

    @FXML
    private void onUpisGodine() {
        if (currentStudentIndeksId == null) {
            AlertUtil.showError("Greška", "Student nije učitan");
            return;
        }

        showUpisGodineDialog();
    }

    private void showUpisGodineDialog() {
        Dialog<UpisGodineRequestDTO> dialog = new Dialog<>();
        dialog.setTitle("Upiši godinu studija");
        dialog.setHeaderText("Izaberite godinu i predmete");

        ButtonType upisButtonType = new ButtonType("Upiši", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(upisButtonType, ButtonType.CANCEL);

        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));

        // Godina studija
        HBox godinaBox = new HBox(10);
        Label lblGodina = new Label("Godina studija:");
        lblGodina.setMinWidth(120);
        Spinner<Integer> spinnerGodina = new Spinner<>(1, 4, 1);
        spinnerGodina.setEditable(true);
        spinnerGodina.setPrefWidth(80);
        godinaBox.getChildren().addAll(lblGodina, spinnerGodina);

        // Školska godina
        HBox skolskaGodinaBox = new HBox(10);
        Label lblSkolskaGodina = new Label("Školska godina:");
        lblSkolskaGodina.setMinWidth(120);
        ComboBox<SkolskaGodinaOption> cmbSkolskaGodina = new ComboBox<>();
        cmbSkolskaGodina.getItems().addAll(
                new SkolskaGodinaOption(1L, "2024/2025"),
                new SkolskaGodinaOption(2L, "2025/2026")
        );
        cmbSkolskaGodina.getSelectionModel().selectFirst();
        cmbSkolskaGodina.setPrefWidth(200);
        skolskaGodinaBox.getChildren().addAll(lblSkolskaGodina, cmbSkolskaGodina);

        // Datum upisa
        HBox datumBox = new HBox(10);
        Label lblDatum = new Label("Datum upisa:");
        lblDatum.setMinWidth(120);
        DatePicker datePicker = new DatePicker(LocalDate.now());
        datePicker.setPrefWidth(200);
        datumBox.getChildren().addAll(lblDatum, datePicker);

        // Napomena
        HBox napomenaBox = new HBox(10);
        Label lblNapomena = new Label("Napomena:");
        lblNapomena.setMinWidth(120);
        TextField txtNapomena = new TextField();
        txtNapomena.setPromptText("Opciono...");
        txtNapomena.setPrefWidth(300);
        napomenaBox.getChildren().addAll(lblNapomena, txtNapomena);

        // Lista predmeta
        Label lblPredmeti = new Label("Izaberite predmete:");

        ObservableList<PredmetCheckItem> predmetItems = FXCollections.observableArrayList();

        ListView<PredmetCheckItem> listPredmeti = new ListView<>(predmetItems);
        listPredmeti.setPrefHeight(250);
        listPredmeti.setCellFactory(CheckBoxListCell.forListView(PredmetCheckItem::selectedProperty));

        // Label za prikaz ukupnog ESPB
        Label lblUkupnoESPB = new Label("Ukupno ESPB: 0");
        lblUkupnoESPB.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        // Učitaj predmete
        Long studijskiProgramId = 1L; // TODO: Dinamički

        predmetService.getPredmetiNaStudijskomProgramu(studijskiProgramId)
                .subscribe(
                        predmeti -> Platform.runLater(() -> {
                            predmetItems.clear();
                            for (PredmetDTO p : predmeti) {
                                PredmetCheckItem item = new PredmetCheckItem(p);

                                item.selectedProperty().addListener((obs, oldVal, newVal) -> {
                                    int ukupnoESPB = predmetItems.stream()
                                            .filter(PredmetCheckItem::isSelected)
                                            .mapToInt(pci -> pci.getPredmet().getEspb())
                                            .sum();
                                    lblUkupnoESPB.setText("Ukupno ESPB: " + ukupnoESPB);
                                });

                                predmetItems.add(item);
                            }
                        }),
                        error -> {
                            log.error("Failed to load predmeti", error);
                            Platform.runLater(() ->
                                    AlertUtil.showError("Greška", "Nije moguće učitati predmete: " + error.getMessage())
                            );
                        }
                );

        vbox.getChildren().addAll(
                godinaBox, skolskaGodinaBox, datumBox, napomenaBox,
                new Separator(),
                lblPredmeti, listPredmeti, lblUkupnoESPB
        );

        ScrollPane scrollPane = new ScrollPane(vbox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(500);

        dialog.getDialogPane().setContent(scrollPane);
        dialog.getDialogPane().setPrefWidth(550);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == upisButtonType) {
                Integer godinaStudija = spinnerGodina.getValue();
                SkolskaGodinaOption skolskaGodina = cmbSkolskaGodina.getValue();
                LocalDate datumUpisa = datePicker.getValue();
                String napomena = txtNapomena.getText().trim();

                List<Long> predmetIds = predmetItems.stream()
                        .filter(PredmetCheckItem::isSelected)
                        .map(item -> item.getPredmet().getId())
                        .collect(Collectors.toList());

                if (skolskaGodina == null || datumUpisa == null || predmetIds.isEmpty()) {
                    Platform.runLater(() ->
                            AlertUtil.showError("Greška", "Morate izabrati školsku godinu, datum i bar jedan predmet!"));
                    return null;
                }

                UpisGodineRequestDTO request = new UpisGodineRequestDTO();
                request.setSkolskaGodinaId(skolskaGodina.getId());
                request.setGodinaStudija(godinaStudija);
                request.setDatumUpisa(datumUpisa);
                request.setNapomena(napomena.isEmpty() ? null : napomena);
                request.setPredmetIds(predmetIds);

                return request;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(request -> {
            if (request != null) {
                izvršiUpisGodine(request);
            }
        });
    }

    private void izvršiUpisGodine(UpisGodineRequestDTO request) {
        log.info("=== IZVRŠAVAM UPIS GODINE ===");
        log.info("Request: godina={}, skolskaGodinaId={}, predmeti={}",
                request.getGodinaStudija(), request.getSkolskaGodinaId(), request.getPredmetIds().size());

        studentService.upisNaGodinu(currentStudentIndeksId, request)
                .subscribe(
                        response -> Platform.runLater(() -> {
                            log.info("✅ UPIS USPEŠAN!");
                            log.info("Response: godina={}, ESPB={}, skolska={}",
                                    response.getGodinaStudija(), response.getUkupnoESPB(), response.getSkolskaGodina());

                            AlertUtil.showInfo("Uspeh", "Student je uspešno upisan na " + response.getGodinaStudija() + ". godinu!");

                            // ✅ KLJUČNO: ČEKAJ 500ms pa osveži
                            new Thread(() -> {
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Platform.runLater(() -> {
                                    log.info("🔄 Refreshing upisane godine...");
                                    loadUpisaneGodine(currentStudentIndeksId);
                                    loadNepolozeniPredmeti(currentStudentIndeksId);
                                });
                            }).start();
                        }),
                        error -> Platform.runLater(() -> {
                            log.error("❌ UPIS FAILED", error);
                            AlertUtil.showException("Greška", (Exception) error);
                        })
                );
    }

    @FXML
    private void onObnovaGodine() {
        if (currentStudentIndeksId == null) {
            AlertUtil.showError("Greška", "Student nije učitan");
            return;
        }

        if (upisaneData.isEmpty()) {
            AlertUtil.showWarning("Upozorenje", "Student nema upisanih godina koje mogu biti obnovljene");
            return;
        }

        showObnovaGodineDialog();
    }

    private void showObnovaGodineDialog() {
        Dialog<ObnovaGodineRequestDTO> dialog = new Dialog<>();
        dialog.setTitle("Obnovi godinu studija");
        dialog.setHeaderText("Izaberite godinu koju želite da obnovite i predmete (max 60 ESPB)");

        ButtonType obnoviButtonType = new ButtonType("Obnovi", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(obnoviButtonType, ButtonType.CANCEL);

        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));

        // Izaberi godinu koju obnavljaš
        HBox upisanaGodinaBox = new HBox(10);
        Label lblUpisanaGodina = new Label("Obnovi godinu:");
        lblUpisanaGodina.setMinWidth(120);
        ComboBox<UpisGodineDTO> cmbUpisanaGodina = new ComboBox<>();
        cmbUpisanaGodina.getItems().addAll(upisaneData);
        cmbUpisanaGodina.setConverter(new javafx.util.StringConverter<UpisGodineDTO>() {
            @Override
            public String toString(UpisGodineDTO upis) {
                if (upis == null) return "";
                return upis.getGodinaStudija() + ". godina - " + upis.getSkolskaGodina();
            }

            @Override
            public UpisGodineDTO fromString(String string) {
                return null;
            }
        });
        cmbUpisanaGodina.setPrefWidth(300);
        upisanaGodinaBox.getChildren().addAll(lblUpisanaGodina, cmbUpisanaGodina);

        // Školska godina
        HBox skolskaGodinaBox = new HBox(10);
        Label lblSkolskaGodina = new Label("Školska godina:");
        lblSkolskaGodina.setMinWidth(120);
        ComboBox<SkolskaGodinaOption> cmbSkolskaGodina = new ComboBox<>();
        cmbSkolskaGodina.getItems().addAll(
                new SkolskaGodinaOption(1L, "2024/2025"),
                new SkolskaGodinaOption(2L, "2025/2026")
        );
        cmbSkolskaGodina.getSelectionModel().selectFirst();
        cmbSkolskaGodina.setPrefWidth(200);
        skolskaGodinaBox.getChildren().addAll(lblSkolskaGodina, cmbSkolskaGodina);

        // Datum obnove
        HBox datumBox = new HBox(10);
        Label lblDatum = new Label("Datum obnove:");
        lblDatum.setMinWidth(120);
        DatePicker datePicker = new DatePicker(LocalDate.now());
        datePicker.setPrefWidth(200);
        datumBox.getChildren().addAll(lblDatum, datePicker);

        // Napomena
        HBox napomenaBox = new HBox(10);
        Label lblNapomena = new Label("Napomena:");
        lblNapomena.setMinWidth(120);
        TextField txtNapomena = new TextField();
        txtNapomena.setPromptText("Opciono...");
        txtNapomena.setPrefWidth(300);
        napomenaBox.getChildren().addAll(lblNapomena, txtNapomena);

        // Lista predmeta
        Label lblPredmeti = new Label("Izaberite predmete (max 60 ESPB):");

        ObservableList<PredmetCheckItem> predmetItems = FXCollections.observableArrayList();

        ListView<PredmetCheckItem> listPredmeti = new ListView<>(predmetItems);
        listPredmeti.setPrefHeight(250);
        listPredmeti.setCellFactory(CheckBoxListCell.forListView(PredmetCheckItem::selectedProperty));

        // Label za prikaz ukupnog ESPB
        Label lblUkupnoESPB = new Label("Ukupno ESPB: 0 / 60");
        lblUkupnoESPB.setStyle("-fx-text-fill: green; -fx-font-size: 14px; -fx-font-weight: bold;");

        Long studijskiProgramId = 1L; // TODO: Dinamički

        predmetService.getPredmetiNaStudijskomProgramu(studijskiProgramId)
                .subscribe(
                        predmeti -> Platform.runLater(() -> {
                            predmetItems.clear();
                            for (PredmetDTO p : predmeti) {
                                PredmetCheckItem item = new PredmetCheckItem(p);

                                item.selectedProperty().addListener((obs, oldVal, newVal) -> {
                                    int ukupnoESPB = predmetItems.stream()
                                            .filter(PredmetCheckItem::isSelected)
                                            .mapToInt(pci -> pci.getPredmet().getEspb())
                                            .sum();

                                    lblUkupnoESPB.setText("Ukupno ESPB: " + ukupnoESPB + " / 60");

                                    if (ukupnoESPB > 60) {
                                        lblUkupnoESPB.setStyle("-fx-text-fill: red; -fx-font-size: 14px; -fx-font-weight: bold;");
                                    } else {
                                        lblUkupnoESPB.setStyle("-fx-text-fill: green; -fx-font-size: 14px; -fx-font-weight: bold;");
                                    }
                                });

                                predmetItems.add(item);
                            }
                        }),
                        error -> {
                            log.error("Failed to load predmeti", error);
                            Platform.runLater(() ->
                                    AlertUtil.showError("Greška", "Nije moguće učitati predmete: " + error.getMessage())
                            );
                        }
                );

        vbox.getChildren().addAll(
                upisanaGodinaBox,
                skolskaGodinaBox, datumBox, napomenaBox,
                new Separator(),
                lblPredmeti, listPredmeti, lblUkupnoESPB
        );

        ScrollPane scrollPane = new ScrollPane(vbox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(550);

        dialog.getDialogPane().setContent(scrollPane);
        dialog.getDialogPane().setPrefWidth(550);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == obnoviButtonType) {
                UpisGodineDTO upisanaGodina = cmbUpisanaGodina.getValue();
                SkolskaGodinaOption skolskaGodina = cmbSkolskaGodina.getValue();
                LocalDate datumObnove = datePicker.getValue();
                String napomena = txtNapomena.getText().trim();

                if (upisanaGodina == null) {
                    Platform.runLater(() ->
                            AlertUtil.showError("Greška", "Morate izabrati godinu koju obnavljate!"));
                    return null;
                }

                List<Long> predmetIds = predmetItems.stream()
                        .filter(PredmetCheckItem::isSelected)
                        .map(item -> item.getPredmet().getId())
                        .collect(Collectors.toList());

                if (skolskaGodina == null || datumObnove == null || predmetIds.isEmpty()) {
                    Platform.runLater(() ->
                            AlertUtil.showError("Greška", "Morate izabrati školsku godinu, datum i bar jedan predmet!"));
                    return null;
                }

                int ukupnoESPB = predmetItems.stream()
                        .filter(PredmetCheckItem::isSelected)
                        .mapToInt(item -> item.getPredmet().getEspb())
                        .sum();

                if (ukupnoESPB > 60) {
                    Platform.runLater(() ->
                            AlertUtil.showError("Greška", "Ukupan broj ESPB bodova ne može biti veći od 60!"));
                    return null;
                }

                ObnovaGodineRequestDTO request = new ObnovaGodineRequestDTO();
                request.setSkolskaGodinaId(skolskaGodina.getId());
                request.setGodinaStudija(upisanaGodina.getGodinaStudija());
                request.setDatumObnove(datumObnove);
                request.setNapomena(napomena.isEmpty() ? null : napomena);
                request.setPredmetIds(predmetIds);

                return request;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(request -> {
            if (request != null) {
                izvršiObnovuGodine(request);
            }
        });
    }

    private void izvršiObnovuGodine(ObnovaGodineRequestDTO request) {
        log.info("=== IZVRŠAVAM OBNOVU GODINE ===");
        log.info("Request: godina={}, skolskaGodinaId={}, predmeti={}",
                request.getGodinaStudija(), request.getSkolskaGodinaId(), request.getPredmetIds().size());

        studentService.obnovaGodine(currentStudentIndeksId, request)
                .subscribe(
                        response -> Platform.runLater(() -> {
                            log.info("✅ OBNOVA USPEŠNA!");
                            log.info("Response: godina={}, ESPB={}, skolska={}",
                                    response.getGodinaStudija(), response.getUkupnoESPB(), response.getSkolskaGodina());

                            AlertUtil.showInfo("Uspeh", "Uspešno obnovljena " + response.getGodinaStudija() + ". godina!");

                            // ✅ KLJUČNO: ČEKAJ 500ms pa osveži
                            new Thread(() -> {
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Platform.runLater(() -> {
                                    log.info("🔄 Refreshing obnovljene godine...");
                                    loadObnovljeneGodine(currentStudentIndeksId);
                                    loadNepolozeniPredmeti(currentStudentIndeksId);
                                });
                            }).start();
                        }),
                        error -> Platform.runLater(() -> {
                            log.error("❌ OBNOVA FAILED", error);
                            AlertUtil.showException("Greška", (Exception) error);
                        })
                );
    }

    @FXML
    private void onDodajUplatu() {
        if (currentStudentIndeksId == null) {
            AlertUtil.showError("Greška", "Student nije učitan");
            return;
        }

        showUplataDialog();
    }

    private void showUplataDialog() {
        Dialog<UplataRequestDTO> dialog = new Dialog<>();
        dialog.setTitle("Dodaj uplatu");
        dialog.setHeaderText("Unesite podatke o uplati");

        ButtonType dodajButtonType = new ButtonType("Dodaj", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(dodajButtonType, ButtonType.CANCEL);

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
        log.info("=== DODAJEM UPLATU ===");
        log.info("Request: datum={}, iznos={} EUR", request.getDatumUplate(), request.getIznosEur());

        studentService.dodajUplatu(currentStudentIndeksId, request)
                .subscribe(
                        uplata -> Platform.runLater(() -> {
                            log.info("✅ UPLATA EVIDENTIRANA!");
                            log.info("Response: iznos={} EUR, kurs={}, RSD={}",
                                    uplata.getIznosEur(), uplata.getSrednjiKurs(), uplata.getIznosRsd());

                            AlertUtil.showInfo("Uspeh", "Uplata je evidentirana");

                            // ✅ OSVEŽI preostali iznos i listu uplata
                            new Thread(() -> {
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Platform.runLater(() -> {
                                    log.info("🔄 Refreshing preostali iznos i uplate...");
                                    loadPreostaliIznos(currentStudentIndeksId);
                                    loadUplate(currentStudentIndeksId);
                                });
                            }).start();
                        }),
                        error -> Platform.runLater(() -> {
                            log.error("❌ UPLATA FAILED", error);
                            AlertUtil.showException("Greška", (Exception) error);
                        })
                );
    }

    // ============================================
    // HELPER KLASE
    // ============================================

    private static class SkolskaGodinaOption {
        private final Long id;
        private final String naziv;

        public SkolskaGodinaOption(Long id, String naziv) {
            this.id = id;
            this.naziv = naziv;
        }

        public Long getId() { return id; }
        public String getNaziv() { return naziv; }

        @Override
        public String toString() { return naziv; }
    }

    private static class PredmetCheckItem {
        private final PredmetDTO predmet;
        private final javafx.beans.property.BooleanProperty selected;

        public PredmetCheckItem(PredmetDTO predmet) {
            this.predmet = predmet;
            this.selected = new javafx.beans.property.SimpleBooleanProperty(false);
        }

        public PredmetDTO getPredmet() {
            return predmet;
        }

        public javafx.beans.property.BooleanProperty selectedProperty() {
            return selected;
        }

        public boolean isSelected() {
            return selected.get();
        }

        public void setSelected(boolean selected) {
            this.selected.set(selected);
        }

        @Override
        public String toString() {
            return predmet.getSifra() + " - " + predmet.getNaziv() + " (" + predmet.getEspb() + " ESPB)";
        }
    }
}