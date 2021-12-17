package LineFollow;

import com.illposed.osc.OSCMessage;
import IZZYCommunication.Heartbeat.IZZYStatus;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;

import static IZZYCommunication.LineFollowing.OSCAddresses.FOLLOW_LINE_SPEED;
import static IZZYCommunication.LineFollowing.OSCAddresses.FOLLOW_LINE_STATE;
import static IZZYCommunication.LineFollowing.OSCAddresses.FOLLOW_LINE_STOP;
import static IZZYCommunication.LineFollowing.OSCAddresses.FOLLOW_LINE_TUNE;
import static IZZYCommunication.LineFollowing.OSCAddresses.STOP_PROCESSING;

public class LineFollowController {

    LineFollowModel model;
    IZZYStatus currentStatus;
    Timeline statusBlinkerTimeline;

    @FXML
    Text driveSpeedValue;
    @FXML
    Text errorCorrectionSetPointValue;
    @FXML
    Text errorAngleValue;
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
    Slider wirePositionSlider;
    @FXML
    Button eStopButton;
    @FXML
    Button startButton;
    @FXML
    Button motionStopButton;
    @FXML
    Slider speedControlSlider;
    @FXML
    TextField kpTextField;
    @FXML
    TextField kiTextField;
    @FXML
    TextField kdTextField;
    @FXML
    Button setTuningButton;
    @FXML
    Text kErrorMessage;
    @FXML
    Circle connectionStatus;
    @FXML
    Text connectionStatusText;

    @FXML
    private void initialize() {
        try {
            this.model = LineFollowModel.getCurrentInstance();
            this.currentStatus = IZZYStatus.UNVERIFIED;
            this.statusBlinkerTimeline = new Timeline();
            model.setLineFollowController(this);
            model.startListening();
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
    public void setTuningButtonClicked(ActionEvent e) {
        kErrorMessage.setVisible(false);
        OSCMessage outgoingMessage = new OSCMessage();
        try {
            outgoingMessage.addArgument(Double.parseDouble(kpTextField.getText())); //kp
            outgoingMessage.addArgument(Double.parseDouble(kiTextField.getText())); //ki
            outgoingMessage.addArgument(Double.parseDouble(kdTextField.getText())); //kd
            model.sendMessage(outgoingMessage, FOLLOW_LINE_TUNE);
        } catch (NumberFormatException exception) {
            kErrorMessage.setVisible(true);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
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

    public void setDriveSpeed(int driveSpeed) {
        driveSpeedValue.setText(Integer.toString(driveSpeed));
    }

    public void setErrorCorrectionSetPoint(double errorCorrectionSetPoint) {
        errorCorrectionSetPointValue.setText(Double.toString(errorCorrectionSetPoint));
    }

    public void setErrorAngle(double errorAngle) {
        errorAngleValue.setText(Double.toString(errorAngle));
    }

    public void setKP(double kP) {
        kpValue.setText(Double.toString(kP));
    }

    public void setKI(double kI) {
        kiValue.setText(Double.toString(kI));
    }

    public void setKD(double kD) {
        kdValue.setText(Double.toString(kD));
    }

    public void setCurrentState(String currentState) {
        currentStateValue.setText(currentState);
    }

    public void setExceptions(String exceptions) {
        exceptionsValue.setText(exceptions);
    }

    public void toggleLeftSensor(boolean reading) {
        if (reading) {
            sensorState0.setFill(javafx.scene.paint.Color.rgb(30, 255, 144));
        } else {
            sensorState0.setFill(javafx.scene.paint.Color.rgb(30, 144, 255));
        }
    }

    public void toggleCenterSensor(boolean reading) {
        if (reading) {
            sensorState1.setFill(javafx.scene.paint.Color.rgb(30, 255, 144));
        } else {
            sensorState1.setFill(javafx.scene.paint.Color.rgb(30, 144, 255));
        }
    }

    public void toggleRightSensor(boolean reading) {
        if (reading) {
            sensorState2.setFill(javafx.scene.paint.Color.rgb(30, 255, 144));
        } else {
            sensorState2.setFill(javafx.scene.paint.Color.rgb(30, 144, 255));
        }
    }

    public void setWirePosition(boolean left, boolean center, boolean right) {
        if (left && !center && !right) {
            wirePositionSlider.setValue(0.1);
        } else if (left && center && !right) {
            wirePositionSlider.setValue(0.3);
        } else if (!left && center && !right) {
            wirePositionSlider.setValue(0.5);
        } else if (!left && center && right) {
            wirePositionSlider.setValue(0.7);
        } else if (!left && !center && right) {
            wirePositionSlider.setValue(0.9);
        }
    }

    public void toggleConnectionStatus(IZZYStatus status) {
        if (status == currentStatus) {
            return;
        }

        switch (status) {
            case AVAILABLE:
                connectionStatus.setFill(javafx.scene.paint.Color.rgb(30, 255, 144));
                connectionStatusText.setText("Connected");
                break;
            case MOVING:
                connectionStatusText.setText("Moving");
                statusBlinkerTimeline.stop();
                statusBlinkerTimeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> connectionStatus.setFill(javafx.scene.paint.Color.rgb(0, 141, 69))),
                        new KeyFrame(Duration.seconds(1), evt -> connectionStatus.setFill(javafx.scene.paint.Color.rgb(30, 255, 144))));
                statusBlinkerTimeline.setCycleCount(Animation.INDEFINITE);
                statusBlinkerTimeline.play();
                break;
            case MISSING:
                connectionStatusText.setText("Missing");
                connectionStatus.setFill(javafx.scene.paint.Color.rgb(255, 74, 74));
                break;
            case BROKEN:
                connectionStatusText.setText("Critical Error");
                statusBlinkerTimeline.stop();
                statusBlinkerTimeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> connectionStatus.setFill(javafx.scene.paint.Color.rgb(255, 74, 74))),
                        new KeyFrame(Duration.seconds(1), evt -> connectionStatus.setFill(javafx.scene.paint.Color.rgb(146, 42, 42))));
                statusBlinkerTimeline.setCycleCount(Animation.INDEFINITE);
                statusBlinkerTimeline.play();
                break;
            default:
                connectionStatusText.setText("Unknown");
                connectionStatus.setFill(javafx.scene.paint.Color.rgb(0, 0, 0));
                break;
        }
    }

}
