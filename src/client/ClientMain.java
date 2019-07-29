package client;

import client.pkg.ItgConnect;

public class ClientMain {

    public static void main(String[] args) {
        ItgConnect itgConnect = new ItgConnect("127.0.0.1", 15543);
        SocketUi socketUi = new SocketUi();
        socketUi.setUiSendListener(new SocketUi.UiSendListener() {
            @Override
            public void sendMessage(String msg) {
                itgConnect.sendMsg(msg);
            }

            @Override
            public void connect() {
                if (!itgConnect.isConnected()) {
                    itgConnect.connect();
                } else {
                    socketUi.showDialog("socket已经连接");
                }
            }
        });
    }


}
