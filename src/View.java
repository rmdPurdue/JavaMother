import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.text.Font;

import java.awt.*;

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


    TextField xPositionEntry = new TextField();
    TextField yPositionEntry = new TextField();
    TextField headingEntry = new TextField();
    TextField controlX1Entry = new TextField();
    TextField controlY1Entry = new TextField();
    TextField controlX2Entry = new TextField();
    TextField controlY2Entry = new TextField();

    Button chooseControl1Point = new Button("Choose Point");
    Button chooseControl2Point = new Button("Choose Point");
    Button setControl1Point = new Button("Set Point");
    Button setControl2Point = new Button("Set Point");

    TextField totalTimeEntry = new TextField();
    TextField accelerationTimeEntry = new TextField();
    TextField decelerationTimeEntry = new TextField();
    Button addTargetBtn = new Button("Add Target");
    Button goToTargetBtn = new Button("Go to Target");

    public int centerlineX;
    public int plasterlineY;

    private Canvas backgroundCanvas = new Canvas(600,400);
    private GraphicsContext backgroundGC = backgroundCanvas.getGraphicsContext2D();

    Pane pane = new Pane();

    public View() {

        VBox header = makeHeader();
        VBox statusFields = makeStatusFields();
        VBox targetArea = makeTargetArea();

        BorderPane window = new BorderPane();
        window.setTop(header);
        window.setLeft(statusFields);
        window.setCenter(targetArea);
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

    private VBox makeStatusFields() {
        Font statusFont = new Font("Arial",12);
        Font statusLabelFont = new Font("Arial",12);

        Label statusAreaLabel = new Label("IZZY Status");
        statusAreaLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        Label linearPositionAreaLabel = new Label("Linear Position");
        linearPositionAreaLabel.setFont(Font.font("Arial", FontWeight.BOLD,14));

        Label angularPositionAreaLabel = new Label("Angular Position");
        angularPositionAreaLabel.setFont(Font.font("Arial", FontWeight.BOLD,14));

        xPositionDisplay.setFont(statusFont);
        yPositionDisplay.setFont(statusFont);
        zPositionDisplay.setFont(statusFont);
        xVelocityDisplay.setFont(statusFont);
        yVelocityDisplay.setFont(statusFont);
        zVelocityDisplay.setFont(statusFont);
        xAccelerationDisplay.setFont(statusFont);
        yAccelerationDisplay.setFont(statusFont);
        zAccelerationDisplay.setFont(statusFont);

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

        xAngularPositionDisplay.setFont(statusFont);
        yAngularPositionDisplay.setFont(statusFont);
        zAngularPositionDisplay.setFont(statusFont);
        xAngularVelocityDisplay.setFont(statusFont);
        yAngularVelocityDisplay.setFont(statusFont);
        zAngularVelocityDisplay.setFont(statusFont);
        xAngularAccelerationDisplay.setFont(statusFont);
        yAngularAccelerationDisplay.setFont(statusFont);
        zAngularAccelerationDisplay.setFont(statusFont);

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
        positionStatusGrid.setAlignment(Pos.CENTER_RIGHT);
        positionStatusGrid.setHgap(10);
        positionStatusGrid.setVgap(5);
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHalignment(HPos.RIGHT);
        positionStatusGrid.getColumnConstraints().addAll(columnConstraints, columnConstraints);

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
        angularPositionStatusGrid.setAlignment(Pos.CENTER_RIGHT);
        angularPositionStatusGrid.setHgap(10);
        angularPositionStatusGrid.setVgap(5);
        angularPositionStatusGrid.getColumnConstraints().addAll(columnConstraints, columnConstraints);

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

    private VBox makeTargetArea() {
        drawBackground();

        pane.getChildren().add(backgroundCanvas);

        Label newMoveLabel = new Label("New Move");
        Label newXPositionLabel = new Label("X Position:");
        Label newYPositionLabel = new Label("Y Position:");
        Label newTotalTime = new Label("Total move time: ");
        Label newAccelerationTime = new Label("Total acceleration time: ");
        Label newDecelerationTime = new Label("Total deceleration time: ");
        Label newHeadingLabel = new Label("Heading:");

        GridPane targetPositionFields = new GridPane();
        targetPositionFields.add(newMoveLabel,0,0,2,1);
        targetPositionFields.add(newXPositionLabel,0,1);
        targetPositionFields.add(xPositionEntry,1,1);
        targetPositionFields.add(newYPositionLabel,0,2);
        targetPositionFields.add(yPositionEntry,1,2);
        targetPositionFields.add(newHeadingLabel, 0, 3);
        targetPositionFields.add(headingEntry,1,3);
        targetPositionFields.add(addTargetBtn,1,4);

        GridPane targetBezierPointFields = new GridPane();
        targetBezierPointFields.add(controlX1Entry,0,0);
        targetBezierPointFields.add(controlY1Entry,1,0);
        targetBezierPointFields.add(chooseControl1Point,2,0);
        targetBezierPointFields.add(controlX2Entry,0,1);
        targetBezierPointFields.add(controlY2Entry,1,1);
        targetBezierPointFields.add(chooseControl2Point,2,1);

        GridPane timeFields = new GridPane();
        timeFields.add(newTotalTime, 0, 0);
        timeFields.add(totalTimeEntry, 1, 0);
        timeFields.add(newAccelerationTime,0,1);
        timeFields.add(accelerationTimeEntry, 1,1);
        timeFields.add(newDecelerationTime,0,2);
        timeFields.add(decelerationTimeEntry,1,2);
        timeFields.add(goToTargetBtn,1,3);

        HBox targetPositionEntry = new HBox();
        targetPositionEntry.setPadding(new Insets(10,10,10,10));
        targetPositionEntry.setSpacing(20);
        targetPositionEntry.getChildren().addAll(targetPositionFields, targetBezierPointFields, timeFields);

        VBox targetArea = new VBox();
        targetArea.setPadding(new Insets(20,20,20,20));
        targetArea.setSpacing(10);
        targetArea.getChildren().addAll(pane, targetPositionEntry);

        return targetArea;
    }

    public void drawBackground() {
        backgroundGC.clearRect(0,0, backgroundCanvas.getWidth(), backgroundCanvas.getHeight());
        backgroundGC.setStroke(Color.BLACK);
        backgroundGC.setLineWidth(2);
        backgroundGC.setLineDashes(0);
        backgroundGC.strokeRect(0,0,600,400);

        backgroundGC.setStroke(Color.BLUE);
        backgroundGC.setLineWidth(1);
        backgroundGC.setLineDashes(5,5);
        backgroundGC.strokeLine(centerlineX,0,centerlineX,400);
        backgroundGC.strokeLine(0,plasterlineY,600,plasterlineY);
    }


    private GridPane makeStageSetupFields() {


        return null;
    }


}
