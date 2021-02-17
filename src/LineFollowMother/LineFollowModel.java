package LineFollowMother;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;
import com.illposed.osc.OSCPortOut;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class LineFollowModel {
    public OSCPortIn receiver = null;
    public OSCListener listener = null;
    private static LineFollowModel currentInstance;

    final InetAddress outgoingAddress;
    final int outgoingPort;

    public LineFollowModel(String ipAddress) throws UnknownHostException {
        outgoingAddress = InetAddress.getByName(ipAddress);
        outgoingPort = 9000;
        currentInstance = this;
    }

    public static LineFollowModel getCurrentInstance() {
        return currentInstance;
    }

    public void startListening() throws IOException {
        receiver = new OSCPortIn(8000);
        listener = (time, message) -> {
            System.out.println("Message Received from IZZY");
            OSCParser(message);
        };
        receiver.startListening();
        receiver.addListener("/IZZYMother/Status", listener);
        System.out.println("Mother is now listening for IZZY via OSC\n");
    }

    public void stopListening() {
        System.out.println("Mother is no longer listening for IZZY");
        receiver.stopListening();
        receiver.close();
    }

    public void sendMessage(OSCMessage message) throws IOException {
        OSCPortOut sender = new OSCPortOut(outgoingAddress, outgoingPort);
        sender.send(message);
        sender.close();
    }

    private void OSCParser(OSCMessage message){
        MotherLineFollowController controller = MotherLineFollowController.getCurrentInstance();
        controller.setDriveSpeed((int) message.getArguments().get(0));
        controller.setErrorCorrectionSetPoint((double) message.getArguments().get(1));
        controller.setErrorAngle((double) message.getArguments().get(2));
        controller.setKP((double) message.getArguments().get(3));
        controller.setKI((double) message.getArguments().get(4));
        controller.setKD((double) message.getArguments().get(5));
        controller.setCurrentState(((boolean) message.getArguments().get(6)) ? "Moving" : "Not Moving");
        controller.setExceptions((String) message.getArguments().get(7));
        controller.toggleLeftSensor((boolean) message.getArguments().get(8));
        controller.toggleCenterSensor((boolean) message.getArguments().get(9));
        controller.toggleRightSensor((boolean) message.getArguments().get(10));
        controller.setWirePosition((boolean) message.getArguments().get(8), (boolean) message.getArguments().get(9),
                (boolean) message.getArguments().get(10));

    }
}
