package mvc;

import Location.Position;
import Location.StageArea;
import Location.Trajectory;
import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;
import com.illposed.osc.OSCPortOut;
import comms.HeartBeat;
import comms.HeartBeatResponder;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import util.IZZY;
import util.Mother;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static comms.PortEnumerations.*;
import static util.OSCAddresses.*;

public class Model {

    public Position currentPosition = new Position(Color.RED);
    public Position centerPosition = new Position(Color.BLUE);
    public Position targetPosition = new Position(Color.GREEN);
    public StageArea stageArea = new StageArea();
    public Trajectory targetTrajectory = new Trajectory(currentPosition, targetPosition, stageArea.getPixelRatio());

    private InetAddress outgoingAddress = InetAddress.getByName("10.101.1.3");

    private Mother mother = new Mother();
    private HeartBeat heartBeat = new HeartBeat(mother);
    private HeartBeatResponder heartBeatResponder = new HeartBeatResponder();
    private ExecutorService executor = Executors.newFixedThreadPool(5);

    private static Scanner scanner = new Scanner( System.in );

    public Model() throws IOException {
        mother.setUUID(UUID.fromString("0a1821f3-4273-4f34-8be5-c167dd7669e2"));

        // If I'm using UUIDs instead of MAC addresses, do I need this at all?
        // selectNetworkInterface();

        startHeartbeat();
    }

    public void startListening() throws SocketException {
        OSCPortIn receiver = new OSCPortIn(OSC_RECEIVE_PORT.getValue());
        OSCListener listener = (time, message) -> {
            if(Objects.equals(message.getAddress(), "/mother/status")) updateCurrentPosition(message);
        };

        receiver.addListener("/mother/status", listener);
        receiver.startListening();
    }

    public void setCenterPosition(int x, int y, int z) {
        centerPosition.getX().setPosition(x);
        centerPosition.getY().setPosition(y);
        centerPosition.getZ().setPosition(z);
    }

    private void updateCurrentPosition(OSCMessage message) {
        List<Object> args = message.getArguments();
        Platform.runLater(() -> {
            currentPosition.getX().setPosition((int) args.get(0));
            currentPosition.getY().setPosition((int) args.get(1));
            currentPosition.getZ().setPosition((int) args.get(2));

            currentPosition.getX().setVelocity((int) args.get(3));
            currentPosition.getY().setVelocity((int) args.get(4));
            currentPosition.getZ().setVelocity((int) args.get(5));

            currentPosition.getX().setAcceleration((int) args.get(6));
            currentPosition.getY().setAcceleration((int) args.get(7));
            currentPosition.getZ().setAcceleration((int) args.get(8));

            currentPosition.getX().setAngularPosition((int) args.get(9));
            currentPosition.getY().setAngularPosition((int) args.get(10));
            currentPosition.getZ().setAngularPosition((int) args.get(11));

            currentPosition.getX().setAngularVelocity((int) args.get(12));
            currentPosition.getY().setAngularVelocity((int) args.get(13));
            currentPosition.getZ().setAngularVelocity((int) args.get(14));

            currentPosition.getX().setAngularAcceleration((int) args.get(15));
            currentPosition.getY().setAngularAcceleration((int) args.get(16));
            currentPosition.getZ().setAngularAcceleration((int) args.get(17));
            currentPosition.setUpdated(true);
        });
    }

    public void setStageArea() {
        stageArea.setDownstageDepthInInches(96);
        stageArea.setUpstageDepthInInches(240);
        stageArea.setWidthInInches(480);
        stageArea.updatePixelRatio();
    }

    public void setTargetPosition(int x, int y, int z, int heading) {
        targetPosition.getX().setPosition(x);
        targetPosition.getY().setPosition(y);
        targetPosition.getZ().setPosition(z);
        targetPosition.getX().setAngularPosition(heading);
    }

