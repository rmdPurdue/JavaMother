package ManualControl;

import ManualControl.ManualModel;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import javafx.event.ActionEvent;
import java.io.IOException;

public class MotherManualController {
    ManualModel manualModel;
    private final int INCHES_TO_MICRONS = 25400;
    private final int DEGREES_TO_MILLIDEGREES = 1000;

    public MotherManualController() {
        this.manualModel = ManualModel.getCurrentInstance();
    }

    @FXML
    public Text xPositionDisplay;

    @FXML
    public Text yPositionDisplay;

    @FXML
    public Text zPositionDisplay;

    @FXML
    public Text xVelocityDisplay;

    @FXML
    public Text yVelocityDisplay;

    @FXML
    public Text zVelocityDisplay;

    @FXML
    public Text xAccelerationDisplay;

    @FXML
    public Text yAccelerationDisplay;

    @FXML
    public Text zAccelerationDisplay;

    @FXML
    public Text xAngularPositionDisplay;

    @FXML
    public Text yAngularPositionDisplay;

    @FXML
    public Text zAngularPositionDisplay;

    @FXML
    public Text xAngularVelocityDisplay;

    @FXML
    public Text yAngularVelocityDisplay;

    @FXML
    public Text zAngularVelocityDisplay;

    @FXML
    public Text xAngularAccelerationDisplay;

    @FXML
    public Text yAngularAccelerationDisplay;

    @FXML
    public Text zAngularAccelerationDisplay;

    @FXML
    TextField xPositionEntry;

    @FXML
    TextField yPositionEntry;

    @FXML
    TextField headingEntry;

    @FXML
    TextField controlX1Entry;

    @FXML
    TextField controlY1Entry;

    @FXML
    TextField controlX2Entry;

    @FXML
    TextField controlY2Entry;

    @FXML
    Button chooseControl1Point;

    @FXML
    Button chooseControl2Point;

    @FXML
    Button setControl1Point;

    @FXML
    Button setControl2Point;

    @FXML
    TextField totalTimeEntry;

    @FXML
    TextField accelerationTimeEntry;

    @FXML
    TextField decelerationTimeEntry;

    @FXML
    Button addTargetBtn;

    @FXML
    Button goToTargetBtn;;

    @FXML
    Canvas backgroundCanvas;

    @FXML
    Pane pane;

    public int centerlineX;
    public int plasterlineY;

    private GraphicsContext backgroundGC;

