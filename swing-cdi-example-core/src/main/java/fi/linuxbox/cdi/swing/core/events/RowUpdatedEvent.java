package fi.linuxbox.cdi.swing.core.events;

/**
 * Created by vmj on 02/03/2017.
 */
public class RowUpdatedEvent {
    private final String rowId;
    private final String state;

    public RowUpdatedEvent(String rowId, String state) {
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
