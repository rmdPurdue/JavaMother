package IZZYCommunication;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

/**
 * @author Rich Dionne
 * @project javaIZZY-2018
 * @package comm
 * @date 12/7/2018
 */
public class HeartbeatMessage {
    private final byte preamble;
    private final byte[] identifier;
    private byte[] length;
    private UUID senderUUID;
    private byte[] uuidHigh;
    private byte[] uuidLow;
    private byte messageType;
    private byte[] data;

    public HeartbeatMessage(byte messageType) {
        this.preamble = 0x10;
        this.identifier = new byte[]{(byte)0x69, (byte)0x7a, (byte)0x7a, (byte)0x79, (byte)0x6d, (byte)0x65, (byte)0x73, (byte)0x73, (byte)0x61, (byte)0x67, (byte)0x65}; // 'izzymessage'
        this.length = toByteArray(31); // 1 preamble, 11 identifier, 2 length, 16 UUID, 1 type = 31 bytes
        this.senderUUID = null;
        this.uuidHigh = new byte[8];
        this.uuidLow = new byte[8];
        this.messageType = messageType;
        this.data = null;
    }

    public HeartbeatMessage(byte[] rawData) {
        this.preamble = 0x10;
        this.identifier = new byte[]{(byte)0x69, (byte)0x7a, (byte)0x7a, (byte)0x79, (byte)0x6d, (byte)0x65, (byte)0x73, (byte)0x73, (byte)0x61, (byte)0x67, (byte)0x65}; // 'izzymessage'
        this.length = toByteArray(31); // 1 preamble, 11 identifier, 2 length, 16 UUID, 1 type = 31 bytes
        this.senderUUID = null;
        this.uuidHigh = new byte[8];
        this.uuidLow = new byte[8];
        this.messageType = 0x00;
        if(!parsePacket(rawData)) throw new IllegalArgumentException("Not a valid message.");
    }

    public UUID getSenderUUID() {
        if(senderUUID == null) {
            senderUUID = new UUID(toLong(uuidHigh), toLong(uuidLow));
        }
        return senderUUID;
    }

    public void setSenderUUID(UUID uuid) {
        this.senderUUID = uuid;
        long uuidMSB = senderUUID.getMostSignificantBits();
        long uuidLSB = senderUUID.getLeastSignificantBits();
        uuidHigh = toByteArray(uuidMSB);
        uuidLow = toByteArray(uuidLSB);
    }

    public byte getMessageType() {
        return messageType;
    }

    public void setMessageType(byte messageType) {
        this.messageType = messageType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
        int length = 31 + data.length;
        this.length = toByteArray(length);
    }

    public byte[] getMessage() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.write(preamble);
        stream.write(identifier);
        stream.write(length);
        stream.write(uuidHigh);
        stream.write(uuidLow);
        stream.write(messageType);
        if(data != null) stream.write(data);
        return stream.toByteArray();
    }

    private boolean parsePacket(byte[] rawData) {
        if(rawData[0] == preamble) {
            if(Arrays.equals(Arrays.copyOfRange(rawData, 1, 12), identifier)) {
                if((rawData.length) == toInteger(Arrays.copyOfRange(rawData, 12, 14))) {
                    this.uuidHigh = Arrays.copyOfRange(rawData, 14, 22);
                    this.uuidLow = Arrays.copyOfRange(rawData, 22, 30);
                    this.senderUUID = new UUID(toLong(uuidHigh), toLong(uuidLow));
                    this.messageType = rawData[30];
                    if (rawData.length > 31) {
                        this.data = Arrays.copyOfRange(rawData, 31, rawData.length);
                    } else {
                        this.data = null;
                    }
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private byte[] toByteArray(int value) {
        return new byte[] {(byte) (value >> 8), (byte) value};
    }

    private byte[] toByteArray(long value) {
        return new byte[] {
                (byte)(value >> 56),
                (byte)(value >> 48),
                (byte)(value >> 40),
                (byte)(value >> 32),
                (byte)(value >> 24),
                (byte)(value >> 16),
                (byte)(value >> 8),
                (byte)value
        };
    }

    private int toInteger(byte[] bytes) {
        return bytes[0] << 8 | bytes[1] & 0xFF;
    }

    private long toLong(byte[] bytes) {
        return ((long)(bytes[0] & 0xFF) << 56) |
                ((long)(bytes[1] & 0xFF) << 48) |
                ((long)(bytes[2] & 0xFF) << 40) |
                ((long)(bytes[3] & 0xFF) << 32) |
                ((long)(bytes[4] & 0xFF) << 24) |
                ((long)(bytes[5] & 0xFF) << 16) |
                ((long)(bytes[6] & 0xFF) << 8) |
                ((long)(bytes[7] & 0xFF));
    }


}
