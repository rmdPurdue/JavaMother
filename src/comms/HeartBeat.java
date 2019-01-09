package comms;

import util.IZZY;
import util.IZZYStatus;
import util.Mother;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import static comms.MessageType.*;
import static comms.PortEnumerations.*;

/**
 * @author Rich Dionne
 * @project JavaMother
 * @package comms
 * @date 12/6/2018
 */
public class HeartBeat implements Runnable {

    private MulticastSocket heartbeatSocket = new MulticastSocket(UDP_SEND_PORT.getValue());
    private InetAddress multicastAddress = InetAddress.getByName("239.0.0.57");
    private AtomicBoolean running = new AtomicBoolean(false);

    private UUID uuid = null;
    private Mother mother;


    public HeartBeat(Mother mother) throws IOException {
        this.mother = mother;
    }

    public void setSenderID(UUID uuid) {
        this.uuid = uuid;
    }

    public void setMulticastAddress(InetAddress multicastAddress) {
        this.multicastAddress = multicastAddress;
    }

    public void stopBeating() {
        running.set(false);
        heartbeatSocket.close();
        Thread.currentThread().interrupt();
    }

    @Override
    public void run() {
        running.set(true);

        while(running.get()) {
            sendBeat();
        }
    }

    private void sendBeat() {
        //System.out.print(".");

        HeartbeatMessage message = new HeartbeatMessage();
        message.setSenderUUID(uuid);
        message.setMessageType(MessageType.HELLO.getValue());

        DatagramPacket heartbeatPacket;
        try {
            heartbeatPacket = new DatagramPacket(message.getMessage(), message.getMessage().length, multicastAddress, UDP_SEND_PORT.getValue());
            heartbeatSocket.send(heartbeatPacket);
            Thread.sleep(250);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
