import Location.Position;
import Location.StageArea;
import Location.Trajectory;
import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;
import javafx.application.Platform;
import javafx.scene.paint.Color;

import java.net.SocketException;
import java.util.List;
import java.util.Objects;

public class Model {

    public Position currentPosition = new Position(Color.RED);
    public Position centerPosition = new Position(Color.BLUE);
    public Position targetPosition = new Position(Color.GREEN);
    public StageArea stageArea = new StageArea();
    Trajectory targetTrajectory = new Trajectory(currentPosition, targetPosition, stageArea.getPixelRatio());

    public void Model() {
    }

    public void startListening() throws SocketException {
        OSCPortIn receiver = new OSCPortIn(8000);
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

}
