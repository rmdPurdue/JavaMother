package lineFollow;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import com.illposed.osc.OSCMessage;
import izzyCommunication.heartbeat.IZZYStatus;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

import static izzyCommunication.lineFollowing.OSCAddresses.*;

public class LineFollowController {

    LineFollowModel model;
    IZZYStatus currentStatus;
    Timeline statusBlinkerTimeline;

    Stage pidStage;
    Stage rangeStage;
    Stage thresholdStage;

    @FXML
    Text driveSpeedValue;
    @FXML
    Text errorCorrectionSetPointValue;
    @FXML
    Text errorAngleValue;
    @FXML
    Text wheelDSpeedValue;
    @FXML
    Text wheelDPositionValue;
    @FXML
    Text wheelTSpeedValue;
    @FXML
    Text wheelTPositionValue;
    @FXML
    Text kpValue;
    @FXML
    Text kiValue;
    @FXML
    Text kdValue;
    @FXML
    Text currentStateValue;
    @FXML
    Text exceptionsValue;
    @FXML
    Circle sensorState0;
    @FXML
    Circle sensorState1;
    @FXML
    Circle sensorState2;
    @FXML
    Text rightSensorAnalogLabel;
    @FXML
    Text rightSensorThresholdLabel;
    @FXML
    Text centerSensorAnalogLabel;
    @FXML
    Text centerSensorThresholdLabel;
    @FXML
    Text leftSensorAnalogLabel;
    @FXML
    Text leftSensorThresholdLabel;
    @FXML
    Slider wirePositionSlider;
    @FXML
    Slider speedControlSlider;
    @FXML
    Circle connectionStatus;
    @FXML
    Text connectionStatusText;
    @FXML
    TextArea logTextArea;
    @FXML
    ScatterChart<Double, Integer> sensorPlot;
    @FXML
    LineChart<Double, Double> mapPlot;
    @FXML
    BorderPane lineFollowPane;

