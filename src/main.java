import javafx.application.Application;
import javafx.stage.Stage;

public class main extends Application {
    private Stage stage;
    private View view;
    private Model model;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
        view = new View();
        this.stage = stage;

        this.stage.setTitle("Izzy");
        this.stage.sizeToScene();
        this.stage.setScene(view.scene);
        this.stage.setResizable(false);
        this.stage.show();
    }
}