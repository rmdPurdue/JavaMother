package lineFollow.mapping;

import javafx.beans.NamedArg;
import javafx.collections.ObservableList;
import javafx.scene.chart.Axis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.shape.Path;

public class MapPlot extends ScatterChart<Double, Double> {
    public MapPlot(@NamedArg("xAxis") Axis<Double> axis, @NamedArg("yAxis") Axis<Double> axis1) {
        super(axis, axis1);
    }

    public MapPlot(@NamedArg("xAxis") Axis<Double> axis, @NamedArg("yAxis") Axis<Double> axis1, ObservableList<XYChart.Series<Double, Double>> observableList) {
        super(axis, axis1, observableList);
    }

    public void updatePath(Path path) {
        this.getChildren().add(path);
    }
}
