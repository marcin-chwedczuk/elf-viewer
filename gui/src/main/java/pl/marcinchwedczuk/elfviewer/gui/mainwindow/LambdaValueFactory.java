package pl.marcinchwedczuk.elfviewer.gui.mainwindow;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.util.Objects;
import java.util.function.Function;

public class LambdaValueFactory<S,T> implements Callback<TableColumn.CellDataFeatures<S,T>, ObservableValue<T>>  {
    private final Function<S, T> mapper;

    public LambdaValueFactory(Function<S, T> mapper) {
        this.mapper = Objects.requireNonNull(mapper);
    }

    @Override
    public ObservableValue<T> call(TableColumn.CellDataFeatures<S, T> param) {
        return new ReadOnlyObjectWrapper<>(mapper.apply(param.getValue()));
    }
}