    @FXML
    private void initialize() {
        backgroundGC = backgroundCanvas.getGraphicsContext2D();
        drawBackground();
        hookupEvents();
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

    public void chooseControl1PointAction(ActionEvent e) {
        manualModel.targetTrajectory.setTrajectoryControlPoints(1,2,3,4);
            /*
            pane.addEventHandler(MouseEvent.MOUSE_MOVED, event -> {
                controlX1Entry.setText(Double.toString(event.getX()));
                controlX2Entry.setText(Double.toString(event.getY()));
            });

            pane.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                System.out.println("Stop listening.");
            });
            */
    }

    public void addLinearTargetBtnAction (ActionEvent e) {
        /*
            //TODO: validate that current heading matches trajectory heading.

            manualModel.setTargetPosition(
                    Integer.parseInt(xPositionEntry.getText()) * INCHES_TO_MICRONS,
                    Integer.parseInt(yPositionEntry.getText()) * INCHES_TO_MICRONS,
                    0,
                    manualModel.currentPosition.getX().getAngularPosition());
            manualModel.targetPosition.updatePosition(manualModel.stageArea.getPixelRatio());
            manualModel.targetTrajectory.updateTrajectory(manualModel.currentPosition, manualModel.targetPosition, manualModel.stageArea.getPixelRatio(),
                    Double.parseDouble(totalTimeEntry.getText()),
                    Double.parseDouble(accelerationTimeEntry.getText()),
                    Double.parseDouble(decelerationTimeEntry.getText()));
         */
    }

    public void addTargetBtnAction(ActionEvent e) {
        manualModel.setTargetPosition(
                Integer.parseInt(xPositionEntry.getText()) * INCHES_TO_MICRONS,
                Integer.parseInt(yPositionEntry.getText()) * INCHES_TO_MICRONS,
                0,
                (int) Double.parseDouble(headingEntry.getText()) * DEGREES_TO_MILLIDEGREES);
        manualModel.targetPosition.updatePosition(manualModel.stageArea.getPixelRatio());
        manualModel.targetTrajectory.updateTrajectory(manualModel.currentPosition, manualModel.targetPosition, manualModel.stageArea.getPixelRatio(),
                Double.parseDouble(totalTimeEntry.getText()),
                Double.parseDouble(accelerationTimeEntry.getText()),
                Double.parseDouble(decelerationTimeEntry.getText()));
    }

    public void goToTargetBtnAction(ActionEvent e) {
        try {
            manualModel.goToTarget();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    protected void hookupEvents() {
        manualModel.setStageArea();

        plasterlineY = (int)(manualModel.stageArea.getUpstageDepthInMicrons() / manualModel.stageArea.getPixelRatio());
        centerlineX = (int)((manualModel.stageArea.getWidthInMicrons() / 2)/ manualModel.stageArea.getPixelRatio());

        manualModel.setCenterPosition(centerlineX, plasterlineY, 0);
        manualModel.currentPosition.setTranslation(centerlineX, plasterlineY);
        manualModel.targetPosition.setTranslation(centerlineX, plasterlineY);

        drawBackground();

        pane.getChildren().addAll(
                manualModel.currentPosition.getMarker(),
                manualModel.targetPosition.getMarker(),
                manualModel.targetTrajectory.getTrajectory(manualModel.stageArea.getPixelRatio()));


        xPositionDisplay.textProperty().bind(manualModel.currentPosition.getX().positionProperty().asString());
        manualModel.currentPosition.updatedProperty().addListener((o, oldValue, newValue) -> {
            manualModel.currentPosition.updatePosition(manualModel.stageArea.getPixelRatio());
            manualModel.currentPosition.setUpdated(false);
        });


/*            if(xPositionEntry.getText().isEmpty() &&
                    yPositionEntry.getText().isEmpty()) {
                if(!headingEntry.getText().isEmpty()) {
                    if(manualModel.currentPosition.getX().getPosition() != Integer.parseInt(headingEntry.getText())) {
                        manualModel.setTargetPosition(manualModel.currentPosition.getX().getPosition(),
                                manualModel.currentPosition.getY().getPosition(),
                                0,
                                Integer.parseInt(headingEntry.getText()) * DEGREES_TO_MILLIDEGREES
                        );
                    }
                }
            } else {
                manualModel.setTargetPosition(
                        Integer.parseInt(xPositionEntry.getText()) * INCHES_TO_MICRONS,
                        Integer.parseInt(yPositionEntry.getText()) * INCHES_TO_MICRONS,
                        0,
                        (int)Double.parseDouble(headingEntry.getText()) * DEGREES_TO_MILLIDEGREES);
            }
            manualModel.targetPosition.updatePosition(manualModel.stageArea.getPixelRatio());
            manualModel.targetTrajectory.updateTrajectory(manualModel.currentPosition, manualModel.targetPosition, manualModel.stageArea.getPixelRatio());

        });
*/
        yPositionDisplay.textProperty().bind(manualModel.currentPosition.getY().positionProperty().asString());
        zPositionDisplay.textProperty().bind(manualModel.currentPosition.getZ().positionProperty().asString());

        xVelocityDisplay.textProperty().bind(manualModel.currentPosition.getX().velocityProperty().asString());
        yVelocityDisplay.textProperty().bind(manualModel.currentPosition.getY().velocityProperty().asString());
        zVelocityDisplay.textProperty().bind(manualModel.currentPosition.getZ().velocityProperty().asString());

        xAccelerationDisplay.textProperty().bind(manualModel.currentPosition.getX().accelerationProperty().asString());
        yAccelerationDisplay.textProperty().bind(manualModel.currentPosition.getY().accelerationProperty().asString());
        zAccelerationDisplay.textProperty().bind(manualModel.currentPosition.getZ().accelerationProperty().asString());

        xAngularPositionDisplay.textProperty().bind(manualModel.currentPosition.getX().angularPositionProperty().asString());
        yAngularPositionDisplay.textProperty().bind(manualModel.currentPosition.getY().angularPositionProperty().asString());
        zAngularPositionDisplay.textProperty().bind(manualModel.currentPosition.getZ().angularPositionProperty().asString());

        xAngularVelocityDisplay.textProperty().bind(manualModel.currentPosition.getX().angularVelocityProperty().asString());
        yAngularVelocityDisplay.textProperty().bind(manualModel.currentPosition.getY().angularVelocityProperty().asString());
        zAngularVelocityDisplay.textProperty().bind(manualModel.currentPosition.getZ().angularVelocityProperty().asString());

        xAngularAccelerationDisplay.textProperty().bind(manualModel.currentPosition.getX().angularAccelerationProperty().asString());
        yAngularAccelerationDisplay.textProperty().bind(manualModel.currentPosition.getY().angularAccelerationProperty().asString());
        zAngularAccelerationDisplay.textProperty().bind(manualModel.currentPosition.getZ().angularAccelerationProperty().asString());
    }

}
