package fi.linuxbox.cdi.swing.core.events;

/**
 * A CDI event to inform that a row was deleted.
 */
public class RowDeletedEvent extends AbstractRowEvent {
    public RowDeletedEvent(final String rowId) {
        super(rowId, null);
    }
}
