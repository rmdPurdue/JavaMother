package izzyCommunication.lineFollowing;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;

import static izzyCommunication.lineFollowing.OSCAddresses.*;
import static izzyCommunication.PortEnumerations.OSC_SEND_PORT;

public class MotherOSCSenderLineFollow {
    private final OSCPortOut sender;

    public MotherOSCSenderLineFollow(InetAddress ipAddress) throws SocketException {
        this.sender = new OSCPortOut(ipAddress, OSC_SEND_PORT.getValue());
    }

    public void sendLineStateMessage(OSCMessage message) throws IOException {
        message.setAddress(FOLLOW_LINE_STATE.valueOf());
        sender.send(message);
    }

    public void sendLineSpeedMessage(OSCMessage message) throws IOException {
        message.setAddress(FOLLOW_LINE_SPEED.valueOf());
        sender.send(message);
    }

    public void sendLineTuneMessage(OSCMessage message) throws IOException {
        message.setAddress(FOLLOW_LINE_TUNE.valueOf());
        sender.send(message);
    }

    public void sendThresholdMessage(OSCMessage message) throws IOException {
        message.setAddress(FOLLOW_LINE_THRESHOLD.valueOf());
        sender.send(message);
    }

    public void sendLineStopMessage(OSCMessage message) throws IOException {
        message.setAddress(STOP_PROCESSING.valueOf());
        sender.send(message);
    }

    public void sendLineResetMessage(OSCMessage message) throws IOException {
        message.setAddress(RESET_SYSTEM.valueOf());
        sender.send(message);
    }

    public void sendLineEStopMessage(OSCMessage message) throws IOException {
        message.setAddress(FOLLOW_LINE_STOP.valueOf());
        sender.send(message);
    }

    public void sendSensorRangeMessage(OSCMessage message) throws IOException {
        message.setAddress(SET_SENSOR_RANGES.valueOf());
        sender.send(message);
    }

    public void close() {
        sender.close();
    }
}
