package LineFollowMother;

import IZZYCommunication.HeartbeatResponseListener;
import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;
import com.illposed.osc.OSCPortOut;
import IZZYCommunication.HeartbeatSender;
import IZZYCommunication.HeartbeatReceiver;
import Devices.IZZY;
import Devices.Mother;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LineFollowModel {
    public OSCPortIn receiver = null;
    public OSCListener listener = null;
    private static LineFollowModel currentInstance;

    private Mother mother = new Mother();
    private HeartbeatSender heartbeatSender = new HeartbeatSender(mother);
    private HeartbeatReceiver heartbeatReceiver = new HeartbeatReceiver();
    private ExecutorService executor = Executors.newFixedThreadPool(2);

    final InetAddress outgoingAddress;
    final int outgoingPort;

    public LineFollowModel(String ipAddress) throws IOException {
        outgoingAddress = InetAddress.getByName(ipAddress);
        outgoingPort = 9000;
        currentInstance = this;
        mother.setUUID(UUID.fromString("0a1821f3-4273-4f34-8be5-c167dd7669e2"));
        startHeartbeat();
    }

    public static LineFollowModel getCurrentInstance() {
        return currentInstance;
    }

    public void closeApplication() {
        stopListening();
        stopHeartbeat();
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

    private void startHeartbeat() throws SocketException, UnknownHostException {
        heartbeatReceiver.setListener(
                new HeartbeatResponseListener() {
                    @Override
                    public void onRemoteDeviceResponseReceived(IZZY izzy) {
                        //System.out.println("Got here.");
                        Iterator itr = mother.getIzzyUnits().iterator();
                        boolean exists = false;
                        while(itr.hasNext()) {
                            IZZY itrIzzy = (IZZY)itr.next();
                            if(itrIzzy.getUUID().equals(izzy.getUUID())) {
                                itrIzzy.setStatus(izzy.getStatus());
                                itrIzzy.setName(izzy.getName());
                                exists = true;
                            }
                        }
                        if(!exists) mother.getIzzyUnits().add(izzy);
                    }

                    @Override
                    public void onRemoteDeviceTimeout() {
                        //TODO: Implement some timeout features if no devices have connected
                        return;
                    }
                }
        );
        executor.submit(heartbeatSender);
        executor.submit(heartbeatReceiver);
    }

    private void stopHeartbeat() {
        heartbeatSender.stopBeating();
    }
}
