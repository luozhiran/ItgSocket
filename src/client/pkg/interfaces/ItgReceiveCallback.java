package client.pkg.interfaces;

public interface ItgReceiveCallback {
    void onReceive(String msg);

    void onError(String msg);
}
