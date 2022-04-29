package LineFollow.Plotting;

import LineFollow.LineFollowController;
import javafx.application.Platform;
import javafx.scene.chart.XYChart;

public class PlotLoop extends Thread {

    private LineFollowController controller;
    private XYChart.Series<Double, Integer> series1;
    private XYChart.Series<Double, Integer> series2;
    private XYChart.Series<Double, Integer> series3;
    private double startTime;
    private final Object syncLock;

    public PlotLoop() {
        startTime = -1.0;
        syncLock = new Object();

        this.series1 = new XYChart.Series<Double, Integer>();
        series1.setName("Left Sensor");

        this.series2 = new XYChart.Series<Double, Integer>();
        series2.setName("Center Sensor");

        this.series3 = new XYChart.Series<Double, Integer>();
        series3.setName("Right Sensor");
    }

    public void setLineFollowController(LineFollowController controller) {
        this.controller = controller;
        Platform.runLater(() -> this.controller.getScatterChart().getData().addAll(series1, series2, series3));
    }

    public void plotData(int left, int center, int right) {
        long currentTime = System.currentTimeMillis();
        if (startTime == -1) {
            startTime = currentTime;
        }
        // Avoid throwing IllegalStateException by running from a non-JavaFX thread.
        Platform.runLater(() -> {
            try {
                synchronized (syncLock) {
                    series1.getData().add(new XYChart.Data<>(convertTo2Decimals(currentTime), left));
                    series2.getData().add(new XYChart.Data<>(convertTo2Decimals(currentTime), center));
                    series3.getData().add(new XYChart.Data<>(convertTo2Decimals(currentTime), right));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void clearPlotData() {
        synchronized (syncLock) {
            startTime = System.currentTimeMillis();
            series1.getData().clear();
            series2.getData().clear();
            series3.getData().clear();
        }
    }

    private double convertTo2Decimals(long currentTime) {
        synchronized (syncLock) {
            double currentTimeDouble = (currentTime - startTime) / 10.0; // remove thousandths place
            currentTimeDouble = Math.floor(currentTimeDouble);  // limit to 2 decimals
            currentTimeDouble = currentTimeDouble / 100.0; // convert to seconds
            return currentTimeDouble;
        }
    }

}
