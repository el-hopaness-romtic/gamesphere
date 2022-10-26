package ru.gamesphere.module;

import com.google.inject.AbstractModule;
import ru.gamesphere.Application;
import ru.gamesphere.service.LogServiceFactory;

public class DefaultModule extends AbstractModule {

    @Override
    public void configure() {
        try {
            // чтоб не подтягивался ConsoleLogger без явного объявления, ну и в принципе хороший тон
            binder().requireExplicitBindings();

            bind(LogServiceFactory.class).toInstance(new LogServiceFactory());
            bind(Application.class).toConstructor(Application.class.getConstructor(LogServiceFactory.class));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
