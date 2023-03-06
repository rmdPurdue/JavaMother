package lineFollow;

import izzyCommunication.lineFollowing.MotherOSCReceiverLineFollow;
import izzyCommunication.lineFollowing.MotherOSCSenderLineFollow;
import izzyCommunication.lineFollowing.OSCAddresses;
import izzyCommunication.logging.LogServer;
import lineFollow.mapping.MapLoop;
import lineFollow.plotting.PlotLoop;
import com.illposed.osc.OSCMessage;
import izzyCommunication.heartbeat.HeartbeatResponseListener;
import izzyCommunication.heartbeat.HeartbeatSender;
import izzyCommunication.heartbeat.HeartbeatReceiver;
import izzyCommunication.heartbeat.IZZYStatus;

import java.io.FileNotFoundException;
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
    private MotherOSCReceiverLineFollow receiver;
    private LineFollowController lineFollowController;
    private LineFollowSensorLogger sensorLogger;
    private LogServer logServer;
    private PlotLoop plotLoop;
    private MapLoop mapLoop;

    public LineFollowModel(InetAddress ipAddress) throws UnknownHostException, SocketException {
        currentInstance = this;
        this.izzyUUID = UUID.randomUUID();
        this.heartbeatSender = HeartbeatSender.getCurrentInstance();
        this.heartbeatReceiver = HeartbeatReceiver.getCurrentInstance();
        this.outgoingAddress = ipAddress;
        startHeartbeat();
        this.sender = new MotherOSCSenderLineFollow(ipAddress);

        try {
            this.sensorLogger = new LineFollowSensorLogger();
            this.plotLoop = new PlotLoop();
            this.mapLoop = new MapLoop();
            this.receiver = new MotherOSCReceiverLineFollow(ipAddress, sensorLogger, plotLoop, mapLoop);
            this.logServer = new LogServer();
        } catch (FileNotFoundException e) {
            this.receiver = new MotherOSCReceiverLineFollow(ipAddress);
            e.printStackTrace();
        }
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
            case SET_SENSOR_RANGES:
                sender.sendSensorRangeMessage(message);
        }
    }

    public void stopSending() {
        sender.close();
    }

    public void setLineFollowController(final LineFollowController controller) {
        this.lineFollowController = controller;
        this.receiver.setLineFollowController(controller);
        this.logServer.setLineFollowController(controller);
        this.plotLoop.setLineFollowController(controller);
        this.mapLoop.setLineFollowController(controller);
        this.logServer.start();
    }

    public LineFollowController getLineFollowController() {
        return lineFollowController;
    }

    private void startHeartbeat() {
        this.heartbeatReceiver.addListener(izzyUUID, outgoingAddress, this);
        this.heartbeatSender.addIzzy(izzyUUID, outgoingAddress);
    }

    private void stopHeartbeat() {
        heartbeatSender.removeIzzy(izzyUUID);
    }

    public void restartLogging() {
        if (sensorLogger != null) {
            sensorLogger.newFile();
        }
    }

    public void clearPlotData() {
        plotLoop.clearPlotData();
        mapLoop.clearPlotData();
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
    public void onError() {
        if (lineFollowController != null) {
            lineFollowController.toggleConnectionStatus(IZZYStatus.BROKEN);
        }
    }

    @Override
    public void onMotherDeviceTimeout() {
        return;
    }
}
