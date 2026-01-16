package org.raflab.studsluzba.client.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.extern.slf4j.Slf4j;
import org.raflab.studsluzba.client.dto.StudentPodaciDTO;
import org.raflab.studsluzba.client.navigation.NavigationManager;
import org.raflab.studsluzba.client.services.StudentService;
import org.raflab.studsluzba.client.utils.AlertUtil;
import org.raflab.studsluzba.client.utils.FxmlLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StudentSearchController {

    @FXML
    private TextField txtIme;

    @FXML
    private TextField txtPrezime;

    @FXML
    private TextField txtGodina;

    @FXML
    private TextField txtBroj;

    @FXML
    private TextField txtOznaka;

    @FXML
    private Button btnSearchByName;

    @FXML
    private Button btnSearchByIndeks;

    @FXML
    private TableView<StudentPodaciDTO> tableStudents;

    @FXML
    private TableColumn<StudentPodaciDTO, String> colIme;

    @FXML
    private TableColumn<StudentPodaciDTO, String> colPrezime;

    @FXML
    private TableColumn<StudentPodaciDTO, String> colEmail;

    @FXML
    private TableColumn<StudentPodaciDTO, String> colJmbg;

    @FXML
    private Pagination pagination;

    @Autowired
    private StudentService studentService;

    @Autowired
    private NavigationManager navigationManager;

    @Autowired
    private FxmlLoader fxmlLoader;

    private final ObservableList<StudentPodaciDTO> studentData = FXCollections.observableArrayList();

    private int currentPage = 0;
    private final int pageSize = 20;

    @FXML
    public void initialize() {
        log.info("StudentSearchController initialized");

        setupTable();
        setupPagination();
    }

    /**
     * Setup tabele
     */
    private void setupTable() {
        colIme.setCellValueFactory(new PropertyValueFactory<>("ime"));
        colPrezime.setCellValueFactory(new PropertyValueFactory<>("prezime"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colJmbg.setCellValueFactory(new PropertyValueFactory<>("jmbg"));

        tableStudents.setItems(studentData);

        // Double-click za otvaranje profila
        tableStudents.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                StudentPodaciDTO selected = tableStudents.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    openStudentProfile(selected.getId());
                }
            }
        });
    }

    /**
     * Setup paginacije
     */
    private void setupPagination() {
        pagination.setPageCount(1);
        pagination.setCurrentPageIndex(0);

        pagination.currentPageIndexProperty().addListener((obs, oldPage, newPage) -> {
            currentPage = newPage.intValue();
            searchByName();
        });
    }

    /**
     * Pretraga po imenu/prezimenu
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
     * Pretraga po broju indeksa
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

            studentService.findByIndeks(godina, broj, oznaka)
                    .subscribe(
                            profile -> Platform.runLater(() -> {
                                log.info("Found student: {}", profile.getId());
                                openStudentProfile(profile.getId());
                            }),
                            error -> Platform.runLater(() -> {
                                log.error("Student not found", error);
                                AlertUtil.showError("Greška", "Student nije pronađen");
                            })
                    );

        } catch (NumberFormatException e) {
            AlertUtil.showError("Greška", "Godina i broj moraju biti brojevi");
        }
    }

    /**
     * Otvori profil studenta
     */
    private void openStudentProfile(Long studentIndeksId) {
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

    /**
     * Clear search fields
     */
    @FXML
    private void onClearSearch() {
        txtIme.clear();
        txtPrezime.clear();
        txtGodina.clear();
        txtBroj.clear();
        txtOznaka.clear();
        studentData.clear();
        pagination.setPageCount(1);
    }
}