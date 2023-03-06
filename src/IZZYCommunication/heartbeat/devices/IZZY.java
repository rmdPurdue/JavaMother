package izzyCommunication.heartbeat.devices;

import izzyCommunication.heartbeat.IZZYStatus;

import java.net.InetAddress;
import java.util.UUID;

import static izzyCommunication.heartbeat.IZZYStatus.*;

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
    private long lastContact;

    public IZZY(UUID uuid, InetAddress ipAddress) {
        this.uuid = uuid;
        this.name = null;
        this.ipAddress = ipAddress;
        this.status = MISSING;
        this.lastContact = 0;
    }

    public UUID getUUID() {
        return uuid;
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

    public IZZYStatus getStatus() {
        return status;
    }

    public void setStatus(IZZYStatus status) {
        this.status = status;
    }

    public long getLastContact() {
        return lastContact;
    }

    public void setLastContact(long lastContact) {
        this.lastContact = lastContact;
    }

    @Override
    public boolean equals(Object izzy) {
        if (izzy instanceof IZZY) {
            return ((IZZY) izzy).getUUID().equals(this.getUUID());
        }
        return false;
    }
}
