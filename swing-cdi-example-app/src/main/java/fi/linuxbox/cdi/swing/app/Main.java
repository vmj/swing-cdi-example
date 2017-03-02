package fi.linuxbox.cdi.swing.app;

import fi.linuxbox.cdi.swing.core.BootEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;

public class Main {
    private static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String... args) throws Exception {
        log.info("starting main");
        SeContainer seContainer = SeContainerInitializer.newInstance().initialize();

        seContainer.getBeanManager().fireEvent(new BootEvent());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("starting shutdown");
            //seContainer.close(); // Weld already adds a shutdown hook for itself
            log.info("finished shutdown");
        }));

        log.info("finished main");
    }
}
