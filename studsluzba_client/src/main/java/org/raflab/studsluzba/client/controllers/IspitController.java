package org.raflab.studsluzba.client.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.extern.slf4j.Slf4j;
import org.raflab.studsluzba.client.api.response.IspitResponse;
import org.raflab.studsluzba.client.api.response.PrijavaIspitaResponse;
import org.raflab.studsluzba.client.api.response.RezultatIspitaResponse;
import org.raflab.studsluzba.client.services.IspitService;
import org.raflab.studsluzba.client.services.ReportService;
import org.raflab.studsluzba.client.utils.AlertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class IspitController {

    @FXML
    private TableView<IspitResponse> tableIspiti;

    @FXML
    private TableColumn<IspitResponse, String> colPredmet;

    @FXML
    private TableColumn<IspitResponse, String> colRok;

    @FXML
    private TableColumn<IspitResponse, String> colDatum;

    @FXML
    private TableView<RezultatIspitaResponse> tableRezultati;

    @FXML
    private TableColumn<RezultatIspitaResponse, String> colIndeks;

    @FXML
    private TableColumn<RezultatIspitaResponse, String> colIme;

    @FXML
    private TableColumn<RezultatIspitaResponse, Integer> colPoeni;

    @FXML
    private TableColumn<RezultatIspitaResponse, Integer> colOcena;

    @FXML
    private Button btnPrikaziRezultate;

    @FXML
    private Button btnGenerisiZapisnik;

    @Autowired
    private IspitService ispitService;

    @Autowired
    private ReportService reportService;

    private final ObservableList<IspitResponse> ispitiData = FXCollections.observableArrayList();
    private final ObservableList<RezultatIspitaResponse> rezultatiData = FXCollections.observableArrayList();

    private IspitResponse selectedIspit;

    @FXML
    public void initialize() {
        log.info("IspitController initialized");

        setupTables();
        loadIspiti();
    }

    /**
     * Setup tabela
     */
    private void setupTables() {
        // Ispiti tabela
        colPredmet.setCellValueFactory(new PropertyValueFactory<>("predmetNaziv"));
        colRok.setCellValueFactory(new PropertyValueFactory<>("ispitniRokNaziv"));
        colDatum.setCellValueFactory(new PropertyValueFactory<>("datumOdrzavanja"));
        tableIspiti.setItems(ispitiData);

        // Selekcija ispita
        tableIspiti.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectedIspit = newVal;
        });

        // Rezultati tabela
        colIndeks.setCellValueFactory(new PropertyValueFactory<>("brojIndeksa"));
        colIme.setCellValueFactory(new PropertyValueFactory<>("imeStudenta"));
        colPoeni.setCellValueFactory(new PropertyValueFactory<>("ukupnoPoeni"));
        colOcena.setCellValueFactory(new PropertyValueFactory<>("ocena"));
        tableRezultati.setItems(rezultatiData);
    }

    /**
     * Učitaj sve ispite
     */
    private void loadIspiti() {
        log.info("Loading all ispiti");

        ispitService.getAllIspiti()
                .subscribe(
                        ispiti -> Platform.runLater(() -> {
                            ispitiData.clear();
                            ispitiData.addAll(ispiti);
                            log.info("Loaded {} ispiti", ispiti.size());
                        }),
                        error -> Platform.runLater(() -> {
                            log.error("Failed to load ispiti", error);
                            AlertUtil.showException("Greška", (Exception) error);
                        })
                );
    }

    /**
     * Prikaži rezultate ispita
     */
    @FXML
    private void onPrikaziRezultate() {
        if (selectedIspit == null) {
            AlertUtil.showWarning("Upozorenje", "Izaberite ispit");
            return;
        }

        log.info("Loading rezultati for ispit ID: {}", selectedIspit.getId());

        ispitService.getRezultati(selectedIspit.getId(), "poeni")
                .subscribe(
                        rezultati -> Platform.runLater(() -> {
                            rezultatiData.clear();
                            rezultatiData.addAll(rezultati);
                            log.info("Loaded {} rezultati", rezultati.size());
                        }),
                        error -> Platform.runLater(() -> {
                            log.error("Failed to load rezultati", error);
                            AlertUtil.showException("Greška", (Exception) error);
                        })
                );
    }

    /**
     * Generiši zapisnik sa ispita
     */
    @FXML
    private void onGenerisiZapisnik() {
        if (selectedIspit == null) {
            AlertUtil.showWarning("Upozorenje", "Izaberite ispit");
            return;
        }

        if (rezultatiData.isEmpty()) {
            AlertUtil.showWarning("Upozorenje", "Prvo učitajte rezultate ispita");
            return;
        }

        try {
            reportService.generateZapisnikSaIspita(selectedIspit, rezultatiData);
            AlertUtil.showInfo("Uspeh", "Zapisnik je generisan i otvoren");
        } catch (Exception e) {
            log.error("Failed to generate zapisnik", e);
            AlertUtil.showException("Greška", e);
        }
    }

    /**
     * Dodaj novi ispit
     */
    @FXML
    private void onDodajIspit() {
        // TODO: Otvori dijalog za dodavanje ispita
        AlertUtil.showInfo("TODO", "Funkcionalnost u izradi");
    }

    /**
     * Dodaj ispitni rok
     */
    @FXML
    private void onDodajIspitniRok() {
        // TODO: Otvori dijalog za dodavanje ispitnog roka
        AlertUtil.showInfo("TODO", "Funkcionalnost u izradi");
    }
}