package org.raflab.studsluzba.client.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import lombok.extern.slf4j.Slf4j;
import org.raflab.studsluzba.client.api.request.PredmetRequest;
import org.raflab.studsluzba.client.api.response.PredmetResponse;
import org.raflab.studsluzba.client.dto.ProsecnaOcenaDTO;
import org.raflab.studsluzba.client.services.PredmetService;
import org.raflab.studsluzba.client.utils.AlertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PredmetController {

    @FXML private TableView<PredmetResponse> tablePredmeti;
    @FXML private TableColumn<PredmetResponse, String> colSifra;
    @FXML private TableColumn<PredmetResponse, String> colNaziv;
    @FXML private TableColumn<PredmetResponse, Integer> colEspb;
    @FXML private TableColumn<PredmetResponse, String> colProgram;
    @FXML private TextField txtOdGodine;
    @FXML private TextField txtDoGodine;
    @FXML private Label lblProsecnaOcena;
    @FXML private Label lblBrojPolaganja;
    @FXML private Button btnPrikaziProsek;
    @FXML private Button btnDodajPredmet;

    @Autowired private PredmetService predmetService;

    private final ObservableList<PredmetResponse> predmetiData = FXCollections.observableArrayList();
    private PredmetResponse selectedPredmet;

    @FXML
    public void initialize() {
        log.info("PredmetController initialized");
        setupTable();
        loadPredmeti();
    }

    private void setupTable() {
        colSifra.setCellValueFactory(new PropertyValueFactory<>("sifra"));
        colNaziv.setCellValueFactory(new PropertyValueFactory<>("naziv"));
        colEspb.setCellValueFactory(new PropertyValueFactory<>("espb"));
        colProgram.setCellValueFactory(new PropertyValueFactory<>("studijskiProgramNaziv"));
        tablePredmeti.setItems(predmetiData);

        tablePredmeti.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectedPredmet = newVal;
        });
    }

    private void loadPredmeti() {
        log.info("Loading all predmeti");
        predmetService.getAllPredmeti()
                .subscribe(
                        predmeti -> Platform.runLater(() -> {
                            predmetiData.clear();
                            predmetiData.addAll(predmeti);
                            log.info("Loaded {} predmeti", predmeti.size());
                        }),
                        error -> Platform.runLater(() -> {
                            log.error("Failed to load predmeti", error);
                            AlertUtil.showException("Greška", (Exception) error);
                        })
                );
    }

    @FXML
    private void onPrikaziProsek() {
        if (selectedPredmet == null) {
            AlertUtil.showWarning("Upozorenje", "Izaberite predmet");
            return;
        }

        String odGodineStr = txtOdGodine.getText().trim();
        String doGodineStr = txtDoGodine.getText().trim();

        if (odGodineStr.isEmpty() || doGodineStr.isEmpty()) {
            AlertUtil.showWarning("Upozorenje", "Unesite period (od godine - do godine)");
            return;
        }

        try {
            int odGodine = Integer.parseInt(odGodineStr);
            int doGodine = Integer.parseInt(doGodineStr);

            log.info("Calculating prosek for predmet {}, period {}-{}", selectedPredmet.getId(), odGodine, doGodine);

            predmetService.getProsecnaOcena(selectedPredmet.getId(), odGodine, doGodine)
                    .subscribe(
                            prosek -> Platform.runLater(() -> {
                                lblProsecnaOcena.setText(String.format("%.2f", prosek.getProsecnaOcena()));
                                lblBrojPolaganja.setText(String.valueOf(prosek.getBrojPolaganja()));
                            }),
                            error -> Platform.runLater(() -> {
                                log.error("Failed to calculate prosek", error);
                                AlertUtil.showException("Greška", (Exception) error);
                            })
                    );

        } catch (NumberFormatException e) {
            AlertUtil.showError("Greška", "Godine moraju biti brojevi");
        }
    }

    // ============================================
    // NOVI DIJALOG ZA DODAVANJE PREDMETA
    // ============================================

    @FXML
    private void onDodajPredmet() {
        showDodajPredmetDialog();
    }

    /**
     * Dijalog za dodavanje novog predmeta
     */
    private void showDodajPredmetDialog() {
        Dialog<PredmetRequest> dialog = new Dialog<>();
        dialog.setTitle("Dodaj novi predmet");
        dialog.setHeaderText("Unesite podatke o predmetu");

        ButtonType dodajButtonType = new ButtonType("Dodaj", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(dodajButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField txtSifra = new TextField();
        txtSifra.setPromptText("Npr. UVIT101");

        TextField txtNaziv = new TextField();
        txtNaziv.setPromptText("Naziv predmeta");
        txtNaziv.setPrefWidth(300);

        TextArea txtOpis = new TextArea();
        txtOpis.setPromptText("Opis predmeta (opciono)");
        txtOpis.setPrefRowCount(3);
        txtOpis.setPrefWidth(300);

        Spinner<Integer> spinnerEspb = new Spinner<>(1, 12, 6);
        spinnerEspb.setEditable(true);

        CheckBox chkObavezan = new CheckBox("Obavezan");
        chkObavezan.setSelected(true);


        ComboBox<StudijskiProgramOption> cmbStudijskiProgram = new ComboBox<>();
        cmbStudijskiProgram.getItems().addAll(
                new StudijskiProgramOption(1L, "Studijski program 1"),
                new StudijskiProgramOption(2L, "Studijski program 2"),
                new StudijskiProgramOption(3L, "Studijski program 3"),
                new StudijskiProgramOption(4L, "Studijski program 4"),
                new StudijskiProgramOption(5L, "Studijski program 5")
        );
        cmbStudijskiProgram.getSelectionModel().selectFirst();
        cmbStudijskiProgram.setPrefWidth(300);

        grid.add(new Label("Šifra:"), 0, 0);
        grid.add(txtSifra, 1, 0);
        grid.add(new Label("Naziv:"), 0, 1);
        grid.add(txtNaziv, 1, 1);
        grid.add(new Label("Opis:"), 0, 2);
        grid.add(txtOpis, 1, 2);
        grid.add(new Label("ESPB bodovi:"), 0, 3);
        grid.add(spinnerEspb, 1, 3);
        grid.add(new Label("Studijski program:"), 0, 4);
        grid.add(cmbStudijskiProgram, 1, 4);
        grid.add(new Label("Status:"), 0, 5);
        grid.add(chkObavezan, 1, 5);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == dodajButtonType) {
                String sifra = txtSifra.getText().trim();
                String naziv = txtNaziv.getText().trim();
                String opis = txtOpis.getText().trim();
                int espb = spinnerEspb.getValue();
                boolean obavezan = chkObavezan.isSelected();
                StudijskiProgramOption program = cmbStudijskiProgram.getValue();

                if (sifra.isEmpty() || naziv.isEmpty() || program == null) {
                    Platform.runLater(() ->
                            AlertUtil.showError("Greška", "Šifra, naziv i studijski program su obavezni!"));
                    return null;
                }

                PredmetRequest request = new PredmetRequest();
                request.setSifra(sifra);
                request.setNaziv(naziv);
                request.setOpis(opis.isEmpty() ? null : opis);
                request.setEspb(espb);
                request.setObavezan(obavezan);
                request.setStudijskiProgramId(program.getId());

                return request;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(request -> {
            if (request != null) {
                dodajPredmet(request);
            }
        });
    }

    private void dodajPredmet(PredmetRequest request) {
        predmetService.addPredmet(request)
                .subscribe(
                        response -> Platform.runLater(() -> {
                            AlertUtil.showInfo("Uspeh", "Predmet je dodat: " + response.getSifra() + " - " + response.getNaziv());
                            loadPredmeti(); // Osveži listu
                        }),
                        error -> Platform.runLater(() -> {
                            log.error("Failed to add predmet", error);
                            AlertUtil.showException("Greška", (Exception) error);
                        })
                );
    }

    // ============================================
    // HELPER KLASA ZA COMBOBOX
    // ============================================

    private static class StudijskiProgramOption {
        private final Long id;
        private final String naziv;

        public StudijskiProgramOption(Long id, String naziv) {
            this.id = id;
            this.naziv = naziv;
        }

        public Long getId() { return id; }
        public String getNaziv() { return naziv; }

        @Override
        public String toString() {
            return naziv;
        }
    }
}