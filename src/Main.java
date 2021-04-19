import LineFollowMother.LineFollowModel;
import ManualControl.ManualModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.script.*;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Main extends Application {
    private Stage stage;
    public static ManualModel manualModel;
    public static LineFollowModel lineFollowModel;
    public static Main main;

    public static void main(String[] args) {
        System.out.println("Hello from mother!");
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        this.main = this;
        this.stage = stage;

        Parent launchMenuView = FXMLLoader.load(getClass().getResource("LaunchMenuView.fxml"));
        Scene launchScene = new Scene(launchMenuView);

        this.stage.setTitle("IZZY");
        this.stage.sizeToScene();
        this.stage.setScene(launchScene);
        this.stage.setResizable(true);
        this.stage.show();
    }

    @Override
    public void stop() throws Exception {
        LineFollowModel.getCurrentInstance().closeApplication();
        super.stop();
    }

    public void switchScenes(final Scene scene, final Object model) {
        this.stage.setScene(scene);
        if (model instanceof ManualModel) {
            manualModel = (ManualModel) model;
        } else if (model instanceof LineFollowModel) {
            lineFollowModel = (LineFollowModel) model;
        } else {
            throw new RuntimeException();
        }
    }

    public static Main getInstance() {
        return main;
    }

}
