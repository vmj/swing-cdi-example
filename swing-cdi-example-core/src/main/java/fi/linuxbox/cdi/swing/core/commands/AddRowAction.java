package fi.linuxbox.cdi.swing.core.commands;

/**
 * Swing action for adding a new row.
 */
public class AddRowAction extends AbstractCdiAction<AddRowCommand> {
    public AddRowAction() {
        super("Add Row", "new-row");
    }
}
