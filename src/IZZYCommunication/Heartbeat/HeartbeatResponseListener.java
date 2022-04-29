package IZZYCommunication.Heartbeat;

public interface HeartbeatResponseListener {
    void onRemoteDeviceResponseReceived(IZZYStatus izzyStatus);
    void onRemoteDeviceTimeout();
    void onError();
    void onMotherDeviceTimeout();
}
