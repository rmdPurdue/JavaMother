package util;

import java.net.InetAddress;
import java.util.UUID;

import static util.IZZYStatus.*;

/**
 * @author Rich Dionne
 * @project JavaMother
 * @package util
 * @date 12/6/2018
 */
public class IZZY {
    private String name = null;
    private InetAddress ipAddress = null;
    private int port;
    private UUID uuid = null;
    private IZZYStatus status = MISSING;

    public IZZY() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public IZZYStatus getStatus() {
        return status;
    }

    public void setStatus(IZZYStatus status) {
        this.status = status;
    }
}
