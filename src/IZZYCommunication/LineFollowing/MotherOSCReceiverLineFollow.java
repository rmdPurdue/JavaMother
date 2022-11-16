package IZZYCommunication.LineFollowing;

import IZZYCommunication.MotherOSCReceiver;
import lineFollow.LineFollowController;
import lineFollow.LineFollowSensorLogger;
import lineFollow.mapping.MapLoop;
import lineFollow.Plotting.PlotLoop;
import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;

import java.net.InetAddress;
import java.net.SocketException;

public class MotherOSCReceiverLineFollow extends MotherOSCReceiver {

    private LineFollowController lineFollowController;
    private final OSCListener listener;
    private final LineFollowSensorLogger sensorLogger;
    private final PlotLoop plotLoop;
    private final MapLoop mapLoop;
    private static final int DRIVE_SPEED_INDEX = 0;
    private static final int ERROR_CORRECTION_SET_POINT_INDEX = 1;
    private static final int ERROR_ANGLE_INDEX = 2;
    private static final int KP_INDEX = 3;
    private static final int KI_INDEX = 4;
    private static final int KD_INDEX = 5;
    private static final int CURRENT_STATE_INDEX = 6;
    private static final int LEFT_SENSOR_ANALOG_INDEX = 7;
    private static final int CENTER_SENSOR_ANALOG_INDEX = 8;
    private static final int RIGHT_SENSOR_ANALOG_INDEX = 9;
    private static final int LEFT_SENSOR_THRESHOLD_INDEX = 10;
    private static final int CENTER_SENSOR_THRESHOLD_INDEX = 11;
    private static final int RIGHT_SENSOR_THRESHOLD_INDEX = 12;
    private static final int DRIVE_P = 13;
    private static final int DRIVE_S = 14;
    private static final int TURN_P = 15;
    private static final int TURN_S = 16;


    public MotherOSCReceiverLineFollow(InetAddress inetAddress, LineFollowSensorLogger sensorLogger, PlotLoop plotLoop, MapLoop mapLoop) throws SocketException {
        super(inetAddress);
        this.listener = (time, message) -> {
            OSCParser(message);
        };
        this.sensorLogger = sensorLogger;
        this.plotLoop = plotLoop;
        this.mapLoop = mapLoop;
    }

    public MotherOSCReceiverLineFollow(InetAddress inetAddress) throws SocketException {
        super(inetAddress);
        this.listener = (time, message) -> {
            OSCParser(message);
        };
        this.sensorLogger = null;
        this.plotLoop = null;
        this.mapLoop = null;
    }

    public void eStopOSC(OSCMessage msg) {

    }

    public void startListening() {
        receiver.startListening();
        receiver.addListener("/IZZYMother/Status", listener);
        System.out.println("Mother is now listening for IZZY via OSC\n");
    }

    public void setLineFollowController(LineFollowController controller) {
        this.lineFollowController = controller;
    }

    private void OSCParser(OSCMessage message) {
        if (lineFollowController != null) {
            lineFollowController.setDriveSpeed((int) message.getArguments().get(DRIVE_SPEED_INDEX));
            lineFollowController.setErrorCorrectionSetPoint((double) message.getArguments().get(ERROR_CORRECTION_SET_POINT_INDEX));
            lineFollowController.setErrorAngle((double) message.getArguments().get(ERROR_ANGLE_INDEX));
            lineFollowController.setWheelDSpeed((int) message.getArguments().get(DRIVE_S));
            lineFollowController.setWheelDPosition((int) message.getArguments().get(DRIVE_P));
            lineFollowController.setWheelTSpeed((int) message.getArguments().get(TURN_S));
            lineFollowController.setWheelTPosition((int) message.getArguments().get(TURN_P));
            lineFollowController.setKP((double) message.getArguments().get(KP_INDEX));
            lineFollowController.setKI((double) message.getArguments().get(KI_INDEX));
            lineFollowController.setKD((double) message.getArguments().get(KD_INDEX));
            lineFollowController.setCurrentState(((boolean) message.getArguments().get(CURRENT_STATE_INDEX)) ? "Moving" : "Not Moving");
            lineFollowController.setLeftThresholdValue((int) message.getArguments().get(LEFT_SENSOR_THRESHOLD_INDEX));
            lineFollowController.setCenterThresholdValue((int) message.getArguments().get(CENTER_SENSOR_THRESHOLD_INDEX));
            lineFollowController.setRightThresholdValue((int) message.getArguments().get(RIGHT_SENSOR_THRESHOLD_INDEX));
            boolean isLeft, isCenter, isRight;
            isLeft = (int) message.getArguments().get(LEFT_SENSOR_ANALOG_INDEX) <= (int) message.getArguments().get(LEFT_SENSOR_THRESHOLD_INDEX);
            isCenter = (int) message.getArguments().get(CENTER_SENSOR_ANALOG_INDEX) <= (int) message.getArguments().get(CENTER_SENSOR_THRESHOLD_INDEX);
            isRight = (int) message.getArguments().get(RIGHT_SENSOR_ANALOG_INDEX) <= (int) message.getArguments().get(RIGHT_SENSOR_THRESHOLD_INDEX);
            lineFollowController.toggleLeftSensor(isLeft, (int) message.getArguments().get(LEFT_SENSOR_ANALOG_INDEX));
            lineFollowController.toggleCenterSensor(isCenter, (int) message.getArguments().get(CENTER_SENSOR_ANALOG_INDEX));
            lineFollowController.toggleRightSensor(isRight, (int) message.getArguments().get(RIGHT_SENSOR_ANALOG_INDEX));
            lineFollowController.setWirePosition(isLeft, isCenter, isRight);
            logAnalogValues((int) message.getArguments().get(LEFT_SENSOR_ANALOG_INDEX),
                    (int) message.getArguments().get(CENTER_SENSOR_ANALOG_INDEX),
                    (int) message.getArguments().get(RIGHT_SENSOR_ANALOG_INDEX));
            plotAnalogValues((int) message.getArguments().get(LEFT_SENSOR_ANALOG_INDEX),
                    (int) message.getArguments().get(CENTER_SENSOR_ANALOG_INDEX),
                    (int) message.getArguments().get(RIGHT_SENSOR_ANALOG_INDEX));
            mapIZZYLocation((int) message.getArguments().get(DRIVE_P), (int) message.getArguments().get(DRIVE_S),
                    (int) message.getArguments().get(TURN_P), (int) message.getArguments().get(TURN_S));
        }
    }


    private void logAnalogValues(int left, int center, int right) {
        if (sensorLogger != null) {
            sensorLogger.logAnalogValues(left, center, right);
        }
    }

    private void plotAnalogValues(int left, int center, int right) {
        if (plotLoop != null) {
            plotLoop.plotData(left, center, right);
        }
    }

    private void mapIZZYLocation(int driveP, int driveS, int turnP, int turnS) {
        if (mapLoop != null) {
            mapLoop.mapData(driveP, driveS, turnP, turnS);
        }
    }
}
