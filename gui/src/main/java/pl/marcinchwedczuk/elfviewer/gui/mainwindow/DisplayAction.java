package pl.marcinchwedczuk.elfviewer.gui.mainwindow;

public class DisplayAction {
    private final String displayName;
    private final Runnable action;

    public DisplayAction(String displayName,
                         Runnable action) {
        this.displayName = displayName;
        this.action = action;
    }

    public DisplayAction(String displayName) {
        this(displayName, () -> { });
    }

    @Override
    public String toString() {
        return displayName;
    }

    public void runAction() {
        action.run();
    }
}
