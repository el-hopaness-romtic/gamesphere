package ru.gamesphere;

import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;
import ru.gamesphere.service.LogService;
import ru.gamesphere.service.LogServiceFactory;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class Application {
    @NotNull   private final LogService logService;

    @Inject
    public Application(@NotNull LogServiceFactory factory) {
        this.logService = factory.getLoggerService();
    }

    public void waitForInput() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Waiting for new lines. Key in Ctrl+D to exit.");
            while (true) {
                logService.log(scanner.nextLine());
            }
        } catch (IllegalStateException | NoSuchElementException e) {
        }
    }
}
