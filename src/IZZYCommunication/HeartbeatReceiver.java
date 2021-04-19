package IZZYCommunication;

import Devices.IZZY;
import util.IZZYStatus;
import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import static IZZYCommunication.MessageType.getMessageType;
import static IZZYCommunication.PortEnumerations.UDP_RECEIVE_PORT;

/**
 * @author Rich Dionne
 * @project JavaMother
 * @package IZZYCommunication
 * @date 12/8/2018
 */
public class HeartbeatReceiver implements Runnable {

    private HeartbeatResponseListener listener;
    private DatagramSocket heartbeatSocket;
    private InetAddress multicastAddress;
    private AtomicBoolean isHeartbeatRunning;

    public HeartbeatReceiver() throws SocketException, UnknownHostException {
        this.heartbeatSocket = new DatagramSocket(UDP_RECEIVE_PORT.getValue());
        this.multicastAddress = InetAddress.getByName("239.0.0.57");
        this.isHeartbeatRunning = new AtomicBoolean(true);
    }

    public void setListener(HeartbeatResponseListener listener) {
        this.listener = listener;
    }

    public void stopBeating() {
        isHeartbeatRunning.set(false);
        heartbeatSocket.close();
        Thread.currentThread().interrupt();
    }

    @Override
    public void run() {
        try {
            heartbeatSocket.setSoTimeout(30000); // 30 seconds, no comm from IZZY
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        while(isHeartbeatRunning.get()) {
            listenForResponse();
        }

        stopBeating();
    }

    private void listenForResponse() {
        byte[] buf = new byte[256];

        DatagramPacket receivedPacket = new DatagramPacket(buf, buf.length);
        try {
            heartbeatSocket.receive(receivedPacket);
        } catch (SocketTimeoutException timeout) {
            listener.onRemoteDeviceTimeout();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        InetAddress packetAddress = receivedPacket.getAddress();

        HeartbeatMessage message;
        try {
            message = new HeartbeatMessage(Arrays.copyOfRange(receivedPacket.getData(), 0, receivedPacket.getLength()));
        } catch (IllegalArgumentException e) {
            System.out.println("Not a valid message.");
            return;
        }
        IZZY izzy = new IZZY(message.getSenderUUID());
        izzy.setIpAddress(packetAddress);
        switch (getMessageType(message.getMessageType())) {
            case HERE:
                System.out.println("Got 'HERE' message.");
                izzy.setStatus(IZZYStatus.AVAILABLE);
                listener.onRemoteDeviceResponseReceived(izzy);
                break;
            case MOVING:
                System.out.println("Got 'MOVING' message.");
                izzy.setStatus(IZZYStatus.MOVING);
                listener.onRemoteDeviceResponseReceived(izzy);
                break;
            case SETUP_ERROR:
                System.out.println("Error During IZZY Setup");
                izzy.setStatus(IZZYStatus.BROKEN);
                listener.onRemoteDeviceResponseReceived(izzy);
            default:
                System.out.println("Invalid Message Type");
                break;
        }
    }

}
