import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class View {
    public Scene scene;
    public Text xPositionDisplay = new Text();
    public Text yPositionDisplay = new Text();
    public Text zPositionDisplay = new Text();
    public Text xVelocityDisplay = new Text();
    public Text yVelocityDisplay = new Text();
    public Text zVelocityDisplay = new Text();
    public Text xAccelerationDisplay = new Text();
    public Text yAccelerationDisplay = new Text();
    public Text zAccelerationDisplay = new Text();
    public Text xAngularPositionDisplay = new Text();
    public Text yAngularPositionDisplay = new Text();
    public Text zAngularPositionDisplay = new Text();
    public Text xAngularVelocityDisplay = new Text();
    public Text yAngularVelocityDsiplay = new Text();
    public Text zAngularVelocityDisplay = new Text();
    public Text xAngularAccelerationDisplay = new Text();
    public Text yAngularAccelerationDisplay = new Text();
    public Text zAngularAccelerationDisplay = new Text();

    public View() {
        VBox header = makeHeader();
        VBox statusFields = makeSatusFields();

        BorderPane window = new BorderPane();
        window.setTop(header);
        this.scene = new Scene(window);
    }

    private VBox makeHeader() {
        Label title = new Label("IZZY Project Mother Interface");
        title.setFont(Font.font("Arial", 24));
        title.setPadding(new Insets(5,10,5,20));

        VBox header = new VBox();
        header.setSpacing(10);
        header.setPadding(new Insets(5,0,20,0));
        header.getChildren().addAll(title);

        return header;
    }

    private VBox makeSatusFields() {
        Label statusAreaLabel = new Label("IZZY Status");
        Font statusFont = new Font ("Arial", 12);
        this.xPositionDisplay.setFont(statusFont);
        this.yPositionDisplay.setFont(statusFont);
        this.zPositionDisplay.setFont(statusFont);
        this.xVelocityDisplay.setFont(statusFont);
        this.yVelocityDisplay.setFont(statusFont);
        this.zVelocityDisplay.setFont(statusFont);
        this.xAccelerationDisplay.setFont(statusFont);
        this.yAccelerationDisplay.setFont(statusFont);
        this.zAccelerationDisplay.setFont(statusFont);
        this.xAngularPositionDisplay.setFont(statusFont);
        this.yAngularPositionDisplay.setFont(statusFont);
        this.zAngularPositionDisplay.setFont(statusFont);
        this.xAngularVelocityDisplay.setFont(statusFont);
        this.yAngularVelocityDsiplay.setFont(statusFont);
        this.zAngularVelocityDisplay.setFont(statusFont);
        this.xAngularAccelerationDisplay.setFont(statusFont);
        this.yAngularAccelerationDisplay.setFont(statusFont);
        this.zAngularAccelerationDisplay.setFont(statusFont);

        VBox statusArea = new VBox();
        statusArea.setSpacing(10);
        statusArea.setPadding(new Insets(5,0,20,0));
        statusArea.getChildren().addAll();

        return statusArea;
    }

}
