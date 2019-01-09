package comms;

import util.IZZY;
import util.IZZYStatus;
import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import static comms.MessageType.getMessageType;
import static comms.PortEnumerations.UDP_RECEIVE_PORT;

/**
 * @author Rich Dionne
 * @project JavaMother
 * @package comms
 * @date 12/8/2018
 */
public class HeartBeatResponder implements Runnable {

    public interface HeartBeatResponseListener {
        void onRemoteDeviceResponseReceived(IZZY izzy);
    }

    private HeartBeatResponseListener listener;
    private DatagramSocket listenerSocket = new DatagramSocket(UDP_RECEIVE_PORT.getValue());
    private AtomicBoolean running = new AtomicBoolean(false);

    public HeartBeatResponder() throws SocketException, UnknownHostException {
    }

    public void setListener(HeartBeatResponseListener listener) {
        this.listener = listener;
    }

    public void stopBeating() {
        running.set(false);
        listenerSocket.close();
        Thread.currentThread().interrupt();
    }

    @Override
    public void run() {
        running.set(true);

        while(running.get()) {
            //System.out.println("Listening...");
            byte[] buf = new byte[256];

            DatagramPacket receivedPacket = new DatagramPacket(buf, buf.length);
            try {
                listenerSocket.receive(receivedPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //System.out.println("Message received.");

            InetAddress packetAddress = receivedPacket.getAddress();

            HeartbeatMessage message;
            try {
                message = new HeartbeatMessage(Arrays.copyOfRange(receivedPacket.getData(), 0, receivedPacket.getLength()));
                IZZY izzy = new IZZY();
                izzy.setIpAddress(packetAddress);
                izzy.setUUID(message.getSenderUUID());
                switch (getMessageType(message.getMessageType())) {
                    case HERE:
                        //System.out.println("Got 'HERE' message.");
                        izzy.setStatus(IZZYStatus.AVAILABLE);
                        izzy.setName(new String(message.getData()));
                        listener.onRemoteDeviceResponseReceived(izzy);
                        break;
                    case MOVING:
                        //System.out.println("Got 'MOVING' message.");
                        izzy.setStatus(IZZYStatus.MOVING);
                        listener.onRemoteDeviceResponseReceived(izzy);
                        break;
                    case NOT_VALID:
                        break;
                }

            } catch (IllegalArgumentException e) {
                System.out.println("Not a valid message.");
            }
        }

    }

}
