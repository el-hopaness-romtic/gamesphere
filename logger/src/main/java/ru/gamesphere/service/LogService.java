package ru.gamesphere.service;

import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;
import ru.gamesphere.logger.AbstractLogger;

import java.util.List;

import static ru.gamesphere.module.ConsoleLoggerModule.CONSOLE_ARGUMENT;
import static ru.gamesphere.module.FileLoggerModule.FILE_ARGUMENT;

public class LogService {
    @NotNull
    private final List<@NotNull AbstractLogger> loggers;
    private int id = 0;

    @Inject
    public LogService(@NotNull List<@NotNull AbstractLogger> loggers) {
        validate(loggers);
        this.loggers = loggers;
    }

    private void validate(List<AbstractLogger> loggers) {
        if (loggers.isEmpty())
            throw new IllegalArgumentException("Should use " + CONSOLE_ARGUMENT + " and/or " + FILE_ARGUMENT
                    + "... to log input to console and/or file respectively");
    }

    public void log(String info) {
        for (AbstractLogger logger : loggers) {
            logger.log(++id, info);
        }
    }
}
