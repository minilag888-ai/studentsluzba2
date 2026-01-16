package org.raflab.studsluzba.client.navigation;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NavigationManager {

    @Autowired
    private NavigationHistory history;

    private Stage primaryStage;
    private Scene scene;

    /**
     * Inicijalizacija - poziva se iz MainApp
     */
    public void initialize(Stage stage, Scene scene) {
        this.primaryStage = stage;
        this.scene = scene;

        setupMouseNavigation();
        setupKeyboardNavigation();

        log.info("NavigationManager initialized with mouse and keyboard support");
    }

    /**
     * Navigacija na novu stranicu
     */
    public void navigateTo(Parent view, String title) {
        navigateTo(view, title, null);
    }

    public void navigateTo(Parent view, String title, Object context) {
        try {
            NavigationEvent event = new NavigationEvent(view, title, context);
            history.push(event);

            displayView(view, title);

            log.info("Navigated to: {}", title);
        } catch (Exception e) {
            log.error("Navigation failed: {}", e.getMessage(), e);
        }
    }

    /**
     * Back (nazad)
     */
    public void goBack() {
        if (!history.canGoBack()) {
            log.debug("Cannot go back - already at first page");
            return;
        }

        try {
            NavigationEvent previous = history.goBack();
            if (previous != null) {
                displayView(previous.getView(), previous.getTitle());
                log.info("Navigated back to: {}", previous.getTitle());
            }
        } catch (Exception e) {
            log.error("Back navigation failed: {}", e.getMessage(), e);
        }
    }

    /**
     * Forward (napred)
     */
    public void goForward() {
        if (!history.canGoForward()) {
            log.debug("Cannot go forward - no forward history");
            return;
        }

        try {
            NavigationEvent next = history.goForward();
            if (next != null) {
                displayView(next.getView(), next.getTitle());
                log.info("Navigated forward to: {}", next.getTitle());
            }
        } catch (Exception e) {
            log.error("Forward navigation failed: {}", e.getMessage(), e);
        }
    }

    /**
     * Prikaži view na stage-u
     */
    private void displayView(Parent view, String title) {
        scene.setRoot(view);
        primaryStage.setTitle("Studentska služba - " + title);
    }

    /**
     * Mouse button 4/5 navigation
     */
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

    /**
     * Ctrl+[ and Ctrl+] navigation
     */
    private void setupKeyboardNavigation() {
        KeyCombination backKey = new KeyCodeCombination(
                KeyCode.OPEN_BRACKET,
                KeyCombination.CONTROL_DOWN
        );

        KeyCombination forwardKey = new KeyCodeCombination(
                KeyCode.CLOSE_BRACKET,
                KeyCombination.CONTROL_DOWN
        );

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

    /**
     * Clear history (logout)
     */
    public void clearHistory() {
        history.clear();
        log.info("Navigation history cleared");
    }
}