package org.raflab.studsluzba.client.controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import lombok.extern.slf4j.Slf4j;
import org.raflab.studsluzba.shared.dtos.StudentPodaciDTO;
import org.raflab.studsluzba.client.navigation.NavigationManager;
import org.raflab.studsluzba.client.services.StudentService;
import org.raflab.studsluzba.client.utils.AlertUtil;
import org.raflab.studsluzba.client.utils.FxmlLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StudentSearchController {

    @FXML private TextField txtIme;
    @FXML private TextField txtPrezime;
    @FXML private TextField txtGodina;
    @FXML private TextField txtBroj;
    @FXML private TextField txtOznaka;
    @FXML private ComboBox<SrednjaSkola> cmbSrednjaSkola;

    // ✅ Koristi StudentPodaciDTO jer to backend vraća
    @FXML private TableView<StudentPodaciDTO> tableStudents;
    @FXML private TableColumn<StudentPodaciDTO, String> colIme;
    @FXML private TableColumn<StudentPodaciDTO, String> colPrezime;
    @FXML private TableColumn<StudentPodaciDTO, String> colEmail;
    @FXML private TableColumn<StudentPodaciDTO, String> colJmbg;
    @FXML private TableColumn<StudentPodaciDTO, String> colSrednjaSkola;
    @FXML private Pagination pagination;

    @Autowired private StudentService studentService;
    @Autowired private NavigationManager navigationManager;
    @Autowired private FxmlLoader fxmlLoader;

    // ✅ Koristi StudentPodaciDTO
    private final ObservableList<StudentPodaciDTO> studentData = FXCollections.observableArrayList();
    private int currentPage = 0;
    private final int pageSize = 20;

    @FXML
    public void initialize() {
        log.info("StudentSearchController initialized");
        setupTable();
        setupPagination();
        loadSrednjeSkole();
    }

    private void setupTable() {
        // ✅ Ispravno mapiranje kolona na StudentPodaciDTO polja
        colIme.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIme()));
        colPrezime.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPrezime()));
        colEmail.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
        colJmbg.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getJmbg()));
        colSrednjaSkola.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getSrednjaSkolaNaziv() != null ?
                        data.getValue().getSrednjaSkolaNaziv() : "N/A")
        );

        tableStudents.setItems(studentData);

        // Double-click za otvaranje profila
        tableStudents.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                StudentPodaciDTO selected = tableStudents.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    // ✅ Koristi getId() jer StudentPodaciDTO ima 'id' polje
                    openStudentProfile(selected.getId());
                }
            }
        });
    }

    private void setupPagination() {
        pagination.setPageCount(1);
        pagination.setCurrentPageIndex(0);
        pagination.currentPageIndexProperty().addListener((obs, oldPage, newPage) -> {
            currentPage = newPage.intValue();
            String ime = txtIme.getText().trim();
            String prezime = txtPrezime.getText().trim();
            if (!ime.isEmpty() || !prezime.isEmpty()) {
                searchByName();
            }
        });
    }

    private void loadSrednjeSkole() {
        ObservableList<SrednjaSkola> skole = FXCollections.observableArrayList(
                new SrednjaSkola(1L, "Srednja škola 1"),
                new SrednjaSkola(2L, "Srednja škola 2"),
                new SrednjaSkola(3L, "Srednja škola 3"),
                new SrednjaSkola(4L, "Srednja škola 4")
        );
        cmbSrednjaSkola.setItems(skole);
    }

    @FXML
    private void onRefreshSkole() {
        loadSrednjeSkole();
        AlertUtil.showInfo("Osveženo", "Lista srednjih škola je ažurirana");
    }

    /**
     * 1. NAČIN: Pretraga po broju indeksa - PRIKAZUJE LISTU (ne otvara direktno profil)
     */
    @FXML
    private void onSearchByIndeks() {
        String godinaStr = txtGodina.getText().trim();
        String brojStr = txtBroj.getText().trim();
        String oznaka = txtOznaka.getText().trim();

        if (godinaStr.isEmpty() || brojStr.isEmpty() || oznaka.isEmpty()) {
            AlertUtil.showWarning("Upozorenje", "Unesite godinu, broj i oznaku indeksa");
            return;
        }

        try {
            int godina = Integer.parseInt(godinaStr);
            int broj = Integer.parseInt(brojStr);

            log.info("Searching by indeks: {}/{}/{}", godina, broj, oznaka);

            // ✅ findByIndeks vraća Page<StudentPodaciDTO>
            studentService.findByIndeks(godina, broj, oznaka)
                    .subscribe(
                            page -> Platform.runLater(() -> {
                                studentData.clear();
                                studentData.addAll(page.getContent());
                                pagination.setPageCount(Math.max(1, page.getTotalPages()));
                                log.info("Found {} students", page.getTotalElements());

                                // Ako je pronađen samo jedan student, odmah otvori profil
                                if (page.getContent().size() == 1) {
                                    StudentPodaciDTO student = page.getContent().get(0);
                                    openStudentProfile(student.getId());
                                }
                            }),
                            error -> Platform.runLater(() -> {
                                log.error("Student not found", error);
                                AlertUtil.showError("Greška", "Student sa tim brojem indeksa nije pronađen");
                            })
                    );

        } catch (NumberFormatException e) {
            AlertUtil.showError("Greška", "Godina i broj moraju biti brojevi");
        }
    }

    /**
     * 2. NAČIN: Pretraga po imenu/prezimenu - PRIKAZUJE LISTU
     */
    @FXML
    private void onSearchByName() {
        currentPage = 0;
        pagination.setCurrentPageIndex(0);
        searchByName();
    }

    private void searchByName() {
        String ime = txtIme.getText().trim();
        String prezime = txtPrezime.getText().trim();

        if (ime.isEmpty() && prezime.isEmpty()) {
            AlertUtil.showWarning("Upozorenje", "Unesite ime ili prezime za pretragu");
            return;
        }

        log.info("Searching students: ime={}, prezime={}, page={}", ime, prezime, currentPage);

        studentService.searchStudents(ime, prezime, currentPage, pageSize)
                .subscribe(
                        page -> Platform.runLater(() -> {
                            // ✅ Ispravno mapiranje Page<StudentDTO>
                            studentData.clear();
                            studentData.addAll(page.getContent());
                            pagination.setPageCount(Math.max(1, page.getTotalPages()));
                            log.info("Found {} students", page.getTotalElements());
                        }),
                        error -> Platform.runLater(() -> {
                            log.error("Search failed", error);
                            AlertUtil.showException("Greška pri pretrazi", (Exception) error);
                        })
                );
    }

    /**
     * 3. NAČIN: Pretraga po srednjoj školi - PRIKAZUJE LISTU
     */
    @FXML
    private void onSearchBySrednjaSkola() {
        SrednjaSkola selected = cmbSrednjaSkola.getValue();

        if (selected == null) {
            AlertUtil.showWarning("Upozorenje", "Izaberite srednju školu");
            return;
        }

        log.info("Searching by srednja skola: {}", selected.getNaziv());

        studentService.findBySrednjaSkola(selected.getId())
                .subscribe(
                        students -> Platform.runLater(() -> {
                            // ✅ List<StudentPodaciDTO> se pravilno parsira
                            studentData.clear();
                            studentData.addAll(students);
                            pagination.setPageCount(1);
                            log.info("Found {} students from {}", students.size(), selected.getNaziv());
                        }),
                        error -> Platform.runLater(() -> {
                            log.error("Search by skola failed", error);
                            AlertUtil.showException("Greška pri pretrazi", (Exception) error);
                        })
                );
    }

    /**
     * Otvori profil studenta
     */
    private void openStudentProfile(Long studentIndeksId) {
        if (studentIndeksId == null) {
            AlertUtil.showError("Greška", "Student nema validan ID");
            return;
        }

        try {
            FxmlLoader.LoadResult<StudentProfileController> result =
                    fxmlLoader.loadWithController("student-profile.fxml");

            StudentProfileController controller = result.getController();
            controller.loadStudent(studentIndeksId);

            navigationManager.navigateTo(result.getRoot(), "Profil studenta");

        } catch (Exception e) {
            log.error("Failed to open student profile", e);
            AlertUtil.showException("Greška", e);
        }
    }

    @FXML
    private void onClearSearch() {
        txtIme.clear();
        txtPrezime.clear();
        txtGodina.clear();
        txtBroj.clear();
        txtOznaka.clear();
        cmbSrednjaSkola.setValue(null);
        studentData.clear();
        pagination.setPageCount(1);
    }

    private static class SrednjaSkola {
        private final Long id;
        private final String naziv;

        public SrednjaSkola(Long id, String naziv) {
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