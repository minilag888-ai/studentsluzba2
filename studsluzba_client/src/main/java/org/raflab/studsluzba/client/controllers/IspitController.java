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
import org.raflab.studsluzba.client.api.request.IspitRequest;
import org.raflab.studsluzba.client.api.request.IspitniRokRequest;
import org.raflab.studsluzba.client.api.response.IspitResponse;
import org.raflab.studsluzba.client.api.response.IspitniRokResponse;
import org.raflab.studsluzba.client.api.response.PredmetResponse;
import org.raflab.studsluzba.client.api.response.RezultatIspitaResponse;
import org.raflab.studsluzba.client.services.IspitService;
import org.raflab.studsluzba.client.services.PredmetService;
import org.raflab.studsluzba.client.services.ReportService;
import org.raflab.studsluzba.client.utils.AlertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Component
public class IspitController {

    @FXML private TableView<IspitResponse> tableIspiti;
    @FXML private TableColumn<IspitResponse, String> colPredmet;
    @FXML private TableColumn<IspitResponse, String> colRok;
    @FXML private TableColumn<IspitResponse, String> colDatum;
    @FXML private TableView<RezultatIspitaResponse> tableRezultati;
    @FXML private TableColumn<RezultatIspitaResponse, String> colIndeks;
    @FXML private TableColumn<RezultatIspitaResponse, String> colIme;
    @FXML private TableColumn<RezultatIspitaResponse, Integer> colPoeni;
    @FXML private TableColumn<RezultatIspitaResponse, Integer> colOcena;
    @FXML private Button btnPrikaziRezultate;
    @FXML private Button btnGenerisiZapisnik;

    @Autowired private IspitService ispitService;
    @Autowired private PredmetService predmetService;
    @Autowired private ReportService reportService;

    private final ObservableList<IspitResponse> ispitiData = FXCollections.observableArrayList();
    private final ObservableList<RezultatIspitaResponse> rezultatiData = FXCollections.observableArrayList();

    private IspitResponse selectedIspit;

    @FXML
    public void initialize() {
        log.info("IspitController initialized");
        setupTables();
        loadIspiti();
    }

