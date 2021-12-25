package pl.marcinchwedczuk.elfviewer.gui.mainwindow;

import javafx.beans.property.StringProperty;
import javafx.scene.control.TableView;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.BaseRenderer;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.NothingRenderer;

import java.util.function.Consumer;

public class RenderDataAction<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        >
{
    private final String displayName;
    private final BaseRenderer<?, NATIVE_WORD> renderer;

    public RenderDataAction(String displayName,
                            BaseRenderer<?, NATIVE_WORD> renderer) {
        this.displayName = displayName;
        this.renderer = renderer;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public void mountView(TableView<Object> tableView,
                          StringProperty filter) {
        renderer.filterPhaseProperty().bind(filter);
        renderer.renderDataOn(tableView);
    }

    public void unmountView() {
        renderer.filterPhaseProperty().unbind();
        renderer.filterPhaseProperty().setValue("");
    }
}
