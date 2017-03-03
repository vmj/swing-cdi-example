package fi.linuxbox.cdi.swing.core;

import fi.linuxbox.cdi.swing.core.commands.AddRowAction;
import fi.linuxbox.cdi.swing.core.events.RowAddedEvent;
import fi.linuxbox.cdi.swing.core.events.RowUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import static java.awt.BorderLayout.NORTH;

@ApplicationScoped
public class UI {
    private static final Logger log = LoggerFactory.getLogger(UI.class);

    private static final int ROW_ID_COLUMN = 0;
    private static final int STATE_COLUMN = 1;

    private JFrame mainWindow;
    private DefaultTableModel tableModel;

    @Inject private AddRowAction addRowAction;

    @PostConstruct
    private void setup() {
        log.info("starting constructing: UI");
        try {
            SwingUtilities.invokeAndWait(() -> {
                log.info("starting constructing: main window");
                mainWindow = new JFrame("Window title");
                mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                mainWindow.setSize(300, 150);

                final String[][] rows = new String[][]{};
                final String[] cols = new String[] { "ID#", "State" };

                tableModel = new DefaultTableModel(rows, cols);

                mainWindow.add(new JButton(addRowAction), NORTH);
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
        exec("add row", () -> tableModel.addRow(new String[] { event.getRowId(), event.getState() }));
    }

    private void onRowStateChanged(@Observes RowUpdatedEvent event) {
        exec("set row", () -> {
            for (int row = 0; row < tableModel.getRowCount(); row++) {
                if (event.getRowId().equals(tableModel.getValueAt(row, ROW_ID_COLUMN))) {
                    tableModel.setValueAt(event.getState(), row, STATE_COLUMN);
                    break;
                }
            }
        });
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
}
