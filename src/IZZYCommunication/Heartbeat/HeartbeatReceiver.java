package IZZYCommunication.Heartbeat;

import IZZYCommunication.Heartbeat.Devices.IZZY;
import IZZYCommunication.Heartbeat.Devices.Mother;

import static IZZYCommunication.Heartbeat.MessageType.getMessageType;
import static IZZYCommunication.PortEnumerations.UDP_RECEIVE_PORT;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Rich Dionne
 * @project JavaMother
 * @package IZZYCommunication
 * @date 12/8/2018
 */
public class HeartbeatReceiver implements Runnable {

    private static HeartbeatReceiver currentInstance;
    private final DatagramSocket heartbeatSocket;
    private final AtomicBoolean isHeartbeatRunning;
    private final Mother mother;
    private final HashMap<UUID, HeartbeatResponseListener> listenerMap;

    public HeartbeatReceiver(UUID motherUUID) throws SocketException {
        currentInstance = this;
        this.heartbeatSocket = new DatagramSocket(UDP_RECEIVE_PORT.getValue());
        this.isHeartbeatRunning = new AtomicBoolean(true);
        this.mother = new Mother(motherUUID);
        this.listenerMap = new HashMap<>();
    }

    @Override
    public void run() {
        try {
            heartbeatSocket.setSoTimeout(5000); // 5 seconds, no comm from any IZZY
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        while(isHeartbeatRunning.get()) {
            listenForResponse();
        }
        stopBeating();
    }

    public void stopBeating() {
        isHeartbeatRunning.set(false);
        heartbeatSocket.close();
    }

    public void addListener(UUID uuid, InetAddress ipAddress, HeartbeatResponseListener listener) {
        this.mother.addIzzyUnit(new IZZY(uuid, ipAddress));
        this.listenerMap.put(uuid, listener);
    }

    public void removeListener(UUID uuid) {
        this.mother.removeIzzyUnit(uuid);
        this.listenerMap.remove(uuid);
    }

    public static HeartbeatReceiver getCurrentInstance() {
        return currentInstance;
    }

    private void listenForResponse() {
        byte[] buf = new byte[256];

        DatagramPacket receivedPacket = new DatagramPacket(buf, buf.length);
        try {
            heartbeatSocket.receive(receivedPacket);
            processPacket(receivedPacket);
        } catch (SocketTimeoutException timeout) {
            for (HeartbeatResponseListener entry : listenerMap.values()) {
                entry.onMotherDeviceTimeout();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            izzyCheckup();
        }
    }

    private void processPacket(DatagramPacket receivedPacket) {
        HeartbeatMessage message;
        try {
            message = new HeartbeatMessage(Arrays.copyOfRange(receivedPacket.getData(), 0, receivedPacket.getLength()));
        } catch (IllegalArgumentException e) {
            System.out.println("Not a valid message.");
            return;
        }

        if (!verifyMessage(message, receivedPacket.getAddress())) {
            return;
        }

        IZZY izzy = mother.getIzzy(message.getSenderUUID());
        izzy.setLastContact(System.currentTimeMillis());

        switch (getMessageType(message.getMessageType())) {
            case HERE:
                System.out.println("Got 'HERE' message.");
                izzy.setStatus(IZZYStatus.AVAILABLE);
                listenerMap.get(message.getSenderUUID()).onRemoteDeviceResponseReceived(IZZYStatus.AVAILABLE);
                break;
            case MOVING:
                System.out.println("Got 'MOVING' message.");
                izzy.setStatus(IZZYStatus.MOVING);
                listenerMap.get(message.getSenderUUID()).onRemoteDeviceResponseReceived(IZZYStatus.MOVING);
                break;
            case SETUP_ERROR:
                System.out.println("Error During IZZY Setup");
                izzy.setStatus(IZZYStatus.BROKEN);
                listenerMap.get(message.getSenderUUID()).onRemoteDeviceResponseReceived(IZZYStatus.BROKEN);
            default:
                System.out.println("Invalid Message Type");
                break;
        }
    }

    private boolean verifyMessage(HeartbeatMessage message, InetAddress packetIp) {
        IZZY izzy = mother.getIzzy(message.getSenderUUID());
        if (izzy == null) {
            return false;
        }

        if (izzy.getIpAddress().equals(packetIp)) {
            return true;
        } else {
            System.out.println("WARNING: received invalid packet with uuid: " + message.getSenderUUID().toString() +
                    " and ipAddress: " + packetIp.toString() + ". Correct IP: " +
                    izzy.getIpAddress().toString());
            return false;
        }
    }

    public void izzyCheckup() {
        for (IZZY izzy : mother.getIzzyUnits()) {
            if ((izzy.getLastContact() + 5000) < System.currentTimeMillis()) { // 5 seconds, no comm from this IZZY
                izzy.setStatus(IZZYStatus.MISSING);
                listenerMap.get(izzy.getUUID()).onRemoteDeviceTimeout();
            }
        }
    }

}
