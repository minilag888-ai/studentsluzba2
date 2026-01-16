package org.raflab.studsluzba.client.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import lombok.extern.slf4j.Slf4j;
import org.raflab.studsluzba.client.navigation.NavigationManager;
import org.raflab.studsluzba.client.utils.AlertUtil;
import org.raflab.studsluzba.client.utils.FxmlLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MainController {

    @FXML private BorderPane rootPane;
    @FXML private StackPane contentArea;
    @FXML private Button btnStudenti;
    @FXML private Button btnIspiti;
    @FXML private Button btnPredmeti;
    @FXML private Button btnIzvestaji;

    @Autowired private NavigationManager navigationManager;
    @Autowired private FxmlLoader fxmlLoader;

    private Button currentActiveButton;

    @FXML
    public void initialize() {
        log.info("MainController initialized");

        // Učitaj default view nakon što se GUI inicijalizuje
        Platform.runLater(() -> {
            setActiveButton(btnStudenti);
            loadStudentSearch();
        });
    }

    @FXML
    private void onStudentiClick() {
        log.info("Studenti button clicked");
        setActiveButton(btnStudenti);
        loadStudentSearch();
    }

    @FXML
    private void onIspitiClick() {
        log.info("Ispiti button clicked");
        setActiveButton(btnIspiti);
        loadIspitView();
    }

    @FXML
    private void onPredmetiClick() {
        log.info("Predmeti button clicked");
        setActiveButton(btnPredmeti);
        loadPredmetView();
    }

    @FXML
    private void onIzvestajiClick() {
        log.info("Izvestaji button clicked");
        setActiveButton(btnIzvestaji);
        AlertUtil.showInfo("TODO", "Izveštaji view u izradi");
    }

    private void loadStudentSearch() {
        loadView("student-search.fxml", "Pretraga studenata", "studenti");
    }

    private void loadIspitView() {
        loadView("ispit-view.fxml", "Ispiti", "ispiti");
    }

    private void loadPredmetView() {
        loadView("predmet-view.fxml", "Predmeti", "predmeti");
    }

    /**
     * Opšta metoda za učitavanje view-a
     */
    private void loadView(String fxmlFile, String title, String viewType) {
        try {
            log.info("Loading view: {}", fxmlFile);
            Parent view = fxmlLoader.load(fxmlFile);
            setContent(view);
            navigationManager.navigateTo(view, title, viewType);
            log.info("Successfully loaded: {}", fxmlFile);
        } catch (Exception e) {
            log.error("Failed to load view: {}", fxmlFile, e);
            AlertUtil.showException("Greška pri učitavanju", e);
        }
    }

    private void setContent(Parent content) {
        if (contentArea != null) {
            contentArea.getChildren().clear();
            contentArea.getChildren().add(content);
            log.debug("Content set successfully");
        } else {
            log.error("ContentArea is null! Cannot set content.");
            AlertUtil.showError("Greška", "Content area nije inicijalizovan!");
        }
    }

    /**
     * Označi aktivno dugme - JAVNA metoda (poziva NavigationManager)
     */
    public void setActiveButton(String viewType) {
        Button button = getButtonForViewType(viewType);
        setActiveButton(button);
    }

    /**
     * Helper metoda - mapira viewType na Button (Java 11 compatible)
     */
    private Button getButtonForViewType(String viewType) {
        if (viewType == null) {
            return btnStudenti;
        }

        switch (viewType) {
            case "studenti":
                return btnStudenti;
            case "ispiti":
                return btnIspiti;
            case "predmeti":
                return btnPredmeti;
            case "izvestaji":
                return btnIzvestaji;
            default:
                return btnStudenti;
        }
    }

    private void setActiveButton(Button button) {
        // Ukloni active stil sa svih dugmadi
        btnStudenti.getStyleClass().remove("nav-button-active");
        btnIspiti.getStyleClass().remove("nav-button-active");
        btnPredmeti.getStyleClass().remove("nav-button-active");
        btnIzvestaji.getStyleClass().remove("nav-button-active");

        // Dodaj active stil na novo dugme
        if (!button.getStyleClass().contains("nav-button-active")) {
            button.getStyleClass().add("nav-button-active");
        }
        currentActiveButton = button;
    }
}