    @FXML
    private void initialize() {
        try {
            this.model = LineFollowModel.getCurrentInstance();
            this.currentStatus = IZZYStatus.UNVERIFIED;
            this.statusBlinkerTimeline = new Timeline();
            model.setLineFollowController(this);
            model.startListening();
            mapPlot.setAxisSortingPolicy(LineChart.SortingPolicy.NONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void startButtonClick(ActionEvent e) {
        OSCMessage outgoingMessage = new OSCMessage();
        outgoingMessage.addArgument("start");
        outgoingMessage.addArgument((int) (speedControlSlider.getValue() + 0.5));
        try {
            model.sendMessage(outgoingMessage, FOLLOW_LINE_STATE);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    public void motionStopButtonClicked(ActionEvent e) {
        OSCMessage outgoingMessage = new OSCMessage();
        outgoingMessage.addArgument("stop");
        outgoingMessage.addArgument((int) (speedControlSlider.getValue() + 0.5));
        try {
            model.sendMessage(outgoingMessage, FOLLOW_LINE_STATE);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    public void eStopButtonClicked(ActionEvent e) {
        OSCMessage outgoingMessage = new OSCMessage();
        outgoingMessage.addArgument("eStop");
        try {
            model.sendMessage(outgoingMessage, FOLLOW_LINE_STOP);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    public void speedControlSliderDragged() {
        OSCMessage outgoingMessage = new OSCMessage();
        outgoingMessage.addArgument((int) (speedControlSlider.getValue() + 0.5));
        try {
            model.sendMessage(outgoingMessage, FOLLOW_LINE_SPEED);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    public void clearPlotButtonClicked(ActionEvent e) {
        model.clearPlotData();
        model.restartLogging();
    }

    @FXML
    public void stopIzzyProcessingButtonClicked(ActionEvent e) {
        OSCMessage outgoingMessage = new OSCMessage();
        try {
            model.sendMessage(outgoingMessage, STOP_PROCESSING);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    public void resetSystemButtonClicked(ActionEvent e) {
        OSCMessage outgoingMessage = new OSCMessage();
        try {
            model.restartLogging();
            model.clearPlotData();
            model.sendMessage(outgoingMessage, RESET_SYSTEM);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    public void closeApplication(ActionEvent e) {
        model.closeApplication();
        System.exit(0);
    }

    @FXML
    public void openRangePopup(ActionEvent e) {
        try {
            final Parent rangePopupView = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("./popups/RangeMenuView.fxml")));
            rangeStage = new Stage();
            rangeStage.setTitle("Range Tuning Menu");
            rangeStage.setResizable(false);
            rangeStage.initModality(Modality.APPLICATION_MODAL);
            Scene s = new Scene(rangePopupView);
            rangeStage.setScene(s);
            rangeStage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void openPIDPopup(ActionEvent e) {
        try {
            final Parent rangePopupView = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("./popups/PIDMenuView.fxml")));
            pidStage = new Stage();
            pidStage.setTitle("PID Tuning Menu");
            pidStage.setResizable(false);
            pidStage.initModality(Modality.APPLICATION_MODAL);
            Scene s = new Scene(rangePopupView);
            pidStage.setScene(s);
            pidStage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void openThresholdPopup(ActionEvent e) {
        try {
            final Parent rangePopupView = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("./popups/ThresholdMenuView.fxml")));
            thresholdStage = new Stage();
            thresholdStage.setTitle("Threshold Tuning Menu");
            thresholdStage.setResizable(false);
            thresholdStage.initModality(Modality.APPLICATION_MODAL);
            Scene s = new Scene(rangePopupView);
            thresholdStage.setScene(s);
            thresholdStage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void closeRangePopup() {
        rangeStage.close();
    }

    @FXML
    public void closeThresholdPopup() {
        thresholdStage.close();
    }

    @FXML
    public void closePIDPopup() {
        pidStage.close();
    }

    public void setDriveSpeed(int driveSpeed) {
        Platform.runLater(() -> driveSpeedValue.setText(Integer.toString(driveSpeed)));
    }

    public void setErrorCorrectionSetPoint(double errorCorrectionSetPoint) {
        Platform.runLater(() -> errorCorrectionSetPointValue.setText(Double.toString(errorCorrectionSetPoint)));
    }

    public void setErrorAngle(double errorAngle) {
        Platform.runLater(() -> errorAngleValue.setText(Double.toString(errorAngle)));
    }

    public void setWheelDSpeed(int wheelDSpeed) {
        Platform.runLater(() -> wheelDSpeedValue.setText(Integer.toString(wheelDSpeed)));
    }

    public void setWheelDPosition(int wheelDPosition) {
        Platform.runLater(() -> wheelDPositionValue.setText(Integer.toString(wheelDPosition)));
    }

    public void setWheelTSpeed(int wheelTSpeed) {
        Platform.runLater(() -> wheelTSpeedValue.setText(Integer.toString(wheelTSpeed)));
    }

    public void setWheelTPosition(int wheelTPosition) {
        Platform.runLater(() -> wheelTPositionValue.setText(Double.toString(wheelTPosition)));
    }

    public void setKP(double kP) {
        Platform.runLater(() -> kpValue.setText(Double.toString(kP)));
    }

    public void setKI(double kI) {
        Platform.runLater(() -> kiValue.setText(Double.toString(kI)));
    }

    public void setKD(double kD) {
        Platform.runLater(() -> kdValue.setText(Double.toString(kD)));
    }

    public void setLeftThresholdValue(int threshold) {
        Platform.runLater(() -> leftSensorThresholdLabel.setText(Integer.toString(threshold)));
    }

    public void setCenterThresholdValue(int threshold) {
        Platform.runLater(() -> centerSensorThresholdLabel.setText(Integer.toString(threshold)));
    }

    public void setRightThresholdValue(int threshold) {
        Platform.runLater(() -> rightSensorThresholdLabel.setText(Integer.toString(threshold)));
    }

    public void setCurrentState(String currentState) {
        Platform.runLater(() -> currentStateValue.setText(currentState));
    }

    public void setExceptions(String exceptions) {
        Platform.runLater(() -> exceptionsValue.setText(exceptions));
    }

    public void toggleLeftSensor(boolean reading, int analogValue) {
        if (reading) {
            Platform.runLater(() -> sensorState0.setFill(javafx.scene.paint.Color.rgb(30, 255, 144)));
        } else {
            Platform.runLater(() -> sensorState0.setFill(javafx.scene.paint.Color.rgb(30, 144, 255)));
        }
        Platform.runLater(() -> leftSensorAnalogLabel.setText(Integer.toString(analogValue)));
    }

    public void toggleCenterSensor(boolean reading, int analogValue) {
        if (reading) {
            Platform.runLater(() -> sensorState1.setFill(javafx.scene.paint.Color.rgb(30, 255, 144)));
        } else {
            Platform.runLater(() -> sensorState1.setFill(javafx.scene.paint.Color.rgb(30, 144, 255)));
        }
        Platform.runLater(() -> centerSensorAnalogLabel.setText(Integer.toString(analogValue)));
    }

    public void toggleRightSensor(boolean reading, int analogValue) {
        if (reading) {
            Platform.runLater(() -> sensorState2.setFill(javafx.scene.paint.Color.rgb(30, 255, 144)));
        } else {
            Platform.runLater(() -> sensorState2.setFill(javafx.scene.paint.Color.rgb(30, 144, 255)));
        }
        Platform.runLater(() -> rightSensorAnalogLabel.setText(Integer.toString(analogValue)));
    }

    public void setWirePosition(boolean left, boolean center, boolean right) {
        if (left && !center && !right) {
            Platform.runLater(() -> wirePositionSlider.setValue(0.1));
        } else if (left && center && !right) {
            Platform.runLater(() -> wirePositionSlider.setValue(0.3));
        } else if (!left && center && !right) {
            Platform.runLater(() -> wirePositionSlider.setValue(0.5));
        } else if (!left && center && right) {
            Platform.runLater(() -> wirePositionSlider.setValue(0.7));
        } else if (!left && !center && right) {
            Platform.runLater(() -> wirePositionSlider.setValue(0.9));
        }
    }

    public void toggleConnectionStatus(IZZYStatus status) {
        if (status == currentStatus) {
            return;
        }

        switch (status) {
            case AVAILABLE:
                Platform.runLater(() -> {
                    connectionStatus.setFill(javafx.scene.paint.Color.rgb(30, 255, 144));
                    connectionStatusText.setText("Connected");
                });
                break;
            case MOVING:
                Platform.runLater(() -> {
                    connectionStatusText.setText("Moving");
                    statusBlinkerTimeline.stop();
                    statusBlinkerTimeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> connectionStatus.setFill(javafx.scene.paint.Color.rgb(0, 141, 69))),
                            new KeyFrame(Duration.seconds(1), evt -> connectionStatus.setFill(javafx.scene.paint.Color.rgb(30, 255, 144))));
                    statusBlinkerTimeline.setCycleCount(Animation.INDEFINITE);
                    statusBlinkerTimeline.play();
                });
                break;
            case MISSING:
                Platform.runLater(() -> {
                    connectionStatusText.setText("Missing");
                    connectionStatus.setFill(javafx.scene.paint.Color.rgb(255, 74, 74));
                });
                break;
            case BROKEN:
                Platform.runLater(() -> {
                    connectionStatusText.setText("Critical Error");
                    statusBlinkerTimeline.stop();
                    statusBlinkerTimeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> connectionStatus.setFill(javafx.scene.paint.Color.rgb(255, 74, 74))),
                            new KeyFrame(Duration.seconds(1), evt -> connectionStatus.setFill(javafx.scene.paint.Color.rgb(146, 42, 42))));
                    statusBlinkerTimeline.setCycleCount(Animation.INDEFINITE);
                    statusBlinkerTimeline.play();
                });
                break;
            default:
                Platform.runLater(() -> {
                    connectionStatusText.setText("Unknown");
                    connectionStatus.setFill(javafx.scene.paint.Color.rgb(0, 0, 0));
                });
                break;
        }
    }

    public void appendLogMessage(String message) {
        Platform.runLater(() -> logTextArea.appendText(message + "\n"));
    }

    public ScatterChart<Double, Integer> getSensorPlot() {
        return sensorPlot;
    }

    public LineChart<Double, Double> getMapPlot() {
        return mapPlot;
    }
}
