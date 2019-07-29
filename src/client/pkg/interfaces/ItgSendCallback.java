package client.pkg.interfaces;

public interface ItgSendCallback {
    void onSended();

    void onError(String msg);
}
