package client.pkg.interfaces;

public interface ItgConnectCallback {
    void onConnect();

    void onError(String msg);
}
