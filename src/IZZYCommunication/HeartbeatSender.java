package IZZYCommunication;

import Devices.Mother;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static IZZYCommunication.PortEnumerations.*;

/**
 * @author Rich Dionne
 * @project JavaMother
 * @package comms
 * @date 12/6/2018
 */
public class HeartbeatSender implements Runnable {

    private DatagramSocket heartbeatSocket;
    private InetAddress multicastAddress;
    private AtomicBoolean isHeartbeatRunning;
    private Mother mother;


    public HeartbeatSender(Mother mother) throws IOException {
        this.heartbeatSocket = new DatagramSocket(UDP_SEND_PORT.getValue()); // the only purpose of specifying a port is so we can manually allow it through the firewall if needed
        this.multicastAddress = InetAddress.getByName("239.0.0.57");
        this.isHeartbeatRunning = new AtomicBoolean(true);
        this.mother = mother;
    }

    public void setMulticastAddress(InetAddress multicastAddress) {
        this.multicastAddress = multicastAddress;
    }

    public void stopBeating() {
        isHeartbeatRunning.set(false);
        heartbeatSocket.close();
        Thread.currentThread().interrupt();
    }

    @Override
    public void run() {
        while(isHeartbeatRunning.get()) {
            sendBeat();
        }
        stopBeating();
    }

    private void sendBeat() {
        System.out.print(".");

        // Might want to consider not spawning a new message every beat to save memory
        HeartbeatMessage message = new HeartbeatMessage(MessageType.HELLO.getValue());
        message.setSenderUUID(mother.getUUID());

        try {
            DatagramPacket heartbeatPacket = new DatagramPacket(message.getMessage(), message.getMessage().length, multicastAddress, UDP_SEND_PORT.getValue());
            heartbeatSocket.send(heartbeatPacket);
            Thread.sleep(250);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
