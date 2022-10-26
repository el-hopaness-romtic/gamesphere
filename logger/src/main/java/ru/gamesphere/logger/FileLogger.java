package ru.gamesphere.logger;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.*;

import static ru.gamesphere.module.FileLoggerModule.TAG;

public class FileLogger extends AbstractLogger {
    @NotNull
    private final Logger logger;
    @NotNull
    private final String template;

    @Inject
    public FileLogger(
            @NotNull Logger logger,
            @NotNull @Named(TAG) String tag
    ) throws IOException {
        template = "%d\t| <" + tag + ">%s</" + tag + ">";

        String pattern = (LocalDateTime.now() + ".log").replace(':', '_');
        FileHandler handler = new FileHandler(pattern, false);

        Formatter formatter = new SimpleFormatter() {
            @Override
            public String format(LogRecord logRecord) {
                // не выводим datetime и имя логгера
                return logRecord.getLevel() + ": " + logRecord.getMessage() + System.lineSeparator();
            }
        };
        handler.setFormatter(formatter);

        logger.setUseParentHandlers(false);
        logger.addHandler(handler);
        this.logger = logger;
    }

    @Override
    public void log(int id, String msg) {
        logger.log(Level.INFO, template.formatted(id, msg));
    }
}
