package LineFollowMother;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;
import com.illposed.osc.OSCPortOut;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class LineFollowModel {
    public OSCPortIn receiver = null;
    public OSCListener listener = null;
    private static LineFollowModel currentInstance;

    final InetAddress outgoingAddress;
    final int outgoingPort;

    public LineFollowModel() throws UnknownHostException {
        outgoingAddress = InetAddress.getByName("192.168.2.5");
        outgoingPort = 9000;
        currentInstance = this;
    }

    public static LineFollowModel getCurrentInstance() {
        return currentInstance;
    }

    public void startListening() throws IOException {
        receiver = new OSCPortIn(8001);
        listener = (time, message) -> {
            System.out.println("Message Received from IZZY");
            OSCParser(message);
        };
        receiver.startListening();
        receiver.addListener("/helloWorld", listener);
        System.out.println("Mother is now listening for IZZY via OSC\n");
    }

    public void sendMessage(OSCMessage message) throws IOException {
        OSCPortOut sender = new OSCPortOut(outgoingAddress, outgoingPort);
        sender.send(message);
        sender.close();
    }

    private void OSCParser(OSCMessage message){
        String address = message.getAddress();

        if(address.equals("/mother/status")) {
            //updateCurrentPosition(message);
        } else if(address.equals("/mother/LineFollow/Update")){
            System.out.println("Received line follow update!");
            System.out.println(message.getArguments().indexOf(0));
        } else if (address.equals("/helloWorld")) {
            System.out.println("Hello World!!");
        }
    }
}
