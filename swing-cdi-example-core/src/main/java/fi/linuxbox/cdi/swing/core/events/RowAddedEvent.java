package fi.linuxbox.cdi.swing.core.events;

/**
 * A CDI event to inform that a new row was added.
 *
 * (This app doesn't really persist anything, so it wasn't really added to anything.)
 */
public class RowAddedEvent extends AbstractRowEvent {
    public RowAddedEvent(final String rowId, final String state) {
        super(rowId, state);
    }
}
