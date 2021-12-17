package IZZYCommunication.LineFollowing;

import IZZYCommunication.MotherOSCReceiver;
import LineFollow.LineFollowController;
import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;

import java.net.InetAddress;
import java.net.SocketException;

public class MotherOSCReceiverLineFollow extends MotherOSCReceiver {

    private LineFollowController lineFollowController;
    private final OSCListener listener;

    public MotherOSCReceiverLineFollow(InetAddress inetAddress) throws SocketException {
        super(inetAddress);
        this.listener = (time, message) -> {
            System.out.println("Message Received from IZZY");
            OSCParser(message);
        };
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
            lineFollowController.setDriveSpeed((int) message.getArguments().get(0));
            lineFollowController.setErrorCorrectionSetPoint((double) message.getArguments().get(1));
            lineFollowController.setErrorAngle((double) message.getArguments().get(2));
            lineFollowController.setKP((double) message.getArguments().get(3));
            lineFollowController.setKI((double) message.getArguments().get(4));
            lineFollowController.setKD((double) message.getArguments().get(5));
            lineFollowController.setCurrentState(((boolean) message.getArguments().get(6)) ? "Moving" : "Not Moving");
            lineFollowController.toggleLeftSensor((boolean) message.getArguments().get(7));
            lineFollowController.toggleCenterSensor((boolean) message.getArguments().get(8));
            lineFollowController.toggleRightSensor((boolean) message.getArguments().get(9));
            lineFollowController.setWirePosition((boolean) message.getArguments().get(7),
                    (boolean) message.getArguments().get(8), (boolean) message.getArguments().get(9));
        }
    }

}
