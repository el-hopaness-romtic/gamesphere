package ru.gamesphere.logger;

import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

public class ConsoleLogger extends AbstractLogger {
    @NotNull
    private final Logger logger;
    @NotNull
    private static final String TEMPLATE = "%d\t| %s";

    @Inject
    public ConsoleLogger(@NotNull Logger logger) {
        logger.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
        this.logger = logger;
    }

    @Override
    public void log(int id, String msg) {
        logger.log(Level.INFO, TEMPLATE.formatted(id, msg));
    }
}
