package ru.gamesphere.service;

import com.google.inject.Inject;
import org.jetbrains.annotations.Nullable;
import ru.gamesphere.logger.AbstractLogger;
import ru.gamesphere.logger.ConsoleLogger;
import ru.gamesphere.logger.FileLogger;

import java.util.LinkedList;
import java.util.List;

public class LogServiceFactory {
    @Inject(optional = true)
    @Nullable ConsoleLogger consoleLogger;
    @Inject(optional = true)
    @Nullable FileLogger fileLogger;

    public LogService getLoggerService() {
        List<AbstractLogger> loggers = new LinkedList<>();
        if (consoleLogger != null) loggers.add(consoleLogger);
        if (fileLogger != null) loggers.add(fileLogger);
        return new LogService(loggers);
    }
}
