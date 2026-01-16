package org.raflab.studsluzba.client;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.raflab.studsluzba.client.navigation.NavigationManager;
import org.raflab.studsluzba.client.utils.AlertUtil;
import org.raflab.studsluzba.client.utils.FxmlLoader;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;


@Slf4j
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class
})
public class StudsluzbaClientApp extends Application {

    private ConfigurableApplicationContext springContext;
    private NavigationManager navigationManager;
    private FxmlLoader fxmlLoader;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        // Pokreni Spring context
        springContext = new SpringApplicationBuilder(StudsluzbaClientApp.class)
                .headless(false)
                .run();

        // Preuzmi beans
        navigationManager = springContext.getBean(NavigationManager.class);
        fxmlLoader = springContext.getBean(FxmlLoader.class);

        log.info("Spring context initialized");
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Učitaj glavni view
            Parent root = fxmlLoader.load("main-view.fxml");

            // Kreiraj scenu
            Scene scene = new Scene(root, 1200, 800);

            // Inicijalizuj NavigationManager
            navigationManager.initialize(primaryStage, scene);

            // Pronađi contentArea i povezi sa NavigationManager
            StackPane contentArea = (StackPane) root.lookup("#contentArea");
            if (contentArea != null) {
                navigationManager.setContentArea(contentArea);
                log.info("ContentArea connected to NavigationManager");
            } else {
                log.warn("ContentArea not found in main-view!");
            }

            // Postavi stage
            primaryStage.setTitle("Studentska služba");
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.show();

            log.info("Application started successfully");

        } catch (Exception e) {
            log.error("Failed to start application", e);
            AlertUtil.showException("Greška pri pokretanju", e);
            System.exit(1);
        }
    }

    @Override
    public void stop() throws Exception {
        // Zatvori Spring context
        if (springContext != null) {
            springContext.close();
            log.info("Spring context closed");
        }
    }
}