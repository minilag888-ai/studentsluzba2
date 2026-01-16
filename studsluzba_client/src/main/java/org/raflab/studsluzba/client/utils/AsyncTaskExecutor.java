package org.raflab.studsluzba.client.utils;

import javafx.application.Platform;
import javafx.concurrent.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Slf4j
@Component
public class AsyncTaskExecutor {

    private final ExecutorService executorService;

    public AsyncTaskExecutor(@Value("${async.thread.pool.size:5}") int poolSize) {
        this.executorService = Executors.newFixedThreadPool(poolSize);
        log.info("AsyncTaskExecutor initialized with pool size: {}", poolSize);
    }

    /**
     * Izvrši task asinhrono
     */
    public <T> void executeAsync(
            Supplier<T> backgroundTask,
            Consumer<T> onSuccess,
            Consumer<Throwable> onError) {

        Task<T> task = new Task<T>() {
            @Override
            protected T call() throws Exception {
                return backgroundTask.get();
            }

            @Override
            protected void succeeded() {
                Platform.runLater(() -> {
                    try {
                        onSuccess.accept(getValue());
                    } catch (Exception e) {
                        log.error("Error in success handler", e);
                        onError.accept(e);
                    }
                });
            }

            @Override
            protected void failed() {
                Platform.runLater(() -> {
                    Throwable exception = getException();
                    log.error("Async task failed", exception);
                    onError.accept(exception);
                });
            }
        };

        executorService.submit(task);
    }

    /**
     * Shutdown executor
     */
    public void shutdown() {
        executorService.shutdown();
        log.info("AsyncTaskExecutor shutdown");
    }
}