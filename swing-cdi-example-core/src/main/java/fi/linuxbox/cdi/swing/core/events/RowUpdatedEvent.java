package fi.linuxbox.cdi.swing.core.events;

/**
 * A CDI event to inform that a row was updated.
 */
public class RowUpdatedEvent extends AbstractRowEvent {
    public RowUpdatedEvent(final String rowId, final String state) {
        super(rowId, state);
    }
}
