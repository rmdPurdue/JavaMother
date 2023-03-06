package lineFollow.popups;

import com.illposed.osc.OSCMessage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import lineFollow.LineFollowModel;

import java.io.IOException;

import static izzyCommunication.lineFollowing.OSCAddresses.SET_SENSOR_RANGES;

public class RangeMenuController {
    LineFollowModel model;
    @FXML
    TextField sensor0MinTextField;
    @FXML
    TextField sensor0MaxTextField;
    @FXML
    TextField sensor1MinTextField;
    @FXML
    TextField sensor1MaxTextField;
    @FXML
    Text rangeErrorMessage;

    @FXML
    private void initialize() {
        try {
            this.model = LineFollowModel.getCurrentInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void setSensorRanges(ActionEvent e) {
        Platform.runLater(() -> rangeErrorMessage.setVisible(false));
        OSCMessage outgoingMessage = new OSCMessage();
        try {
            outgoingMessage.addArgument(Integer.parseInt(sensor0MinTextField.getText()));
            outgoingMessage.addArgument(Integer.parseInt(sensor0MaxTextField.getText()));
            outgoingMessage.addArgument(Integer.parseInt(sensor1MinTextField.getText()));
            outgoingMessage.addArgument(Integer.parseInt(sensor1MaxTextField.getText()));
            model.sendMessage(outgoingMessage, SET_SENSOR_RANGES);
            model.getLineFollowController().closeRangePopup();
        } catch (NumberFormatException exception) {
            Platform.runLater(() -> rangeErrorMessage.setVisible(true));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