    private void setupTables() {
        // Ispiti tabela
        colPredmet.setCellValueFactory(new PropertyValueFactory<>("predmetNaziv"));
        colRok.setCellValueFactory(new PropertyValueFactory<>("ispitniRokNaziv"));
        colDatum.setCellValueFactory(new PropertyValueFactory<>("datumOdrzavanja"));
        tableIspiti.setItems(ispitiData);

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

    // ============================================
    // NOVI DIJALOZI ZA DODAVANJE
    // ============================================

    @FXML
    private void onDodajIspitniRok() {
        showIspitniRokDialog();
    }

    /**
     * Dijalog za dodavanje ispitnog roka
     */
    private void showIspitniRokDialog() {
        Dialog<IspitniRokRequest> dialog = new Dialog<>();
        dialog.setTitle("Dodaj ispitni rok");
        dialog.setHeaderText("Unesite podatke o ispitnom roku");

        ButtonType dodajButtonType = new ButtonType("Dodaj", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(dodajButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField txtNaziv = new TextField();
        txtNaziv.setPromptText("Npr. Januar 2026");

        DatePicker datePocetak = new DatePicker(LocalDate.now());
        DatePicker dateKraj = new DatePicker(LocalDate.now().plusDays(14));

        // TODO: Učitaj školske godine iz API-ja dinamički
        ComboBox<SkolskaGodinaOption> cmbSkolskaGodina = new ComboBox<>();
        cmbSkolskaGodina.getItems().addAll(
                new SkolskaGodinaOption(1L, "2024/2025"),
                new SkolskaGodinaOption(2L, "2025/2026")
        );
        cmbSkolskaGodina.getSelectionModel().selectFirst();

        grid.add(new Label("Naziv:"), 0, 0);
        grid.add(txtNaziv, 1, 0);
        grid.add(new Label("Školska godina:"), 0, 1);
        grid.add(cmbSkolskaGodina, 1, 1);
        grid.add(new Label("Datum početka:"), 0, 2);
        grid.add(datePocetak, 1, 2);
        grid.add(new Label("Datum kraja:"), 0, 3);
        grid.add(dateKraj, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == dodajButtonType) {
                String naziv = txtNaziv.getText().trim();
                LocalDate pocetak = datePocetak.getValue();
                LocalDate kraj = dateKraj.getValue();
                SkolskaGodinaOption skolskaGodina = cmbSkolskaGodina.getValue();

                if (naziv.isEmpty() || pocetak == null || kraj == null || skolskaGodina == null) {
                    Platform.runLater(() ->
                            AlertUtil.showError("Greška", "Sva polja su obavezna!"));
                    return null;
                }

                if (pocetak.isAfter(kraj)) {
                    Platform.runLater(() ->
                            AlertUtil.showError("Greška", "Datum početka ne može biti nakon datuma kraja!"));
                    return null;
                }

                IspitniRokRequest request = new IspitniRokRequest();
                request.setNaziv(naziv);
                request.setSkolskaGodinaId(skolskaGodina.getId());
                request.setPocetak(pocetak);
                request.setKraj(kraj);
                request.setAktivan(true);

                return request;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(request -> {
            if (request != null) {
                dodajIspitniRok(request);
            }
        });
    }

    private void dodajIspitniRok(IspitniRokRequest request) {
        ispitService.addIspitniRok(request)
                .subscribe(
                        response -> Platform.runLater(() -> {
                            AlertUtil.showInfo("Uspeh", "Ispitni rok je dodat: " + response.getNaziv());
                            // Opciono: osveži listu ispita
                            loadIspiti();
                        }),
                        error -> Platform.runLater(() -> {
                            log.error("Failed to add ispitni rok", error);
                            AlertUtil.showException("Greška", (Exception) error);
                        })
                );
    }

    @FXML
    private void onDodajIspit() {
        showIspitDialog();
    }

    /**
     * Dijalog za dodavanje ispita
     */
    private void showIspitDialog() {
        Dialog<IspitRequest> dialog = new Dialog<>();
        dialog.setTitle("Dodaj ispit");
        dialog.setHeaderText("Unesite podatke o ispitu");

        ButtonType dodajButtonType = new ButtonType("Dodaj", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(dodajButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // ComboBox za predmete
        ComboBox<PredmetOption> cmbPredmet = new ComboBox<>();
        cmbPredmet.setPromptText("Izaberite predmet...");
        cmbPredmet.setPrefWidth(250);

        // ComboBox za ispitne rokove
        ComboBox<IspitniRokOption> cmbIspitniRok = new ComboBox<>();
        cmbIspitniRok.setPromptText("Izaberite ispitni rok...");
        cmbIspitniRok.setPrefWidth(250);

        // Datum i vreme
        DatePicker datePicker = new DatePicker(LocalDate.now());

        Spinner<Integer> spinnerSat = new Spinner<>(8, 20, 10);
        Spinner<Integer> spinnerMinut = new Spinner<>(0, 59, 0, 15);
        spinnerSat.setEditable(true);
        spinnerMinut.setEditable(true);

        TextField txtNapomena = new TextField();
        txtNapomena.setPromptText("Opciono...");

        // Učitaj predmete
        predmetService.getAllPredmeti()
                .subscribe(
                        predmeti -> Platform.runLater(() -> {
                            cmbPredmet.getItems().clear();
                            predmeti.forEach(p ->
                                    cmbPredmet.getItems().add(new PredmetOption(p.getId(), p.getSifra(), p.getNaziv()))
                            );
                        }),
                        error -> log.error("Failed to load predmeti", error)
                );

        // Učitaj ispitne rokove
        ispitService.getAllIspitniRokovi()
                .subscribe(
                        rokovi -> Platform.runLater(() -> {
                            cmbIspitniRok.getItems().clear();
                            rokovi.forEach(r ->
                                    cmbIspitniRok.getItems().add(new IspitniRokOption(r.getId(), r.getNaziv(), r.getAktivan()))
                            );
                        }),
                        error -> log.error("Failed to load ispitni rokovi", error)
                );

        grid.add(new Label("Predmet:"), 0, 0);
        grid.add(cmbPredmet, 1, 0);
        grid.add(new Label("Ispitni rok:"), 0, 1);
        grid.add(cmbIspitniRok, 1, 1);
        grid.add(new Label("Datum:"), 0, 2);
        grid.add(datePicker, 1, 2);
        grid.add(new Label("Vreme (sat:minut):"), 0, 3);

        javafx.scene.layout.HBox timeBox = new javafx.scene.layout.HBox(5);
        timeBox.getChildren().addAll(spinnerSat, new Label(":"), spinnerMinut);
        grid.add(timeBox, 1, 3);

        grid.add(new Label("Napomena:"), 0, 4);
        grid.add(txtNapomena, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == dodajButtonType) {
                PredmetOption predmet = cmbPredmet.getValue();
                IspitniRokOption ispitniRok = cmbIspitniRok.getValue();
                LocalDate datum = datePicker.getValue();
                int sat = spinnerSat.getValue();
                int minut = spinnerMinut.getValue();

                if (predmet == null || ispitniRok == null || datum == null) {
                    Platform.runLater(() ->
                            AlertUtil.showError("Greška", "Morate izabrati predmet, ispitni rok i datum!"));
                    return null;
                }

                if (datum.isBefore(LocalDate.now())) {
                    Platform.runLater(() ->
                            AlertUtil.showError("Greška", "Datum održavanja ne može biti u prošlosti!"));
                    return null;
                }

                IspitRequest request = new IspitRequest();
                request.setPredmetId(predmet.getId());
                request.setIspitniRokId(ispitniRok.getId());
                request.setDatumOdrzavanja(datum);
                request.setVremePocetka(LocalDateTime.of(datum, LocalTime.of(sat, minut)));

                String napomena = txtNapomena.getText().trim();
                if (!napomena.isEmpty()) {
                    request.setNapomena(napomena);
                }

                // TODO: drziPredmetId - potrebno je još implementirati ComboBox za nastavnike
                // Za sada stavljamo null ili hardcode vrednost
                request.setDrziPredmetId(1L); // PLACEHOLDER

                return request;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(request -> {
            if (request != null) {
                dodajIspit(request);
            }
        });
    }

    private void dodajIspit(IspitRequest request) {
        ispitService.addIspit(request)
                .subscribe(
                        response -> Platform.runLater(() -> {
                            AlertUtil.showInfo("Uspeh", "Ispit je dodat: " + response.getPredmetNaziv());
                            loadIspiti(); // Osveži listu
                        }),
                        error -> Platform.runLater(() -> {
                            log.error("Failed to add ispit", error);
                            AlertUtil.showException("Greška", (Exception) error);
                        })
                );
    }

    // ============================================
    // HELPER KLASE ZA COMBOBOX
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

    private static class PredmetOption {
        private final Long id;
        private final String sifra;
        private final String naziv;

        public PredmetOption(Long id, String sifra, String naziv) {
            this.id = id;
            this.sifra = sifra;
            this.naziv = naziv;
        }

        public Long getId() { return id; }

        @Override
        public String toString() {
            return sifra + " - " + naziv;
        }
    }

    private static class IspitniRokOption {
        private final Long id;
        private final String naziv;
        private final Boolean aktivan;

        public IspitniRokOption(Long id, String naziv, Boolean aktivan) {
            this.id = id;
            this.naziv = naziv;
            this.aktivan = aktivan;
        }

        public Long getId() { return id; }

        @Override
        public String toString() {
            return naziv + (Boolean.TRUE.equals(aktivan) ? " ✓" : "");
        }
    }
}