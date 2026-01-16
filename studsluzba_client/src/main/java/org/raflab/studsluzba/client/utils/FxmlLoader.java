package org.raflab.studsluzba.client.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Slf4j
@Component
public class FxmlLoader {

    private final ApplicationContext springContext;

    public FxmlLoader(ApplicationContext springContext) {
        this.springContext = springContext;
    }

    /**
     * Učitaj FXML sa Spring dependency injection
     */
    public Parent load(String fxmlFileName) throws IOException {
        String fxmlPath = "/fxml/" + fxmlFileName;
        URL fxmlUrl = getClass().getResource(fxmlPath);

        if (fxmlUrl == null) {
            throw new IOException("FXML file not found: " + fxmlPath);
        }

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        loader.setControllerFactory(springContext::getBean);

        Parent root = loader.load();
        log.info("Loaded FXML: {}", fxmlFileName);

        return root;
    }

    /**
     * Učitaj FXML i vrati controller
     */
    public <T> LoadResult<T> loadWithController(String fxmlFileName) throws IOException {
        String fxmlPath = "/fxml/" + fxmlFileName;
        URL fxmlUrl = getClass().getResource(fxmlPath);

        if (fxmlUrl == null) {
            throw new IOException("FXML file not found: " + fxmlPath);
        }

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        loader.setControllerFactory(springContext::getBean);

        Parent root = loader.load();
        T controller = loader.getController();

        log.info("Loaded FXML with controller: {}", fxmlFileName);

        return new LoadResult<>(root, controller);
    }

    /**
     * Helper klasa za rezultat učitavanja
     */
    public static class LoadResult<T> {
        private final Parent root;
        private final T controller;

        public LoadResult(Parent root, T controller) {
            this.root = root;
            this.controller = controller;
        }

        public Parent getRoot() {
            return root;
        }

        public T getController() {
            return controller;
        }
    }
}