package izzyCommunication.lineFollowing;

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
    FOLLOW_LINE_THRESHOLD("/IZZY/FollowLineThreshold"),
    STOP_PROCESSING("/IZZY/StopProcessing"),
    RESET_SYSTEM("/IZZY/ResetSystem"),
    FOLLOW_LINE_STOP("/IZZY/eStop"),
    SET_SENSOR_RANGES("/IZZY/ranges");

    private String address;

    public String valueOf() {
        return this.address;
    }

    OSCAddresses(String address) {
        this.address = address;
    }
}
