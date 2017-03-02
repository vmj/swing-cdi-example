package fi.linuxbox.cdi.swing.core;

import fi.linuxbox.cdi.swing.core.commands.AddNewRowCommand;
import fi.linuxbox.cdi.swing.core.events.NewRowAddedEvent;
import fi.linuxbox.cdi.swing.core.events.RowStateChangedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.event.ObservesAsync;
import javax.inject.Inject;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ApplicationScoped
public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(4);
    private static final Random random = new Random();

    @Inject private Event<NewRowAddedEvent> newRowAddedEventEvent;
    @Inject private Event<RowStateChangedEvent> rowStateChangedEventEvent;

    public void onBoot(@Observes BootEvent e, UI ui) {
        log.info("starting cdi-event: boot");
        ui.showMainWindow();
        log.info("finished cdi-event: boot");
    }

    public void onAddNewRow(@ObservesAsync AddNewRowCommand command) {
        log.info("starting cdi-event: add new row");

        final String rowId = UUID.randomUUID().toString();

        newRowAddedEventEvent.fire(new NewRowAddedEvent(rowId, "IN QUEUE"));

        threadPool.submit(() -> {
            log.info("starting worker: " + Thread.currentThread().getId());
            rowStateChangedEventEvent.fire(new RowStateChangedEvent(rowId, "0 %"));
            //ui.setRow(rowId, "0 %");
            final int totalDuration = 1000 + random.nextInt(4000); // 1-5 secs
            int leftToDo = totalDuration;
            while (leftToDo > 0) {
                int sleep = Math.min(250, leftToDo);
                leftToDo -= sleep;
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    leftToDo = 0;
                }
                int percent = 100 - (int)((leftToDo / (double)totalDuration) * 100);
                rowStateChangedEventEvent.fire(new RowStateChangedEvent(rowId, String.valueOf(percent) + " %"));
            }
            rowStateChangedEventEvent.fire(new RowStateChangedEvent(rowId, "FINISHED"));
            log.info("finished worker: " + Thread.currentThread().getId());
        });
        log.info("finished cdi-event: command");
    }
}
