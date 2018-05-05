package fi.linuxbox.cdi.swing.core;

import fi.linuxbox.cdi.swing.core.commands.AddRowCommand;
import fi.linuxbox.cdi.swing.core.commands.ClearRowsCommand;
import fi.linuxbox.cdi.swing.core.events.RowAddedEvent;
import fi.linuxbox.cdi.swing.core.events.RowDeletedEvent;
import fi.linuxbox.cdi.swing.core.events.RowUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.event.ObservesAsync;
import javax.inject.Inject;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ApplicationScoped
public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(4);
    private static final Random random = new Random();

    private static final Map<String, String> rows = new ConcurrentHashMap<>();

    @Inject private Event<RowAddedEvent> rowAdded;
    @Inject private Event<RowUpdatedEvent> rowUpdated;
    @Inject private Event<RowDeletedEvent> rowDeleted;

    public void onBoot(@Observes BootEvent e, UI ui) {
        log.info("starting cdi-event: boot");
        ui.showMainWindow();
        log.info("finished cdi-event: boot");
    }

    public void onAddNewRow(@ObservesAsync AddRowCommand command) {
        log.info("starting cdi-event: add new row");

        final String rowId = UUID.randomUUID().toString();
        rows.put(rowId, "foo");
        rowAdded.fire(new RowAddedEvent(rowId, "IN QUEUE"));

        threadPool.submit(() -> {
            log.info("starting worker: " + Thread.currentThread().getId());

            if (rows.containsKey(rowId))
                rowUpdated.fire(new RowUpdatedEvent(rowId, "0 %"));

            final int totalDuration = 1000 + random.nextInt(4000); // 1-5 secs
            int leftToDo = totalDuration;
            while (rows.containsKey(rowId) && leftToDo > 0) {
                final int sleep = Math.min(250, leftToDo);
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }

                leftToDo -= sleep;
                final int percent = 100 - (int)((leftToDo / (double)totalDuration) * 100);

                if (rows.containsKey(rowId))
                    rowUpdated.fire(new RowUpdatedEvent(rowId, String.valueOf(percent) + " %"));
            }

            if (rows.containsKey(rowId)) {
                if (leftToDo == 0) {
                    rowUpdated.fire(new RowUpdatedEvent(rowId, "FINISHED"));
                } else {
                    rowUpdated.fire(new RowUpdatedEvent(rowId, "FAILED"));
                }
            }

            log.info("finished worker: " + Thread.currentThread().getId());
        });
        log.info("finished cdi-event: add new row");
    }

    public void onClearRows(@ObservesAsync ClearRowsCommand command) {
        rows.keySet().forEach(key -> {
            rows.remove(key);
            rowDeleted.fire(new RowDeletedEvent(key));
        });
    }
}
