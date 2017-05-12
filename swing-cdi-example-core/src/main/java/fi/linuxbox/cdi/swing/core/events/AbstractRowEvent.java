package fi.linuxbox.cdi.swing.core.events;

/**
 * A base class for row related CDI events.
 */
public class AbstractRowEvent {
    private final String rowId;
    private final String state;

    public AbstractRowEvent(String rowId, String state) {
        this.rowId = rowId;
        this.state = state;
    }

    public String getRowId() {
        return rowId;
    }

    public String getState() {
        return state;
    }

    public String[] asRow() {
        return new String[] { rowId, state };
    }
}
