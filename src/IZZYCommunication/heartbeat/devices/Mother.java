package izzyCommunication.heartbeat.devices;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.UUID;

/**
 * @author Rich Dionne
 * @project JavaMother
 * @package util
 * @date 12/6/2018
 */
public class Mother {

    private final UUID uuid;
    private InetAddress ipAddress;
    private final ArrayList<IZZY> izzyUnits;

    public Mother(UUID uuid) {
        this.uuid = uuid;
        ipAddress = null;
        izzyUnits = new ArrayList<>();
    }

    public UUID getUUID() {
        return uuid;
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void addIzzyUnit(IZZY izzy) {
        izzyUnits.add(izzy);
    }

    public void removeIzzyUnit(UUID uuid) {
        for (int i = 0; i < izzyUnits.size(); i++) {
            if (izzyUnits.get(i).getUUID().equals(uuid)) {
                izzyUnits.remove(i);
                i--;
            }
        }
    }

    public IZZY getIzzy(UUID uuid) {
        for (IZZY izzyUnit : izzyUnits) {
            if (izzyUnit.getUUID().equals(uuid)) {
                return izzyUnit;
            }
        }
        return null;
    }

    public ArrayList<IZZY> getIzzyUnits() {
        return izzyUnits;
    }

}
