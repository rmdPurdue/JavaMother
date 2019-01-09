package comms;

import sun.plugin2.message.Message;

/**
 * @author Rich Dionne
 * @project javaIZZY-2018
 * @package comm
 * @date 12/7/2018
 */
public enum MessageType {
    HELLO ((byte)0x01),
    HERE ((byte)0x02),
    MOVING ((byte)0x04),
    NOT_VALID((byte)0x00);

    private byte value;

    MessageType(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public static final MessageType getMessageType(byte value) {
        for(MessageType type : MessageType.values()) {
            if(type.getValue() == value) return type;
        }
        return NOT_VALID;
    }

}
