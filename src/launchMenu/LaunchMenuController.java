package launchMenu;

import lineFollow.LineFollowModel;
import ManualControl.ManualModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;


public class LaunchMenuController {

    @FXML
    ChoiceBox<String> interfaceSelection;

    @FXML
    Label launchErrorLabel;

    @FXML
    TextField izzyIpAddress;

    @FXML
    public void initialize() {
        interfaceSelection.getItems().addAll("Line Following", "Manual Control");
    }

    public void chooseAction(ActionEvent e) {
        try {
            final InetAddress inetAddress;
            try {
                if (izzyIpAddress.getText() != null && !izzyIpAddress.getText().isBlank()) {
                    inetAddress = InetAddress.getByName(izzyIpAddress.getText());
                } else {
                    launchErrorLabel.setText("Invalid IP Address");
                    launchErrorLabel.setVisible(true);
                    return;
                }
            } catch (UnknownHostException exception) {
                launchErrorLabel.setText("Invalid IP Address");
                launchErrorLabel.setVisible(true);
                return;
            }

            if (interfaceSelection.getValue().equals("Line Following")) {
                final LineFollowModel model = new LineFollowModel(inetAddress);
                final Parent lineFollowView = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../LineFollow/LineFollowView.fxml")));
                final Scene currentScene = ((Node) e.getSource()).getScene();
                switchScenes(currentScene, new Scene(lineFollowView));
            } else if (interfaceSelection.getValue().equals("Manual Control")){
                final ManualModel model = new ManualModel();
                final Parent manualView = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../ManualControl/MotherManualView.fxml")));
                final Scene currentScene = ((Node) e.getSource()).getScene();
                switchScenes(currentScene, new Scene(manualView));
            } else {
                launchErrorLabel.setText("Please select an interface");
                launchErrorLabel.setVisible(true);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
            launchErrorLabel.setText("An unknown error has occurred");
            launchErrorLabel.setVisible(true);
        }
    }

    public void switchScenes(final Scene currentScene, final Scene nextScene) {
        final Stage currentStage = (Stage) currentScene.getWindow();
        currentStage.setScene(nextScene);
    }

}
