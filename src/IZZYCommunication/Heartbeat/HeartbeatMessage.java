package IZZYCommunication.Heartbeat;

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
    private byte[] length; // keep this under 256 bytes (Can probably bump to 512 bytes safely)
    private UUID senderUUID;
    private byte[] senderUUIDHigh;
    private byte[] senderUUIDLow;
    private UUID receiverUUID;
    private byte[] receiverUUIDHigh;
    private byte[] receiverUUIDLow;
    private byte messageType;
    private byte[] data;

    public HeartbeatMessage(byte messageType) {
        this.preamble = 0x10;
        this.identifier = new byte[]{(byte)0x69, (byte)0x7a, (byte)0x7a, (byte)0x79, (byte)0x6d, (byte)0x65, (byte)0x73, (byte)0x73, (byte)0x61, (byte)0x67, (byte)0x65}; // 'izzymessage'
        this.length = toByteArray(47); // 1 preamble, 11 identifier, 2 length, 16 SenderUUID, 16 Receiver UUID, 1 type = 47 bytes
        this.senderUUID = null;
        this.senderUUIDHigh = new byte[8];
        this.senderUUIDLow = new byte[8];
        this.receiverUUID = null;
        this.receiverUUIDHigh = new byte[8];
        this.receiverUUIDLow = new byte[8];
        this.messageType = messageType;
        this.data = null;
    }

    public HeartbeatMessage(byte[] rawData) {
        this.preamble = 0x10;
        this.identifier = new byte[]{(byte)0x69, (byte)0x7a, (byte)0x7a, (byte)0x79, (byte)0x6d, (byte)0x65, (byte)0x73, (byte)0x73, (byte)0x61, (byte)0x67, (byte)0x65}; // 'izzymessage'
        this.length = toByteArray(47); // 1 preamble, 11 identifier, 2 length, 16 SenderUUID, 16 Receiver UUID, 1 type = 47 bytes
        this.senderUUID = null;
        this.senderUUIDHigh = new byte[8];
        this.senderUUIDLow = new byte[8];
        this.receiverUUID = null;
        this.receiverUUIDHigh = new byte[8];
        this.receiverUUIDLow = new byte[8];
        this.messageType = 0x00;
        if(!parsePacket(rawData)) throw new IllegalArgumentException("Not a valid message.");
    }

    public UUID getSenderUUID() {
        if(senderUUID == null) {
            senderUUID = new UUID(toLong(senderUUIDHigh), toLong(senderUUIDLow));
        }
        return senderUUID;
    }

    public void setSenderUUID(UUID uuid) {
        this.senderUUID = uuid;
        long uuidMSB = senderUUID.getMostSignificantBits();
        long uuidLSB = senderUUID.getLeastSignificantBits();
        senderUUIDHigh = toByteArray(uuidMSB);
        senderUUIDLow = toByteArray(uuidLSB);
    }

    public UUID getReceiverUUID() {
        if(receiverUUID == null) {
            receiverUUID = new UUID(toLong(receiverUUIDHigh), toLong(receiverUUIDLow));
        }
        return receiverUUID;
    }

    public void setReceiverUUID(UUID uuid) {
        this.receiverUUID = uuid;
        long uuidMSB = receiverUUID.getMostSignificantBits();
        long uuidLSB = receiverUUID.getLeastSignificantBits();
        receiverUUIDHigh = toByteArray(uuidMSB);
        receiverUUIDLow = toByteArray(uuidLSB);
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
        int length = 47 + data.length;
        this.length = toByteArray(length);
    }

    public byte[] getMessage() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.write(preamble);
        stream.write(identifier);
        stream.write(length);
        stream.write(senderUUIDHigh);
        stream.write(senderUUIDLow);
        stream.write(receiverUUIDHigh);
        stream.write(receiverUUIDLow);
        stream.write(messageType);
        if(data != null) stream.write(data);
        return stream.toByteArray();
    }

    private boolean parsePacket(byte[] rawData) {
        if(rawData[0] == preamble) {
            if(Arrays.equals(Arrays.copyOfRange(rawData, 1, 12), identifier)) {
                if((rawData.length) == toInteger(Arrays.copyOfRange(rawData, 12, 14))) {
                    this.senderUUIDHigh = Arrays.copyOfRange(rawData, 14, 22);
                    this.senderUUIDLow = Arrays.copyOfRange(rawData, 22, 30);
                    this.receiverUUIDHigh = Arrays.copyOfRange(rawData, 30, 38);
                    this.receiverUUIDLow = Arrays.copyOfRange(rawData, 38, 46);
                    this.messageType = rawData[46];
                    if (rawData.length > 47) {
                        this.data = Arrays.copyOfRange(rawData, 47, rawData.length);
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
