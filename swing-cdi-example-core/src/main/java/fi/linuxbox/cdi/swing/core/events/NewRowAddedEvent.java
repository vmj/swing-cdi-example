package fi.linuxbox.cdi.swing.core.events;

/**
 * A CDI event to inform that a new row was added.
 *
 * (This app doesn't really persist anything, so it wasn't really added to anything.)
 */
public class NewRowAddedEvent {
    private final String rowId;
    private final String state;

    public NewRowAddedEvent(String rowId, String state) {
        this.rowId = rowId;
        this.state = state;
    }

    public String getRowId() {
        return rowId;
    }

    public String getState() {
        return state;
    }
}
