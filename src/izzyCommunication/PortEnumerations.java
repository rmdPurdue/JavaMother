package izzyCommunication;

/**
 * @author Rich Dionne
 * @project JavaMother
 * @package comms
 * @date 12/6/2018
 */
public enum PortEnumerations {
    UDP_SEND_PORT(9001),
    UDP_RECEIVE_PORT(9000),
    OSC_SEND_PORT(8000),
    OSC_RECEIVE_PORT(8001);

    private int port;

    PortEnumerations(int port) {
        this.port = port;
    }

    public int getValue() {
        return port;
    }
}
