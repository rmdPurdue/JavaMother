import IZZYCommunication.Heartbeat.HeartbeatReceiver;
import IZZYCommunication.Heartbeat.HeartbeatSender;
import lineFollow.LineFollowModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main extends Application {

    public static HeartbeatSender heartbeatSender;
    public static HeartbeatReceiver heartbeatReceiver;
    public static ExecutorService executor;

    /* This function will NOT be hit when the app is packaged, we will start in init or start */
    public static void main(String[] args) {
        System.out.println("Hello from mother!");

        // Launch preloader ??

        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        UUID motherUUID = UUID.fromString("0a1821f3-4273-4f34-8be5-c167dd7669e2");
        heartbeatSender = new HeartbeatSender(motherUUID);
        heartbeatReceiver = new HeartbeatReceiver(motherUUID);
        executor = Executors.newFixedThreadPool(2);

        final Parent launchMenuView = FXMLLoader.load(Objects.requireNonNull(
                getClass().getResource("LaunchMenu/LaunchMenuView.fxml")));
        final Scene launchScene = new Scene(launchMenuView);

        stage.setTitle("IZZY");
        stage.setScene(launchScene);
        stage.sizeToScene();
        stage.setResizable(true);
        stage.show();

        executor.submit(heartbeatSender);
        executor.submit(heartbeatReceiver);
    }

    @Override
    public void stop() throws Exception {
        final LineFollowModel lineFollowModel = LineFollowModel.getCurrentInstance();
        if (lineFollowModel != null) {
            lineFollowModel.closeApplication();
        }
        heartbeatSender.stopBeating();
        heartbeatReceiver.stopBeating();
        super.stop();
        // Creating set object to hold all the threads where
        // Thread.getAllStackTraces().keySet() returns
        // all threads including application threads and
        // system threads
        Set<Thread> threadSet
                = Thread.getAllStackTraces().keySet();

        // Now, for loop is used to iterate through the
        // threadset
        for (Thread t : threadSet) {

            // Printing the thread status using getState()
            // method
            System.out.println("Thread :" + t + ":"
                    + "Thread status : "
                    + t.getState());
        }
    }

}
