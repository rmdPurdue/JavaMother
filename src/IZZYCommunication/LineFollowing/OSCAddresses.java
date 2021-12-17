package IZZYCommunication.LineFollowing;

/**
 * @author Rich Dionne
 * @project javaIZZY-2018
 * @package util
 * @date 12/4/2018
 */
public enum OSCAddresses {
    FOLLOW_LINE_STATE("/IZZY/FollowLineState"),
    FOLLOW_LINE_SPEED("/IZZY/FollowLineSpeed"),
    FOLLOW_LINE_TUNE("/IZZY/FollowLineTune"),
    STOP_PROCESSING("/IZZY/StopProcessing"),
    FOLLOW_LINE_STOP("/IZZY/eStop");

    private String address;

    public String valueOf() {
        return this.address;
    }

    OSCAddresses(String address) {
        this.address = address;
    }
}
