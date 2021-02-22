package util;

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

    private UUID uuid = null;
    private InetAddress ipAddress = null;
    private ArrayList<IZZY> izzyUnits = new ArrayList<>();

    public Mother() {
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    private void setIpAddress(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void addIzzyUnit(IZZY izzy) {
        izzyUnits.add(izzy);
    }

    public void removeIzzyUnit(IZZY izzy) {
        izzyUnits.remove(izzy);
    }

    public ArrayList<IZZY> getIzzyUnits() {
        return izzyUnits;
    }

}
