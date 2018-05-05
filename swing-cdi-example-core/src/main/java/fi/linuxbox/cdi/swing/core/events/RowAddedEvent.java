package fi.linuxbox.cdi.swing.core.events;

/**
 * A CDI event to inform that a new row was added.
 */
public class RowAddedEvent extends AbstractRowEvent {
    public RowAddedEvent(final String rowId, final String state) {
        super(rowId, state);
    }
}
