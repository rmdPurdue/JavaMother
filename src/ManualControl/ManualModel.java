package ManualControl;

import Location.Position;
import Location.StageArea;
import Location.Trajectory;
import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;
import com.illposed.osc.OSCPortOut;
import javafx.application.Platform;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Objects;

public class ManualModel {
    public OSCPortIn receiver = null;
    public OSCListener listener = null;
    public Position currentPosition = new Position(Color.RED);
    public Position centerPosition = new Position(Color.BLUE);
    public Position targetPosition = new Position(Color.GREEN);
    public StageArea stageArea = new StageArea();
    public Trajectory targetTrajectory = new Trajectory(currentPosition, targetPosition, stageArea.getPixelRatio());
    private static ManualModel currentInstance;

    InetAddress outgoingAddress = InetAddress.getByName("192.168.2.4");
    int outgoingPort = 8000;

    public ManualModel() throws UnknownHostException {
        currentInstance = this;
    }

    public static ManualModel getCurrentInstance() {
        return currentInstance;
    }

    public void startListening() throws SocketException, IOException {
        receiver = new OSCPortIn(8001);
        listener = (time, message) -> {
            System.out.println("Message received from IZZY!");
            OSCParser(message);
        };
        receiver.startListening();
        receiver.addListener("/helloWorld",listener);
        receiver.addListener("/mother/status", listener);
        receiver.addListener("/mother/Units", listener);
        receiver.addListener("/mother/LineFollow/Update", listener);
        //receiver.startListening();
        System.out.println("Mother is now listening for IZZY via OSC");
        System.out.println();


        CurveMove myMove = new CurveMove();
        myMove.addPoint(0,0);
        myMove.addPoint(0,20);
        myMove.addPoint(50,100);
        myMove.addPoint(150,300);
        myMove.addPoint(300,500);
        myMove.addPoint(500,650);
        myMove.addPoint(700,800);
        myMove.addPoint(900,1000);
        myMove.addPoint(1000,1200);
        myMove.addPoint(1200,1350);
        myMove.addPoint(1400,1450);
        myMove.addPoint(1500,1500);


        OSCMessage outgoingMessage = myMove.generateMessage();
        /*
        OSCMessage outgoingMessage = new OSCMessage();
        outgoingMessage.setAddress("/IZZY/ManualControl.CurveMove");
        outgoingMessage.addArgument(5);
        outgoingMessage.addArgument(3000);
        outgoingMessage.addArgument(0);
        OSCMessage outgoingMessage2 = new OSCMessage();
        outgoingMessage2.setAddress("/IZZY/Turn");
        outgoingMessage2.addArgument(0);
        outgoingMessage2.addArgument(90);
        outgoingMessage2.addArgument(10);
        */
        OSCPortOut sender = new OSCPortOut(outgoingAddress, outgoingPort);
        sender.send(outgoingMessage);
        sender.close();
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
        */
        targetTrajectory.calculateMotionProfileCubic();

        OSCMessage outgoingMessage = new OSCMessage();
        outgoingMessage.setAddress("/IZZY/LinearMove");
        outgoingMessage.addArgument(targetTrajectory.getTotalTime());
        outgoingMessage.addArgument(targetTrajectory.getTotalDistance());
        outgoingMessage.addArgument(targetTrajectory.getAccelerationTime());
        outgoingMessage.addArgument(targetTrajectory.getFinalAccelerationPosition());
        outgoingMessage.addArgument(targetTrajectory.getDecelerationTime());
        outgoingMessage.addArgument(targetTrajectory.getInitialDecelerationPosition());
        outgoingMessage.addArgument(targetTrajectory.getMaxVelocity());
        OSCPortOut sender = new OSCPortOut(outgoingAddress, outgoingPort);
        sender.send(outgoingMessage);
        sender.close();

    }

    private void OSCParser(OSCMessage message){
        String address = message.getAddress();

        if(Objects.equals(address, "/mother/status")) {
            updateCurrentPosition(message);
        }else if(Objects.equals(address, "/mother/LineFollow/Update")){
            System.out.println("Received line follow update!");
            System.out.println(message.getArguments().indexOf(0));
        }
    }
}
