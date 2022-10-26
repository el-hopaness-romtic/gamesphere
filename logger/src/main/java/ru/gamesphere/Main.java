package ru.gamesphere;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.jetbrains.annotations.NotNull;
import ru.gamesphere.module.ConsoleLoggerModule;
import ru.gamesphere.module.DefaultModule;
import ru.gamesphere.module.FileLoggerModule;

import java.util.Locale;

public class Main {

    static {
        // чтоб не было проблем с русской кодировкой
        Locale.setDefault(new Locale("en", "GB"));
    }

    public static void main(@NotNull String[] args) {
        final Injector injector = Guice.createInjector(
                new DefaultModule(), new ConsoleLoggerModule(args), new FileLoggerModule(args)
        );
        injector.getInstance(Application.class).waitForInput();
    }
}

