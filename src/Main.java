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

    public static void main(String[] args) throws ScriptException, IOException, NoSuchMethodException {
        System.out.println("Hello from mother!");
        //an attempt to run the javascript library in java, ran into certain undefined problems
        /*ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
// read script file
        engine.eval(Files.newBufferedReader(Paths.get("C:\\Users\\dandree\\IdeaProjects\\JavaMother\\src\\bezierjs-gh-pages\\lib\\bezier.js"), StandardCharsets.UTF_8));


        Invocable inv = (Invocable) engine;
// call function from script file
        inv.invokeFunction("runBez");
        */

        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws SocketException, UnknownHostException, IOException {
        main = this;
        this.stage = stage;

        Parent launchMenuView = FXMLLoader.load(getClass().getResource("LaunchMenuView.fxml"));
        Scene launchScene = new Scene(launchMenuView);

        this.stage.setTitle("IZZY");
        this.stage.sizeToScene();
        this.stage.setScene(launchScene);
        this.stage.setResizable(true);
        this.stage.show();
    }

    public void switchScenes(Scene scene, Object model) {
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
