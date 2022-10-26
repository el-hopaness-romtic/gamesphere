package ru.gamesphere.module;

import com.google.inject.AbstractModule;
import org.jetbrains.annotations.NotNull;
import ru.gamesphere.logger.ConsoleLogger;

import java.util.logging.Logger;

public class ConsoleLoggerModule extends AbstractModule {
    @NotNull
    public static final String CONSOLE_ARGUMENT = "--console";
    @NotNull
    private final String[] args;

    public ConsoleLoggerModule(@NotNull String[] args) {
        this.args = args;
    }

    @Override
    public void configure() {
        for (String arg : args) {
            if (CONSOLE_ARGUMENT.equalsIgnoreCase(arg)) {
                try {
                    bind(ConsoleLogger.class).toConstructor(ConsoleLogger.class.getConstructor(Logger.class));
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
