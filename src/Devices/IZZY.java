package Devices;

import util.IZZYStatus;

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
    private final UUID uuid;
    private String name;
    private InetAddress ipAddress;
    private IZZYStatus status;

    public IZZY(UUID uuid) {
        this.uuid = uuid;
        this.name = null;
        this.ipAddress = null;
        this.status = MISSING;
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

    public UUID getUUID() {
        return uuid;
    }

    public IZZYStatus getStatus() {
        return status;
    }

    public void setStatus(IZZYStatus status) {
        this.status = status;
    }
}
