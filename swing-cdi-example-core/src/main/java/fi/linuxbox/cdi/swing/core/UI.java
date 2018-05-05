package fi.linuxbox.cdi.swing.core;

import fi.linuxbox.cdi.swing.core.commands.AddRowAction;
import fi.linuxbox.cdi.swing.core.commands.ClearRowsAction;
import fi.linuxbox.cdi.swing.core.events.AbstractRowEvent;
import fi.linuxbox.cdi.swing.core.events.RowAddedEvent;
import fi.linuxbox.cdi.swing.core.events.RowDeletedEvent;
import fi.linuxbox.cdi.swing.core.events.RowUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.util.function.IntConsumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.awt.BorderLayout.NORTH;

@ApplicationScoped
public class UI {
    private static final Logger log = LoggerFactory.getLogger(UI.class);

    private static final int ROW_ID_COLUMN = 0;
    private static final int STATE_COLUMN = 1;

    private JFrame mainWindow;
    private DefaultTableModel tableModel;

    @Inject private AddRowAction addRowAction;
    @Inject private ClearRowsAction clearRowsAction;

    @PostConstruct
    private void setup() {
        log.info("starting constructing: UI");
        try {
            SwingUtilities.invokeAndWait(() -> {
                log.info("starting constructing: main window");
                mainWindow = new JFrame("Window title");
                mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                mainWindow.setSize(300, 450);

                final String[][] rows = new String[][]{};
                final String[] cols = new String[] { "ID#", "State" };

                tableModel = new DefaultTableModel(rows, cols);

                final JToolBar toolBar = new JToolBar("Actions");
                toolBar.add(addRowAction);
                toolBar.add(clearRowsAction);

                mainWindow.add(toolBar, NORTH);
                mainWindow.add(new JScrollPane(new JTable(tableModel)));

                log.info("finished constructing: main window");
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("finished constructing: UI");
    }

    public void showMainWindow() {
        exec("show main window", () -> mainWindow.setVisible(true));
    }

    private void onRowAdded(@Observes RowAddedEvent event) {
        Stream.of(event)
                .map(AbstractRowEvent::asRow)
                .forEach(row -> exec("add row", () -> tableModel.addRow(row)));
    }

    private void onRowStateChanged(@Observes RowUpdatedEvent event) {
        exec("set row", () -> forRow(event.getRowId(), row ->
                tableModel.setValueAt(event.getState(), row, STATE_COLUMN)
        ));
    }

    private void onRowRemoved(@Observes RowDeletedEvent event) {
        exec("del row", () -> forRow(event.getRowId(), tableModel::removeRow));
    }

    private void exec(final String name, final Runnable func) {
        log.info("starting swing-invoke: " + name);
        SwingUtilities.invokeLater(() -> {
            log.info("starting swing-event: " + name);
            func.run();
            log.info("finished swing-event: " + name);
        });
        log.info("finished swing-invoke: " + name);
    }

    private void forRow(final String rowId, IntConsumer func) {
        IntStream.range(0, tableModel.getRowCount())
                .filter(row -> tableModel.getValueAt(row, ROW_ID_COLUMN).equals(rowId))
                .findFirst()
                .ifPresent(func);
    }
}
