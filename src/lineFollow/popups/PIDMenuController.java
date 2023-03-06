package lineFollow.popups;

import com.illposed.osc.OSCMessage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import lineFollow.LineFollowModel;

import java.io.IOException;

import static izzyCommunication.lineFollowing.OSCAddresses.FOLLOW_LINE_TUNE;

public class PIDMenuController {
    LineFollowModel model;
    @FXML
    TextField kpTextField;
    @FXML
    TextField kiTextField;
    @FXML
    TextField kdTextField;
    @FXML
    Text kErrorMessage;

    @FXML
    private void initialize() {
        try {
            this.model = LineFollowModel.getCurrentInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void setTuningButtonClicked(ActionEvent e) {
        Platform.runLater(() -> kErrorMessage.setVisible(false));
        OSCMessage outgoingMessage = new OSCMessage();
        try {
            outgoingMessage.addArgument(Double.parseDouble(kpTextField.getText())); //kp
            outgoingMessage.addArgument(Double.parseDouble(kiTextField.getText())); //ki
            outgoingMessage.addArgument(Double.parseDouble(kdTextField.getText())); //kd
            model.sendMessage(outgoingMessage, FOLLOW_LINE_TUNE);
            model.getLineFollowController().closePIDPopup();
        } catch (NumberFormatException exception) {
            Platform.runLater(() -> kErrorMessage.setVisible(true));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
