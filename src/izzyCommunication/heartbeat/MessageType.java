package izzyCommunication.heartbeat;

/**
 * @author Rich Dionne
 * @project javaIZZY-2018
 * @package comm
 * @date 12/7/2018
 */
public enum MessageType {
    HELLO ((byte)0x01),
    HERE ((byte)0x02),
    SETUP_ERROR((byte)0x03),
    MOVING ((byte)0x04),
    BROKEN ((byte)0x05),
    OSC_COM_ERROR((byte)0x06),
    NOT_VALID((byte)0x00);

    private final byte value;

    MessageType(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public static MessageType getMessageType(byte value) {
        for(MessageType type : MessageType.values()) {
            if(type.getValue() == value) return type;
        }
        return NOT_VALID;
    }

}
