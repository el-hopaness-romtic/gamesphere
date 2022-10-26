package ru.gamesphere.module;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import org.jetbrains.annotations.NotNull;
import ru.gamesphere.logger.FileLogger;

import java.util.logging.Logger;

public class FileLoggerModule extends AbstractModule {
    @NotNull
    public static final String FILE_ARGUMENT = "--filetag=";
    @NotNull
    public static final String TAG = "tag";
    @NotNull
    private final String[] args;

    public FileLoggerModule(@NotNull String[] args) {
        this.args = args;
    }

    @Override
    public void configure() {
        for (String arg : args) {
            if (arg.startsWith(FILE_ARGUMENT)) {
                try {
                    bind(FileLogger.class).toConstructor(FileLogger.class.getConstructor(Logger.class, String.class));
                    bind(String.class).annotatedWith(Names.named(TAG)).toInstance(arg.substring(FILE_ARGUMENT.length()));
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
