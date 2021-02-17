import LineFollowMother.LineFollowModel;
import ManualControl.ManualModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;


public class LaunchMenuController {

    @FXML
    ChoiceBox<String> interfaceSelection;

    @FXML
    Label noneChosenErrorLabel;

    @FXML
    TextField izzyIpAddress;

    @FXML
    public void initialize() {
        interfaceSelection.getItems().addAll("Line Following", "Manual Control");
    }

    public void chooseAction(ActionEvent e) {
        try {
            if (interfaceSelection.getValue().equals("Line Following")) {
                LineFollowModel model = new LineFollowModel(izzyIpAddress.getText());
                Parent lineFollowView = FXMLLoader.load(getClass().getResource("LineFollowMother/MotherLineFollowView.fxml"));
                Main.getInstance().switchScenes(new Scene(lineFollowView), model);
            } else if (interfaceSelection.getValue().equals("Manual Control")){
                ManualModel model = new ManualModel();
                model.startListening();
                Parent manualView = FXMLLoader.load(getClass().getResource("ManualControl/MotherManualView.fxml"));
                Main.getInstance().switchScenes(new Scene(manualView), model);
            } else {
                noneChosenErrorLabel.setVisible(true);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
