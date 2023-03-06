package izzyCommunication.heartbeat;

import izzyCommunication.heartbeat.devices.IZZY;
import izzyCommunication.heartbeat.devices.Mother;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import static izzyCommunication.PortEnumerations.UDP_SEND_PORT;

/**
 * @author Rich Dionne
 * @project JavaMother
 * @package comms
 * @date 12/6/2018
 */
public class HeartbeatSender implements Runnable {

    private static HeartbeatSender currentInstance;
    private final DatagramSocket heartbeatSocket; // switch to Multicast at some point for multiple device control
    private final AtomicBoolean isHeartbeatRunning;
    private final Mother mother;

    public HeartbeatSender(final UUID motherUUID) throws SocketException {
        currentInstance = this;
        this.heartbeatSocket = new DatagramSocket();
        this.isHeartbeatRunning = new AtomicBoolean(true);
        this.mother = new Mother(motherUUID);
    }

    @Override
    public void run() {
        while(isHeartbeatRunning.get()) {
            sendBeat();
        }
    }

    public void addIzzy(UUID izzyID, InetAddress izzyIP) {
        this.mother.addIzzyUnit(new IZZY(izzyID, izzyIP));
    }

    public void removeIzzy(UUID izzyId) {
        this.mother.removeIzzyUnit(izzyId);
    }

    public void stopBeating() {
        isHeartbeatRunning.set(false);
        heartbeatSocket.close();
        Thread.currentThread().interrupt();
    }

    public static HeartbeatSender getCurrentInstance() {
        return currentInstance;
    }

    private void sendBeat() {
        HeartbeatMessage message = new HeartbeatMessage(MessageType.HELLO.getValue());
        message.setSenderUUID(mother.getUUID());

        for (IZZY izzy : mother.getIzzyUnits()) {
            try {
                message.setReceiverUUID(izzy.getUUID());
                DatagramPacket heartbeatPacket = new DatagramPacket(message.getMessage(), message.getMessage().length, izzy.getIpAddress(), UDP_SEND_PORT.getValue());
                heartbeatSocket.send(heartbeatPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
