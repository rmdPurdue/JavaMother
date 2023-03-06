package izzyCommunication;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;

import java.net.InetAddress;
import java.net.SocketException;

import static izzyCommunication.PortEnumerations.OSC_RECEIVE_PORT;

public abstract class MotherOSCReceiver {

    protected final OSCPortIn receiver;

    public MotherOSCReceiver(InetAddress inetAddress) throws SocketException {
        this.receiver = new OSCPortIn(OSC_RECEIVE_PORT.getValue());
    }

    public void addListener(final String address, final OSCListener listener) {
        receiver.addListener(address, listener);
    }

    public void startListening() {
        if (!receiver.isListening()) {
            receiver.startListening();
        }
    }

    public void stopListening() {
        if (receiver.isListening()) {
            receiver.stopListening();
            this.close();
        }
    }

    public void close() {
        if (!receiver.isListening()) {
            receiver.close();
        }
    }

    public abstract void eStopOSC(final OSCMessage msg);

}
