package IZZYCommunication.LineFollowing;

import IZZYCommunication.MotherOSCReceiver;
import LineFollow.LineFollowController;
import LineFollow.LineFollowSensorLogger;
import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;

import java.net.InetAddress;
import java.net.SocketException;

public class MotherOSCReceiverLineFollow extends MotherOSCReceiver {

    private LineFollowController lineFollowController;
    private final OSCListener listener;
    private final LineFollowSensorLogger sensorLogger;
    private static final int DRIVE_SPEED_INDEX = 0;
    private static final int ERROR_CORRECTION_SET_POINT_INDEX = 1;
    private static final int ERROR_ANGLE_INDEX = 2;
    private static final int KP_INDEX = 3;
    private static final int KI_INDEX = 4;
    private static final int KD_INDEX = 5;
    private static final int CURRENT_STATE_INDEX = 6;
    private static final int LEFT_SENSOR_TOGGLED_INDEX = 7;
    private static final int CENTER_SENSOR_TOGGLED_INDEX = 8;
    private static final int RIGHT_SENSOR_TOGGLED_INDEX = 9;
    private static final int LEFT_SENSOR_ANALOG_INDEX = 10;
    private static final int CENTER_SENSOR_ANALOG_INDEX = 11;
    private static final int RIGHT_SENSOR_ANALOG_INDEX = 12;
    private static final int LEFT_SENSOR_THRESHOLD_INDEX = 13;
    private static final int CENTER_SENSOR_THRESHOLD_INDEX = 14;
    private static final int RIGHT_SENSOR_THRESHOLD_INDEX = 15;

    public MotherOSCReceiverLineFollow(InetAddress inetAddress, LineFollowSensorLogger sensorLogger) throws SocketException {
        super(inetAddress);
        this.listener = (time, message) -> {
            OSCParser(message);
        };
        this.sensorLogger = sensorLogger;
    }

    public MotherOSCReceiverLineFollow(InetAddress inetAddress) throws SocketException {
        super(inetAddress);
        this.listener = (time, message) -> {
            OSCParser(message);
        };
        this.sensorLogger = null;
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
            lineFollowController.setKP((double) message.getArguments().get(KP_INDEX));
            lineFollowController.setKI((double) message.getArguments().get(KI_INDEX));
            lineFollowController.setKD((double) message.getArguments().get(KD_INDEX));
            lineFollowController.setCurrentState(((boolean) message.getArguments().get(CURRENT_STATE_INDEX)) ? "Moving" : "Not Moving");
            lineFollowController.toggleLeftSensor((boolean) message.getArguments().get(LEFT_SENSOR_TOGGLED_INDEX),
                    (int) message.getArguments().get(LEFT_SENSOR_ANALOG_INDEX));
            lineFollowController.toggleCenterSensor((boolean) message.getArguments().get(CENTER_SENSOR_TOGGLED_INDEX),
                    (int) message.getArguments().get(CENTER_SENSOR_ANALOG_INDEX));
            lineFollowController.toggleRightSensor((boolean) message.getArguments().get(RIGHT_SENSOR_TOGGLED_INDEX),
                    (int) message.getArguments().get(RIGHT_SENSOR_ANALOG_INDEX));
            lineFollowController.setWirePosition((boolean) message.getArguments().get(LEFT_SENSOR_TOGGLED_INDEX),
                    (boolean) message.getArguments().get(CENTER_SENSOR_TOGGLED_INDEX),
                    (boolean) message.getArguments().get(RIGHT_SENSOR_TOGGLED_INDEX));
            lineFollowController.setLeftThresholdValue((int) message.getArguments().get(LEFT_SENSOR_THRESHOLD_INDEX));
            lineFollowController.setCenterThresholdValue((int) message.getArguments().get(CENTER_SENSOR_THRESHOLD_INDEX));
            lineFollowController.setRightThresholdValue((int) message.getArguments().get(RIGHT_SENSOR_THRESHOLD_INDEX));
            logAnalogValues((int) message.getArguments().get(LEFT_SENSOR_ANALOG_INDEX),
                    (int) message.getArguments().get(CENTER_SENSOR_ANALOG_INDEX),
                    (int) message.getArguments().get(RIGHT_SENSOR_ANALOG_INDEX));
        }
    }


    private void logAnalogValues(int left, int center, int right) {
        if (sensorLogger != null) {
            sensorLogger.logAnalogValues(left, center, right);
        }
    }
}
