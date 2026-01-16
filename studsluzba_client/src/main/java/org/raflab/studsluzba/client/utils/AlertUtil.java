package org.raflab.studsluzba.client.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class AlertUtil {

    /**
     * Prikaži error alert
     */
    public static void showError(String title, String message) {
        log.error("Error alert: {} - {}", title, message);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Prikaži info alert
     */
    public static void showInfo(String title, String message) {
        log.info("Info alert: {} - {}", title, message);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Prikaži warning alert
     */
    public static void showWarning(String title, String message) {
        log.warn("Warning alert: {} - {}", title, message);

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Prikaži confirmation alert
     */
    public static boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * Prikaži error sa exception detaljima
     */
    public static void showException(String title, Exception e) {
        log.error("Exception alert: {} - {}", title, e.getMessage(), e);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("Greška:");
        alert.setContentText(e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName());
        alert.showAndWait();
    }
}