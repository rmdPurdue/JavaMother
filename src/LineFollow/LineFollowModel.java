package LineFollow;

import IZZYCommunication.LineFollowing.MotherOSCReceiverLineFollow;
import IZZYCommunication.LineFollowing.MotherOSCSenderLineFollow;
import IZZYCommunication.LineFollowing.OSCAddresses;
import com.illposed.osc.OSCMessage;
import IZZYCommunication.Heartbeat.HeartbeatResponseListener;
import IZZYCommunication.Heartbeat.HeartbeatSender;
import IZZYCommunication.Heartbeat.HeartbeatReceiver;
import IZZYCommunication.Heartbeat.IZZYStatus;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.UUID;

public class LineFollowModel implements HeartbeatResponseListener {
    private static LineFollowModel currentInstance;

    private final UUID izzyUUID;
    private final HeartbeatSender heartbeatSender;
    private final HeartbeatReceiver heartbeatReceiver;
    private final InetAddress outgoingAddress;
    private final MotherOSCSenderLineFollow sender;
    private final MotherOSCReceiverLineFollow receiver;
    private LineFollowController lineFollowController;

    public LineFollowModel(InetAddress ipAddress) throws UnknownHostException, SocketException {
        currentInstance = this;
        this.izzyUUID = UUID.randomUUID();
        this.heartbeatSender = HeartbeatSender.getCurrentInstance();
        this.heartbeatReceiver = HeartbeatReceiver.getCurrentInstance();
        this.outgoingAddress = ipAddress;
        startHeartbeat();

        this.sender = new MotherOSCSenderLineFollow(ipAddress);
        this.receiver = new MotherOSCReceiverLineFollow(ipAddress);
    }

    public static LineFollowModel getCurrentInstance() {
        return currentInstance;
    }

    public void closeApplication() {
        stopListening();
        stopSending();
        stopHeartbeat();
    }

    public void startListening() {
        receiver.startListening();
    }

    public void stopListening() {
        System.out.println("Mother is no longer listening for IZZY");
        receiver.stopListening();
    }

    public void sendMessage(OSCMessage message, OSCAddresses oscAddress) throws IOException {
        switch (oscAddress) {
            case FOLLOW_LINE_STATE:
                sender.sendLineStateMessage(message);
                break;
            case FOLLOW_LINE_SPEED:
                sender.sendLineSpeedMessage(message);
                break;
            case FOLLOW_LINE_TUNE:
                sender.sendLineTuneMessage(message);
                break;
            case FOLLOW_LINE_THRESHOLD:
                sender.sendThresholdMessage(message);
                break;
            case STOP_PROCESSING:
                sender.sendLineStopMessage(message);
                break;
            case RESET_SYSTEM:
                sender.sendLineResetMessage(message);
                break;
            case FOLLOW_LINE_STOP:
                sender.sendLineEStopMessage(message);
                break;
        }
    }

    public void stopSending() {
        sender.close();
    }

    public void setLineFollowController(final LineFollowController controller) {
        this.lineFollowController = controller;
        this.receiver.setLineFollowController(controller);
    }

    private void startHeartbeat() {
        this.heartbeatReceiver.addListener(izzyUUID, outgoingAddress, this);
        this.heartbeatSender.addIzzy(izzyUUID, outgoingAddress);
    }

    private void stopHeartbeat() {
        heartbeatSender.removeIzzy(izzyUUID);
    }

    /**
     * Loops through all IZZY units known by mother (RIGHT NOW JUST 1)
     * and updates the status of each. Then calls the Controller toggle
     * function to update the view displayed to the user.
     */
    @Override
    public void onRemoteDeviceResponseReceived(IZZYStatus izzyStatus) {
        if (lineFollowController != null) {
            lineFollowController.toggleConnectionStatus(izzyStatus);
        }
    }

    @Override
    public void onRemoteDeviceTimeout() {
        if (lineFollowController != null) {
            lineFollowController.toggleConnectionStatus(IZZYStatus.MISSING);
        }
    }

    @Override
    public void onMotherDeviceTimeout() {
        return;
    }
}
