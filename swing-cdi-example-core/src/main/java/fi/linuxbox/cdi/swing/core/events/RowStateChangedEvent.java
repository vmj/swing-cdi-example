package fi.linuxbox.cdi.swing.core.events;

/**
 * Created by vmj on 02/03/2017.
 */
public class RowStateChangedEvent {
    private final String rowId;
    private final String state;

    public RowStateChangedEvent(String rowId, String state) {
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
