package client.pkg;

import client.pkg.interfaces.ItgConnectCallback;
import client.pkg.interfaces.ItgReceiveCallback;
import client.pkg.interfaces.ItgSendCallback;

import java.io.*;
import java.net.*;

public class ItgConnect {
    private Socket mSocket;
    private String mIp;
    private int mPort;
    private static final int COMMECT_TIME_OUT = 10 * 1000;
    private static final int READ_TIME_OUT = 15 * 1000;
    private ItgConnectCallback mItgConnectCallback;
    private ItgSendCallback mItgSendCallback;
    private ItgReceiveCallback mItgReceiveCallback;

    public ItgConnect(String ip, int port) {
        mIp = ip;
        mPort = port;
        mSocket = new Socket();
    }

    public void connect() {
        InetSocketAddress isa = new InetSocketAddress(mIp, mPort);
        try {
            mSocket.connect(isa);
//            mSocket.setSoTimeout(READ_TIME_OUT);
            mSocket.setKeepAlive(true);
            if (mItgConnectCallback != null) {
                mItgConnectCallback.onConnect();
            }
        } catch (IOException e) {
            if (mItgConnectCallback != null) {
                mItgConnectCallback.onError(filterError(e));
            }

            shutSocket();
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg) {
        if (isConnected()) {
            try {
                OutputStream outputStream = mSocket.getOutputStream();
                Writer writer = new OutputStreamWriter(outputStream, "UTF-8");
                writer = new BufferedWriter(writer);
                writer.write(msg);
                writer.flush();
                mItgSendCallback.onSended();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mItgSendCallback.onError("没有连接到服务器");
        }
    }

    public void receiveMsg() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isConnected()) {
                    try {
                        InputStream in = mSocket.getInputStream();
                        byte[] bytes = new byte[1024];
                        int len = 0;
                        StringBuilder s = new StringBuilder();
                        while ((len = in.read(bytes)) != -1) {
                            s.append(new String(bytes, 0, len));
                            if (mItgReceiveCallback != null) {
                                mItgReceiveCallback.onReceive(s.toString());
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        if (mItgReceiveCallback != null) {
                            mItgReceiveCallback.onError(e.getMessage());
                        }
                        shutSocket();
                    }
                }
            }
        }).start();

    }


    public void addItgSendCallback(ItgSendCallback itgSendCallback) {
        mItgSendCallback = itgSendCallback;
    }

    public void addItgReceiveCallback(ItgReceiveCallback itgReceiveCallback) {
        mItgReceiveCallback = itgReceiveCallback;
    }

    public void setItgConnectCallback(ItgConnectCallback itgConnectCallback) {
        this.mItgConnectCallback = itgConnectCallback;
    }

    /**
     * isClosed():如果socket关闭 isClosed()会返回true，否则返回false。
     * 有一种特殊情况，假如socket根本没有打开过（socket从未连接），isClosed()也会返回false，
     * <p>
     * isConnected()：如果socket从来没有连接过远程主机，isConnected()会返回false，但是只要socket连接过远程主机，
     * 即使socket当前已经关闭，isConnected()也会返回true。
     * <p>
     * 因此，要检查socket当前是否打开，要检查两个条件
     *
     * @return
     */
    public boolean isConnected() {
        boolean connect = mSocket.isConnected() && !mSocket.isClosed();
        return connect;
    }

    public void shutSocket() {
        if (!mSocket.isClosed()) {
            try {
                mSocket.close();
                mSocket = null;
                mSocket = new Socket();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mSocket = null;
            mSocket = new Socket();
        }
    }

    public String filterError(IOException ioe) {
        if (ioe instanceof ConnectException) {
            return "服务器拒绝连接: " + ioe.getMessage();
        } else if (ioe instanceof NoRouteToHostException) {
            return "路由器无法识别服务器: " + ioe.getMessage();
        } else if (ioe instanceof SocketTimeoutException) {
            return "你连接的服务器挂起【连接超时】: " + ioe.getMessage();
        }
        return ioe.getMessage();
    }
}
