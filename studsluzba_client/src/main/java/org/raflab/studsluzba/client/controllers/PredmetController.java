package org.raflab.studsluzba.client.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.extern.slf4j.Slf4j;
import org.raflab.studsluzba.client.api.response.PredmetResponse;
import org.raflab.studsluzba.client.dto.ProsecnaOcenaDTO;
import org.raflab.studsluzba.client.services.PredmetService;
import org.raflab.studsluzba.client.utils.AlertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PredmetController {

    @FXML
    private TableView<PredmetResponse> tablePredmeti;

    @FXML
    private TableColumn<PredmetResponse, String> colSifra;

    @FXML
    private TableColumn<PredmetResponse, String> colNaziv;

    @FXML
    private TableColumn<PredmetResponse, Integer> colEspb;

    @FXML
    private TableColumn<PredmetResponse, String> colProgram;

    @FXML
    private TextField txtOdGodine;

    @FXML
    private TextField txtDoGodine;

    @FXML
    private Label lblProsecnaOcena;

    @FXML
    private Label lblBrojPolaganja;

    @FXML
    private Button btnPrikaziProsek;

    @FXML
    private Button btnDodajPredmet;

    @Autowired
    private PredmetService predmetService;

    private final ObservableList<PredmetResponse> predmetiData = FXCollections.observableArrayList();

    private PredmetResponse selectedPredmet;

    @FXML
    public void initialize() {
        log.info("PredmetController initialized");

        setupTable();
        loadPredmeti();
    }

    /**
     * Setup tabele
     */
    private void setupTable() {
        colSifra.setCellValueFactory(new PropertyValueFactory<>("sifra"));
        colNaziv.setCellValueFactory(new PropertyValueFactory<>("naziv"));
        colEspb.setCellValueFactory(new PropertyValueFactory<>("espb"));
        colProgram.setCellValueFactory(new PropertyValueFactory<>("studijskiProgramNaziv"));
        tablePredmeti.setItems(predmetiData);

        // Selekcija predmeta
        tablePredmeti.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectedPredmet = newVal;
        });
    }

    /**
     * Učitaj sve predmete
     */
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

    /**
     * Prikaži prosečnu ocenu
     */
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

    /**
     * Dodaj novi predmet
     */
    @FXML
    private void onDodajPredmet() {
        // TODO: Otvori dijalog za dodavanje predmeta
        AlertUtil.showInfo("TODO", "Funkcionalnost u izradi");
    }
}