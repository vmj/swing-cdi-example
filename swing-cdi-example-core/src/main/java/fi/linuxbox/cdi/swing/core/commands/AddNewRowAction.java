package fi.linuxbox.cdi.swing.core.commands;

/**
 * Swing action for adding a new row.
 */
public class AddNewRowAction extends AbstractCdiAction<AddNewRowCommand> {
    public AddNewRowAction() {
        super("Add Row", "new-row");
    }
}
