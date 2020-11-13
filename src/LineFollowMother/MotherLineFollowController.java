package LineFollowMother;

import com.illposed.osc.OSCMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.io.IOException;

public class MotherLineFollowController {

    private static MotherLineFollowController currentInstance;

    @FXML
    Text speedSetPointValue;
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

    public MotherLineFollowController() {
        currentInstance = this;
    }

    @FXML
    private void initialize() {
        try {
            LineFollowModel.getCurrentInstance().startListening();
        } catch (Exception e) {
            e.printStackTrace(); 
        }
    }

    public static MotherLineFollowController getCurrentInstance() {
        return currentInstance;
    }

    @FXML
    public void startButtonClick(ActionEvent e) {
        OSCMessage outgoingMessage = new OSCMessage();
        outgoingMessage.setAddress("/IZZY/FollowLineState");
        outgoingMessage.addArgument("start");
        outgoingMessage.addArgument(30);
        try {
            LineFollowModel.getCurrentInstance().sendMessage(outgoingMessage);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    public void motionStopButtonClicked(ActionEvent e) {
        OSCMessage outgoingMessage = new OSCMessage();
        outgoingMessage.setAddress("/IZZY/FollowLineState");
        outgoingMessage.addArgument("stop");
        outgoingMessage.addArgument(30);
        try {
            LineFollowModel.getCurrentInstance().sendMessage(outgoingMessage);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    public void eStopButtonClicked(ActionEvent e) {
        OSCMessage outgoingMessage = new OSCMessage();
        outgoingMessage.setAddress("/IZZY/eStop");
        outgoingMessage.addArgument("eStop");
        try {
            LineFollowModel.getCurrentInstance().sendMessage(outgoingMessage);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    public void speedControlSliderDragged(ActionEvent e) {
        OSCMessage outgoingMessage = new OSCMessage();
        outgoingMessage.setAddress("/IZZY/FollowLineSpeed");
        outgoingMessage.addArgument((int) (speedControlSlider.getValue() + 0.5));
        try {
            LineFollowModel.getCurrentInstance().sendMessage(outgoingMessage);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    public void setTuningButtonClicked(ActionEvent e) {
        OSCMessage outgoingMessage = new OSCMessage();
        outgoingMessage.setAddress("/IZZY/FollowLineTune");
        outgoingMessage.addArgument(1.0); //kp
        outgoingMessage.addArgument(1.0); //ki
        outgoingMessage.addArgument(1.0); //kd
        try {
            LineFollowModel.getCurrentInstance().sendMessage(outgoingMessage);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void setSpeedSetPoint(int speedSetPoint) {
        speedSetPointValue.setText(Integer.toString(speedSetPoint));
    }

    public void setDriveSpeed(int driveSpeed) {
        driveSpeedValue.setText(Integer.toString(driveSpeed));
    }

    public void setErrorCorrectionSetPoint(int errorCorrectionSetPoint) {
        errorCorrectionSetPointValue.setText(Integer.toString(errorCorrectionSetPoint));
    }

    public void setErrorAngle(int errorAngle) {
        errorAngleValue.setText(Integer.toString(errorAngle));
    }

    public void setKP(int kP) {
        kpValue.setText(Integer.toString(kP));
    }

    public void setKI(int kI) {
        kiValue.setText(Integer.toString(kI));
    }

    public void setKD(int kD) {
        kdValue.setText(Integer.toString(kD));
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

}
