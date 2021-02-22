package util;

/**
 * @author Rich Dionne
 * @project javaIZZY-2018
 * @package util
 * @date 12/4/2018
 */
public enum OSCAddresses {
    SIMPLE_MOVE("/IZZY/SimpleMove"),
    FOLLOW_LINE("/IZZY/LineFollow/*"),
    FOLLOW_LINE_FORWARD("/IZZY/LineFollow/Forward"),
    FOLLOW_LINE_BACKWARD("/IZZY/LineFollow/Backward"),
    FOLLOW_LINE_STOP("/IZZY/LineFollow/Stop");

    private String address;

    public String valueOf() {
        return this.address;
    }

    OSCAddresses(String address) {
        this.address = address;
    }
}
