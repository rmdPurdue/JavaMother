package IZZYCommunication;

import Devices.IZZY;

public interface HeartbeatResponseListener {
    void onRemoteDeviceResponseReceived(IZZY izzy);
    void onRemoteDeviceTimeout();
}
