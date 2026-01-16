package org.raflab.studsluzba.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;
import org.raflab.studsluzba.client.navigation.NavigationManager;
import org.raflab.studsluzba.client.utils.AlertUtil;
import org.raflab.studsluzba.client.utils.FxmlLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class
MainController {

    @FXML
    private BorderPane mainContainer;

    @FXML
    private Button btnStudenti;

    @FXML
    private Button btnIspiti;

    @FXML
    private Button btnPredmeti;

    @FXML
    private Button btnIzvestaji;

    @Autowired
    private NavigationManager navigationManager;

    @Autowired
    private FxmlLoader fxmlLoader;

    @FXML
    public void initialize() {
        log.info("MainController initialized");

        // Default view - Student search
        loadStudentSearch();
    }

    @FXML
    private void onStudentiClick() {
        log.info("Studenti menu clicked");
        loadStudentSearch();
    }

    @FXML
    private void onIspitiClick() {
        log.info("Ispiti menu clicked");
        loadIspitView();
    }

    @FXML
    private void onPredmetiClick() {
        log.info("Predmeti menu clicked");
        loadPredmetView();
    }

    @FXML
    private void onIzvestajiClick() {
        log.info("Izvestaji menu clicked");
        loadReportView();
    }

    /**
     * Učitaj Student Search view
     */
    private void loadStudentSearch() {
        try {
            Parent view = fxmlLoader.load("student-search.fxml");
            navigationManager.navigateTo(view, "Pretraga studenata");
        } catch (Exception e) {
            log.error("Failed to load student search view", e);
            AlertUtil.showException("Greška", e);
        }
    }

    /**
     * Učitaj Ispit view
     */
    private void loadIspitView() {
        try {
            Parent view = fxmlLoader.load("ispit-view.fxml");
            navigationManager.navigateTo(view, "Ispiti");
        } catch (Exception e) {
            log.error("Failed to load ispit view", e);
            AlertUtil.showException("Greška", e);
        }
    }

    /**
     * Učitaj Predmet view
     */
    private void loadPredmetView() {
        try {
            Parent view = fxmlLoader.load("predmet-view.fxml");
            navigationManager.navigateTo(view, "Predmeti");
        } catch (Exception e) {
            log.error("Failed to load predmet view", e);
            AlertUtil.showException("Greška", e);
        }
    }

    /**
     * Učitaj Report view
     */
    private void loadReportView() {
        try {
            Parent view = fxmlLoader.load("report-view.fxml");
            navigationManager.navigateTo(view, "Izveštaji");
        } catch (Exception e) {
            log.error("Failed to load report view", e);
            AlertUtil.showException("Greška", e);
        }
    }
}
