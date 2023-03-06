package lineFollow.popups;

import com.illposed.osc.OSCMessage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import lineFollow.LineFollowModel;

import java.io.IOException;

import static izzyCommunication.lineFollowing.OSCAddresses.FOLLOW_LINE_THRESHOLD;

public class ThresholdMenuController {
    LineFollowModel model;
    @FXML
    Text thresholdErrorMessage;
    @FXML
    TextField leftThresholdTextField;
    @FXML
    TextField centerThresholdTextField;
    @FXML
    TextField rightThresholdTextField;

    @FXML
    private void initialize() {
        try {
            this.model = LineFollowModel.getCurrentInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void setThresholdButtonClicked(ActionEvent e) {
        Platform.runLater(() -> thresholdErrorMessage.setVisible(false));
        OSCMessage outgoingMessage = new OSCMessage();
        try {
            outgoingMessage.addArgument(Integer.parseInt(leftThresholdTextField.getText()));
            outgoingMessage.addArgument(Integer.parseInt(centerThresholdTextField.getText()));
            outgoingMessage.addArgument(Integer.parseInt(rightThresholdTextField.getText()));
            model.sendMessage(outgoingMessage, FOLLOW_LINE_THRESHOLD);
            model.getLineFollowController().closeThresholdPopup();
        } catch (NumberFormatException exception) {
            Platform.runLater(() -> thresholdErrorMessage.setVisible(true));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
