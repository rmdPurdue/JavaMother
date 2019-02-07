import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

public class main extends Application {
    private int INCHES_TO_MICRONS = 25400;
    private int DEGREES_TO_MILLIDEGREES = 1000;
    private Stage stage;
    private View view;
    private Model model;

    public static void main(String[] args) {
        System.out.println("Hello from mother!");
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws SocketException, UnknownHostException, IOException {
        view = new View();
        model = new Model();
        this.stage = stage;

        hookupEvents();

        this.stage.setTitle("Izzy");
        this.stage.sizeToScene();
        this.stage.setScene(view.scene);
        this.stage.setResizable(false);
        this.stage.show();
        model.startListening();
    }

    private void hookupEvents() {
        model.setStageArea();

        view.plasterlineY = (int)(model.stageArea.getUpstageDepthInMicrons() / model.stageArea.getPixelRatio());
        view.centerlineX = (int)((model.stageArea.getWidthInMicrons() / 2)/ model.stageArea.getPixelRatio());

        model.setCenterPosition(view.centerlineX, view.plasterlineY, 0);
        model.currentPosition.setTranslation(view.centerlineX, view.plasterlineY);
        model.targetPosition.setTranslation(view.centerlineX, view.plasterlineY);

        view.drawBackground();

        view.pane.getChildren().addAll(
                model.currentPosition.getMarker(),
                model.targetPosition.getMarker(),
                model.targetTrajectory.getTrajectory(model.stageArea.getPixelRatio()));


        view.xPositionDisplay.textProperty().bind(model.currentPosition.getX().positionProperty().asString());
        model.currentPosition.updatedProperty().addListener((o, oldValue, newValue) -> {
            model.currentPosition.updatePosition(model.stageArea.getPixelRatio());
            model.currentPosition.setUpdated(false);
        });

        view.chooseControl1Point.setOnAction(e -> {
            model.targetTrajectory.setTrajectoryControlPoints(1,2,3,4);
            /*
            view.pane.addEventHandler(MouseEvent.MOUSE_MOVED, event -> {
                view.controlX1Entry.setText(Double.toString(event.getX()));
                view.controlX2Entry.setText(Double.toString(event.getY()));
            });

            view.pane.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                System.out.println("Stop listening.");
            });
            */

        });


/*
        view.addLinearTargetBtn.setOnAction(e -> {
            //TODO: validate that current heading matches trajectory heading.

            model.setTargetPosition(
                    Integer.parseInt(view.xPositionEntry.getText()) * INCHES_TO_MICRONS,
                    Integer.parseInt(view.yPositionEntry.getText()) * INCHES_TO_MICRONS,
                    0,
                    model.currentPosition.getX().getAngularPosition());
            model.targetPosition.updatePosition(model.stageArea.getPixelRatio());
            model.targetTrajectory.updateTrajectory(model.currentPosition, model.targetPosition, model.stageArea.getPixelRatio(),
                    Double.parseDouble(view.totalTimeEntry.getText()),
                    Double.parseDouble(view.accelerationTimeEntry.getText()),
                    Double.parseDouble(view.decelerationTimeEntry.getText()));
        });
*/

        view.addTargetBtn.setOnAction(e -> {
            model.setTargetPosition(
                    Integer.parseInt(view.xPositionEntry.getText()) * INCHES_TO_MICRONS,
                    Integer.parseInt(view.yPositionEntry.getText()) * INCHES_TO_MICRONS,
                    0,
                    (int) Double.parseDouble(view.headingEntry.getText()) * DEGREES_TO_MILLIDEGREES);
            model.targetPosition.updatePosition(model.stageArea.getPixelRatio());
            model.targetTrajectory.updateTrajectory(model.currentPosition, model.targetPosition, model.stageArea.getPixelRatio(),
                    Double.parseDouble(view.totalTimeEntry.getText()),
                    Double.parseDouble(view.accelerationTimeEntry.getText()),
                    Double.parseDouble(view.decelerationTimeEntry.getText()));
        });

        view.goToTargetBtn.setOnAction(e -> {
            try {
                model.goToTarget();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
/*            if(view.xPositionEntry.getText().isEmpty() &&
                    view.yPositionEntry.getText().isEmpty()) {
                if(!view.headingEntry.getText().isEmpty()) {
                    if(model.currentPosition.getX().getPosition() != Integer.parseInt(view.headingEntry.getText())) {
                        model.setTargetPosition(model.currentPosition.getX().getPosition(),
                                model.currentPosition.getY().getPosition(),
                                0,
                                Integer.parseInt(view.headingEntry.getText()) * DEGREES_TO_MILLIDEGREES
                        );
                    }
                }
            } else {
                model.setTargetPosition(
                        Integer.parseInt(view.xPositionEntry.getText()) * INCHES_TO_MICRONS,
                        Integer.parseInt(view.yPositionEntry.getText()) * INCHES_TO_MICRONS,
                        0,
                        (int)Double.parseDouble(view.headingEntry.getText()) * DEGREES_TO_MILLIDEGREES);
            }
            model.targetPosition.updatePosition(model.stageArea.getPixelRatio());
            model.targetTrajectory.updateTrajectory(model.currentPosition, model.targetPosition, model.stageArea.getPixelRatio());

        });
*/

        view.yPositionDisplay.textProperty().bind(model.currentPosition.getY().positionProperty().asString());
        view.zPositionDisplay.textProperty().bind(model.currentPosition.getZ().positionProperty().asString());

        view.xVelocityDisplay.textProperty().bind(model.currentPosition.getX().velocityProperty().asString());
        view.yVelocityDisplay.textProperty().bind(model.currentPosition.getY().velocityProperty().asString());
        view.zVelocityDisplay.textProperty().bind(model.currentPosition.getZ().velocityProperty().asString());

        view.xAccelerationDisplay.textProperty().bind(model.currentPosition.getX().accelerationProperty().asString());
        view.yAccelerationDisplay.textProperty().bind(model.currentPosition.getY().accelerationProperty().asString());
        view.zAccelerationDisplay.textProperty().bind(model.currentPosition.getZ().accelerationProperty().asString());

        view.xAngularPositionDisplay.textProperty().bind(model.currentPosition.getX().angularPositionProperty().asString());
        view.yAngularPositionDisplay.textProperty().bind(model.currentPosition.getY().angularPositionProperty().asString());
        view.zAngularPositionDisplay.textProperty().bind(model.currentPosition.getZ().angularPositionProperty().asString());

        view.xAngularVelocityDisplay.textProperty().bind(model.currentPosition.getX().angularVelocityProperty().asString());
        view.yAngularVelocityDisplay.textProperty().bind(model.currentPosition.getY().angularVelocityProperty().asString());
        view.zAngularVelocityDisplay.textProperty().bind(model.currentPosition.getZ().angularVelocityProperty().asString());

        view.xAngularAccelerationDisplay.textProperty().bind(model.currentPosition.getX().angularAccelerationProperty().asString());
        view.yAngularAccelerationDisplay.textProperty().bind(model.currentPosition.getY().angularAccelerationProperty().asString());
        view.zAngularAccelerationDisplay.textProperty().bind(model.currentPosition.getZ().angularAccelerationProperty().asString());

    }

}