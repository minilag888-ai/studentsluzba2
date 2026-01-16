package org.raflab.studsluzba.client.navigation;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.raflab.studsluzba.client.controllers.MainController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NavigationManager {

    @Autowired
    private NavigationHistory history;

    @Autowired
    private ApplicationContext applicationContext;

    private Stage primaryStage;
    private Scene scene;
    private StackPane contentArea;

    public void initialize(Stage stage, Scene scene) {
        this.primaryStage = stage;
        this.scene = scene;

        setupMouseNavigation();
        setupKeyboardNavigation();

        log.info("NavigationManager initialized");
    }

    public void setContentArea(StackPane contentArea) {
        this.contentArea = contentArea;
        log.info("Content area set");
    }

    public void navigateTo(Parent view, String title) {
        navigateTo(view, title, null);
    }

    public void navigateTo(Parent view, String title, Object context) {
        try {
            NavigationEvent event = new NavigationEvent(view, title, context);
            history.push(event);
            displayView(event);
            log.info("Navigated to: {}", title);
        } catch (Exception e) {
            log.error("Navigation failed", e);
        }
    }

    public void goBack() {
        if (!history.canGoBack()) {
            log.debug("Cannot go back");
            return;
        }

        try {
            NavigationEvent previous = history.goBack();
            if (previous != null) {
                displayView(previous);
                log.info("Navigated back to: {}", previous.getTitle());
            }
        } catch (Exception e) {
            log.error("Back navigation failed", e);
        }
    }

    public void goForward() {
        if (!history.canGoForward()) {
            log.debug("Cannot go forward");
            return;
        }

        try {
            NavigationEvent next = history.goForward();
            if (next != null) {
                displayView(next);
                log.info("Navigated forward to: {}", next.getTitle());
            }
        } catch (Exception e) {
            log.error("Forward navigation failed", e);
        }
    }

    private void displayView(NavigationEvent event) {
        if (contentArea != null) {
            contentArea.getChildren().clear();
            contentArea.getChildren().add(event.getView());
            primaryStage.setTitle("Studentska služba - " + event.getTitle());

            // Ažuriraj aktivno dugme
            updateActiveButton(event.getViewType());
        }
    }

    /**
     * Ažuriraj aktivno dugme u navigation baru
     */
    private void updateActiveButton(String viewType) {
        try {
            MainController mainController = applicationContext.getBean(MainController.class);
            mainController.setActiveButton(viewType);
        } catch (Exception e) {
            log.warn("Could not update active button", e);
        }
    }

    private void setupMouseNavigation() {
        scene.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.BACK) {
                goBack();
                event.consume();
            }
            if (event.getButton() == MouseButton.FORWARD) {
                goForward();
                event.consume();
            }
        });
    }

    private void setupKeyboardNavigation() {
        KeyCombination backKey = new KeyCodeCombination(KeyCode.OPEN_BRACKET, KeyCombination.CONTROL_DOWN);
        KeyCombination forwardKey = new KeyCodeCombination(KeyCode.CLOSE_BRACKET, KeyCombination.CONTROL_DOWN);

        scene.setOnKeyPressed(event -> {
            if (backKey.match(event)) {
                goBack();
                event.consume();
            } else if (forwardKey.match(event)) {
                goForward();
                event.consume();
            }
        });
    }

    public void clearHistory() {
        history.clear();
        log.info("Navigation history cleared");
    }
}