    public void goToTarget() throws IOException {
        /*
        OSCMessage outgoingMessage = new OSCMessage();
        outgoingMessage.setAddress("/izzy/target");
        outgoingMessage.addArgument(targetPosition.getX().getPosition());
        outgoingMessage.addArgument(targetPosition.getY().getPosition());
        outgoingMessage.addArgument(targetPosition.getZ().getPosition());

        outgoingMessage.addArgument(targetPosition.getX().getVelocity());
        outgoingMessage.addArgument(targetPosition.getY().getVelocity());
        outgoingMessage.addArgument(targetPosition.getZ().getVelocity());

        outgoingMessage.addArgument(targetPosition.getX().getAcceleration());
        outgoingMessage.addArgument(targetPosition.getY().getAcceleration());
        outgoingMessage.addArgument(targetPosition.getZ().getAcceleration());

        outgoingMessage.addArgument(targetPosition.getX().getAngularPosition());
        outgoingMessage.addArgument(targetPosition.getY().getAngularPosition());
        outgoingMessage.addArgument(targetPosition.getZ().getAngularPosition());

        outgoingMessage.addArgument(targetPosition.getX().getAngularVelocity());
        outgoingMessage.addArgument(targetPosition.getY().getAngularVelocity());
        outgoingMessage.addArgument(targetPosition.getZ().getAngularVelocity());

        outgoingMessage.addArgument(targetPosition.getX().getAngularAcceleration());
        outgoingMessage.addArgument(targetPosition.getY().getAngularAcceleration());
        outgoingMessage.addArgument(targetPosition.getZ().getAngularAcceleration());
        OSCPortOut sender = new OSCPortOut(outgoingAddress, outgoingPort);
        sender.send(outgoingMessage);
        sender.close();

        targetTrajectory.calculateMotionProfileCubic();
    */
        OSCMessage outgoingMessage = new OSCMessage();
        outgoingMessage.setAddress(SIMPLE_MOVE.valueOf());
        outgoingMessage.addArgument(10);
        OSCPortOut sender = new OSCPortOut(outgoingAddress, OSC_SEND_PORT.getValue());
        System.out.println("Sending forward message.");
        sender.send(outgoingMessage);
        sender.close();

    }

    private void selectNetworkInterface() throws SocketException {

        // Make list of NICs
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        int i = 1;
        ArrayList<NetworkInterface> networkInterfaces = new ArrayList<>();

        for (NetworkInterface netint : Collections.list(nets)) {
            if (netint.supportsMulticast() && netint.isUp()) {
                networkInterfaces.add(netint);
            }
        }

        // Display NICs menu
        for (NetworkInterface networkInterface : networkInterfaces) {
            System.out.println("Press " + i + " for:");
            System.out.printf("Display name: %s\n", networkInterface.getDisplayName());
            System.out.printf("Name: %s\n", networkInterface.getName());
            Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                System.out.printf("InetAddress: %s\n", inetAddress);
            }
            System.out.printf("\n");
            i++;
        }

        System.out.println("ArrayList length: " + networkInterfaces.size());

        // Wait for user input
        System.out.print("?>");
        String input = scanner.nextLine();

        // Set mother's NIC and update MAC address.
        System.out.println("Selected interface details:");
        System.out.printf("Display name: %s\n", networkInterfaces.get(Integer.parseInt(input)-1).getDisplayName());
        System.out.printf("Name: %s\n", networkInterfaces.get(Integer.parseInt(input)-1).getName());
        //mother.setNetInterface(networkInterfaces.get(Integer.parseInt(input)-1));

    }

    private void startHeartbeat() throws SocketException, UnknownHostException {
        heartBeat.setSenderID(mother.getUUID());

        heartBeatResponder.setListener(izzy -> {
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
        });

        executor.submit(heartBeat);
        executor.submit(heartBeatResponder);
    }

    private void stopHeartbeat() {
        heartBeat.stopBeating();
    }

}
