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
import java.net.InetAddress;

public class MotherLineFollowController {

    private static MotherLineFollowController currentInstance;

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
    public void speedControlSliderDragged() {
        System.out.println("Speed: " + (int) (speedControlSlider.getValue() + 0.5));
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
        kErrorMessage.setVisible(false);
        OSCMessage outgoingMessage = new OSCMessage();
        outgoingMessage.setAddress("/IZZY/FollowLineTune");
        try {
            outgoingMessage.addArgument(Double.parseDouble(kpTextField.getText())); //kp
            outgoingMessage.addArgument(Double.parseDouble(kiTextField.getText())); //ki
            outgoingMessage.addArgument(Double.parseDouble(kdTextField.getText())); //kd
            LineFollowModel.getCurrentInstance().sendMessage(outgoingMessage);
        } catch (NumberFormatException exception) {
            kErrorMessage.setVisible(true);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    public void stopIzzyProcessingButtonClicked(ActionEvent e) {
        OSCMessage outgoingMessage = new OSCMessage();
        outgoingMessage.setAddress("/IZZY/StopProcessing");
        try {
            LineFollowModel.getCurrentInstance().sendMessage(outgoingMessage);
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

}
