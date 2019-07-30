package client;

import client.pkg.ItgConnect;
import client.pkg.interfaces.ItgConnectCallback;
import client.pkg.interfaces.ItgReceiveCallback;
import client.pkg.interfaces.ItgSendCallback;

public class ClientMain {

    public static void main(String[] args) {
        ItgConnect itgConnect = new ItgConnect("127.0.0.1", 15543);
        SocketUi socketUi = new SocketUi();
        itgConnect.setItgConnectCallback(new ItgConnectCallback() {
            @Override
            public void onConnect() {
                socketUi.mConnectBtn.setText("已连接");
                socketUi.mInterrucpBtn.setEnabled(true);
                socketUi.mSendBtn.setEnabled(true);
                itgConnect.receiveMsg();
            }

            @Override
            public void onError(String msg) {
                socketUi.mConnectBtn.setText("连接失败");
                socketUi.mInterrucpBtn.setEnabled(false);
                socketUi.mSendBtn.setEnabled(false);
            }
        });

        itgConnect.addItgSendCallback(new ItgSendCallback() {
            @Override
            public void onSended() {

                socketUi.mSendState.setText("send success");
            }

            @Override
            public void onError(String msg) {
                socketUi.mSendState.setText("send fail");
            }
        });

        itgConnect.addItgReceiveCallback(new ItgReceiveCallback() {
            @Override
            public void onReceive(String msg) {
                socketUi.mReceiverText.setText(msg);
            }

            @Override
            public void onError(String msg) {
                socketUi.mReceiverText.setText(msg);
                socketUi.mConnectBtn.setText("连接");
                socketUi.mInterrucpBtn.setEnabled(false);
                socketUi.mSendBtn.setEnabled(false);
            }
        });
        socketUi.setUiSendListener(new SocketUi.UiSendListener() {
            @Override
            public void sendMessage(String msg) {
                socketUi.mSendState.setText("send...");
                itgConnect.sendMsg(msg);
            }

            @Override
            public void connect() {
                if (!itgConnect.isConnected()) {
                    socketUi.mConnectBtn.setText("连接...");
                    itgConnect.connect();
                } else {
                    socketUi.showDialog("socket已经连接");
                }
            }

            @Override
            public void interrup() {
                itgConnect.shutSocket();
                socketUi.mConnectBtn.setText("连接");
            }
        });
    }


}
