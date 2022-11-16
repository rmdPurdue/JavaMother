package lineFollow.mapping;

import lineFollow.LineFollowController;
import javafx.application.Platform;
import javafx.scene.chart.XYChart;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class MapLoop {
    private LineFollowController controller;
    private XYChart.Series<Double, Double> izzyPath;
    private double xPosition;
    private double yPosition;
    private int previousDriveP;
    private int previousTurnP;
    private final Object syncLock;

    public MapLoop() {
        xPosition = 0;
        yPosition = 0;
        previousDriveP = 0;
        previousTurnP = 0;
        syncLock = new Object();

        this.izzyPath = new XYChart.Series<Double, Double>();
        izzyPath.setName("IZZY Path");
    }

    public void setLineFollowController(LineFollowController controller) {
        this.controller = controller;
        Platform.runLater(() -> this.controller.getMapPlot().getData().add(izzyPath));
    }

    /**
     *
     *
     * @param driveP Position of Drive in MM
     * @param driveS Speed of Drive in MM per Second
     * @param turnP Position of Turn in Degrees
     * @param turnS Speed of Turn in Degrees per Second
     */
    public void mapData(int driveP, int driveS, int turnP, int turnS) {
        calculateXAndY(driveP, turnP);
        // Avoid throwing IllegalStateException by running from a non-JavaFX thread.
        Platform.runLater(() -> {
            try {
//                final Path path = new Path();
//                path.setStrokeWidth(2);
//                path.getElements().addAll(new MoveTo(oldXPosition, oldYPosition), new LineTo(xPosition, yPosition));

                // Add the path to plot children and keep a reference for later use.
                synchronized (syncLock) {
                    izzyPath.getData().add(new XYChart.Data<>(xPosition, yPosition));
//                    controller.getMapPlot()getMapPlot.updatePath(path);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void clearPlotData() {
        synchronized (syncLock) {
            izzyPath.getData().clear();
        }
    }

    private void calculateXAndY(int driveP, int turnP) {
        xPosition += (driveP - previousDriveP) * Math.sin(Math.toRadians(turnP));
        yPosition += (driveP - previousDriveP) * Math.cos(Math.toRadians(turnP));
        previousDriveP = driveP;
        previousTurnP = turnP;
    }

}
