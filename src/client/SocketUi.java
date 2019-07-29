package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SocketUi {

    private JFrame jFrame;

    private UiSendListener uiSendListener;
    public JTextArea mSendText;
    public JTextArea mReceiverText;
    public Label mSendState;
    public JButton mSendBtn, mCancelBtn, mConnectBtn, mInterrucpBtn;
    private JLabel mDialogLabel;

    public SocketUi() {
        jFrame = new JFrame("socketUi");
        jFrame.setSize(500, 400);
        jFrame.setLocation(200, 300);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        create1();
        jFrame.setVisible(true);
    }


    private void create1() {
        JPanel jp1 = new JPanel();    //创建面板
        mSendText = new JTextArea();    //创建多行文本框
        mSendText.setLineWrap(true);    //设置多行文本框自动换行
        JScrollPane jspane1 = new JScrollPane(mSendText);    //创建滚动窗格

        mReceiverText = new JTextArea();
        mReceiverText.setLineWrap(true);
        mReceiverText.setEditable(false);
        JScrollPane jspane2 = new JScrollPane(mReceiverText);


        JSplitPane jsp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, jspane1, jspane2); //创建拆分窗格
        jsp.setDividerLocation(200);    //设置拆分窗格分频器初始位置
        jsp.setDividerSize(1);            //设置分频器大小

        //下部组件
        JPanel jp2 = new JPanel();
        mSendState = new Label("success");

        mConnectBtn = new JButton("连接");
        mInterrucpBtn = new JButton("断开");
        mInterrucpBtn.setEnabled(false);
        mCancelBtn = new JButton("返回");        //创建按钮

        mSendBtn = new JButton("发送");
        mSendBtn.setEnabled(false);


        //设置布局管理
        jp1.setLayout(new BorderLayout());    //设置面板布局
        jp2.setLayout(new FlowLayout(FlowLayout.RIGHT));
        //添加组件
        jp1.add(jsp);

        jp2.add(mSendState);
        jp2.add(mConnectBtn);
        jp2.add(mInterrucpBtn);
        jp2.add(mCancelBtn);
        jp2.add(mSendBtn);

        jFrame.add(jp1, BorderLayout.CENTER);
        jFrame.add(jp2, BorderLayout.SOUTH);

        mDialogLabel = new JLabel();

        mSendBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mSendText.getText() == null || mSendText.getText().length() == 0) {
                    showDialog("发送消息不能为空");

                } else {
                    if (uiSendListener != null) {
                        uiSendListener.sendMessage(mSendText.getText());
                    }
                }
            }
        });

        mConnectBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (uiSendListener != null) {
                    uiSendListener.connect();
                }
            }
        });

    }

    public void setUiSendListener(UiSendListener uiSendListener) {
        this.uiSendListener = uiSendListener;

    }

    public void showDialog(String msg) {
        mDialogLabel.setText(msg);
        JOptionPane.showMessageDialog(
                mDialogLabel,
                msg,
                "socket",
                JOptionPane.INFORMATION_MESSAGE
        );
    }


    public interface UiSendListener {
        void sendMessage(String msg);

        void connect();
    }


}
