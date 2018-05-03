import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
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
    public Text yAngularVelocityDisplay = new Text();
    public Text zAngularVelocityDisplay = new Text();
    public Text xAngularAccelerationDisplay = new Text();
    public Text yAngularAccelerationDisplay = new Text();
    public Text zAngularAccelerationDisplay = new Text();

    public View() {
        VBox header = makeHeader();
        VBox statusFields = makeSatusFields();

        BorderPane window = new BorderPane();
        window.setTop(header);
        window.setLeft(statusFields);
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
        Font statusFont = new Font("Arial",12);
        Font statusLabelFont = new Font("Arial",12);

        Label statusAreaLabel = new Label("IZZY Status");
        statusAreaLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        Label linearPositionAreaLabel = new Label("Linear Position");
        linearPositionAreaLabel.setFont(Font.font("Arial", FontWeight.BOLD,14));

        Label angularPositionAreaLabel = new Label("Angular Position");
        angularPositionAreaLabel.setFont(Font.font("Arial", FontWeight.BOLD,14));

        this.xPositionDisplay.setFont(statusFont);
        this.yPositionDisplay.setFont(statusFont);
        this.zPositionDisplay.setFont(statusFont);
        this.xVelocityDisplay.setFont(statusFont);
        this.yVelocityDisplay.setFont(statusFont);
        this.zVelocityDisplay.setFont(statusFont);
        this.xAccelerationDisplay.setFont(statusFont);
        this.yAccelerationDisplay.setFont(statusFont);
        this.zAccelerationDisplay.setFont(statusFont);

        Label xPositionLabel = new Label("X Position");
        xPositionLabel.setFont(statusLabelFont);

        Label yPositionLabel = new Label("Y Position");
        yPositionLabel.setFont(statusLabelFont);

        Label zPositionLabel = new Label("Z Position");
        zPositionLabel.setFont(statusLabelFont);

        Label xVelocityLabel = new Label("Velocity in X");
        xVelocityLabel.setFont(statusLabelFont);

        Label yVelocityLabel = new Label("Velocity in Y");
        yVelocityLabel.setFont(statusLabelFont);

        Label zVelocityLabel = new Label("Velocity in Z");
        zVelocityLabel.setFont(statusLabelFont);

        Label xAccelerationLabel = new Label("Acceleration in X");
        xAccelerationLabel.setFont(statusLabelFont);

        Label yAccelerationLabel = new Label("Acceleration in Y");
        yAccelerationLabel.setFont(statusLabelFont);

        Label zAccelerationLabel = new Label("Acceleration in Z");
        zAccelerationLabel.setFont(statusLabelFont);

        this.xAngularPositionDisplay.setFont(statusFont);
        this.yAngularPositionDisplay.setFont(statusFont);
        this.zAngularPositionDisplay.setFont(statusFont);
        this.xAngularVelocityDisplay.setFont(statusFont);
        this.yAngularVelocityDisplay.setFont(statusFont);
        this.zAngularVelocityDisplay.setFont(statusFont);
        this.xAngularAccelerationDisplay.setFont(statusFont);
        this.yAngularAccelerationDisplay.setFont(statusFont);
        this.zAngularAccelerationDisplay.setFont(statusFont);

        Label xAnglePositionLabel = new Label("Angular Position in X");
        xAnglePositionLabel.setFont(statusLabelFont);

        Label yAnglePositionLabel = new Label("Angular Position in Y");
        yAnglePositionLabel.setFont(statusLabelFont);

        Label zAnglePositionLabel = new Label("Angular Position in Z");
        zAnglePositionLabel.setFont(statusLabelFont);

        Label xAngleVelocityLabel = new Label("Angular Velocity in X");
        xAngleVelocityLabel.setFont(statusLabelFont);

        Label yAngleVelocityLabel = new Label("Angular Velocity in Y");
        yAngleVelocityLabel.setFont(statusLabelFont);

        Label zAngleVelocityLabel = new Label("Angular Velocity in Z");
        zAngleVelocityLabel.setFont(statusLabelFont);

        Label xAngleAccelerationLabel = new Label("Angular Acceleration in X");
        xAngleAccelerationLabel.setFont(statusLabelFont);

        Label yAngleAccelerationLabel = new Label("Angular Acceleration in Y");
        yAngleAccelerationLabel.setFont(statusLabelFont);

        Label zAngleAccelerationLabel = new Label("Angular Acceleration in Z");
        zAngleAccelerationLabel.setFont(statusLabelFont);

        Separator horizontalSeparator = new Separator();
        horizontalSeparator.setPadding(new Insets(10,5,5,10));

        GridPane positionStatusGrid = new GridPane();
        positionStatusGrid.setHgap(10);
        positionStatusGrid.setVgap(5);
        positionStatusGrid.add(linearPositionAreaLabel,0,0,2,1);
        positionStatusGrid.add(xPositionLabel,0,1);
        positionStatusGrid.add(xPositionDisplay,1,1);
        positionStatusGrid.add(yPositionLabel,0,2);
        positionStatusGrid.add(yPositionDisplay,1,2);
        positionStatusGrid.add(zPositionLabel,0,3);
        positionStatusGrid.add(zPositionDisplay,1,3);
        positionStatusGrid.add(xVelocityLabel,0,4);
        positionStatusGrid.add(xVelocityDisplay,1,4);
        positionStatusGrid.add(yVelocityLabel,0,5);
        positionStatusGrid.add(yVelocityDisplay,1,5);
        positionStatusGrid.add(zVelocityLabel,0,6);
        positionStatusGrid.add(zVelocityDisplay,1,6);
        positionStatusGrid.add(xAccelerationLabel,0,7);
        positionStatusGrid.add(xAccelerationDisplay,1,7);
        positionStatusGrid.add(yAccelerationLabel,0,8);
        positionStatusGrid.add(yAccelerationDisplay,1,8);
        positionStatusGrid.add(zAccelerationLabel,0,9);
        positionStatusGrid.add(zAccelerationDisplay,1,9);

        GridPane angularPositionStatusGrid = new GridPane();
        angularPositionStatusGrid.setHgap(10);
        angularPositionStatusGrid.setVgap(5);
        angularPositionStatusGrid.add(angularPositionAreaLabel,0,0,2,1);
        angularPositionStatusGrid.add(xAnglePositionLabel,0,1);
        angularPositionStatusGrid.add(xAngularPositionDisplay,1,1);
        angularPositionStatusGrid.add(yAnglePositionLabel,0,2);
        angularPositionStatusGrid.add(yAngularPositionDisplay,1,2);
        angularPositionStatusGrid.add(zAnglePositionLabel,0,3);
        angularPositionStatusGrid.add(zAngularPositionDisplay,1,3);
        angularPositionStatusGrid.add(xAngleVelocityLabel,0,4);
        angularPositionStatusGrid.add(xAngularVelocityDisplay,1,4);
        angularPositionStatusGrid.add(yAngleVelocityLabel,0,5);
        angularPositionStatusGrid.add(yAngularVelocityDisplay,1,5);
        angularPositionStatusGrid.add(zAngleVelocityLabel,0,6);
        angularPositionStatusGrid.add(zAngularVelocityDisplay,1,6);
        angularPositionStatusGrid.add(xAngleAccelerationLabel,0,7);
        angularPositionStatusGrid.add(xAngularAccelerationDisplay,1,7);
        angularPositionStatusGrid.add(yAngleAccelerationLabel,0,8);
        angularPositionStatusGrid.add(yAngularAccelerationDisplay,1,8);
        angularPositionStatusGrid.add(zAngleAccelerationLabel,0,9);
        angularPositionStatusGrid.add(zAngularAccelerationDisplay,1,9);

        VBox statusArea = new VBox();
        statusArea.setSpacing(10);
        statusArea.setPadding(new Insets(5,10,20,20));
        statusArea.getChildren().addAll(statusAreaLabel,positionStatusGrid,horizontalSeparator,angularPositionStatusGrid);

        return statusArea;
    }

}
