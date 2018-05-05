package fi.linuxbox.cdi.swing.core.commands;

/**
 * Swing action for removing all rows.
 */
public class ClearRowsAction extends AbstractCdiAction<ClearRowsCommand> {
    public ClearRowsAction() {
        super("Clear Rows", "clear-rows");
    }
}
