package ru.gb.chat.client;

import com.sun.scenario.effect.impl.sw.java.JSWBlend_SRC_OUTPeer;
import ru.gb.chat.library.Library;
import ru.gb.jtwo.network.SocketThread;
import ru.gb.jtwo.network.SocketThreadListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;

public class ClientGUI extends JFrame implements ActionListener, Thread.UncaughtExceptionHandler, SocketThreadListener {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 300;

    private final JTextArea log = new JTextArea();
    private final JPanel panelTop = new JPanel(new GridLayout(2, 3));
    private final JTextField tfIPAddress = new JTextField("127.0.0.1");
    private final JTextField tfPort = new JTextField("8189");
    private final JCheckBox cbAlwaysOnTop = new JCheckBox("Always on top");
    private final JTextField tfLogin = new JTextField("ivan");
    private final JPasswordField tfPassword = new JPasswordField("1234");
    private final JButton btnLogin = new JButton("Login");

    //private final JPanel panelBottom = new JPanel(new BorderLayout());
    private final JPanel panelBottom = new JPanel(new GridLayout(2, 3));
    private final JButton btnDisconnect = new JButton("<html><b>Disconnect</b></html>");
    private final JTextField tfMessage = new JTextField();
    private final JButton btnSend = new JButton("Send");

    private final JTextField nickNameMessage = new JTextField();
    private final JButton btnNickName= new JButton("Edit nickName");

    private final JList<String> userList = new JList<>();
    private boolean shownIoErrors = false;
    private final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss: ");

    private SocketThread socketThread;
    private static final String WINDOW_TITLE = "Chat";
    private String fileName = "";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientGUI();
            }
        });
    }

    private ClientGUI() {
        Thread.setDefaultUncaughtExceptionHandler(this);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(WIDTH, HEIGHT);
        setTitle(WINDOW_TITLE);
        log.setEditable(false);
        JScrollPane scrollLog = new JScrollPane(log);
        JScrollPane scrollUser = new JScrollPane(userList);
        scrollUser.setPreferredSize(new Dimension(100, 0));
        cbAlwaysOnTop.addActionListener(this);
        btnSend.addActionListener(this);
        tfMessage.addActionListener(this);
        btnLogin.addActionListener(this);
        btnDisconnect.addActionListener(this);
        nickNameMessage.addActionListener(this);
        btnNickName.addActionListener(this);

        panelTop.add(tfIPAddress);
        panelTop.add(tfPort);
        panelTop.add(cbAlwaysOnTop);
        panelTop.add(tfLogin);
        panelTop.add(tfPassword);
        panelTop.add(btnLogin);

        panelBottom.add(btnDisconnect);//, BorderLayout.WEST);
        panelBottom.add(tfMessage);//, BorderLayout.CENTER);
        panelBottom.add(btnSend);//, BorderLayout.EAST);
        panelBottom.add(nickNameMessage);//, BorderLayout.SOUTH);
        panelBottom.add(btnNickName);//, BorderLayout.EAST);
        panelBottom.setVisible(false);

        add(scrollLog, BorderLayout.CENTER);
        add(scrollUser, BorderLayout.EAST);
        add(panelTop, BorderLayout.NORTH);
        add(panelBottom, BorderLayout.SOUTH);

        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == cbAlwaysOnTop) {
            setAlwaysOnTop(cbAlwaysOnTop.isSelected());
        } else if (src == btnSend || src == tfMessage) {
            sendMessage();
        } else if (src == btnLogin) {
            connect();
        } else if (src == btnDisconnect) {
            socketThread.close();
        } else if (src == btnNickName || src == nickNameMessage) {
            sendNickName();
        } else {
            throw new RuntimeException("Unknown source: " + src);
        }
    }

    private void connect() {
        try {
            Socket socket = new Socket(tfIPAddress.getText(), Integer.parseInt(tfPort.getText()));
            socketThread = new SocketThread("Client", this, socket);
        } catch (IOException exception) {
            showException(Thread.currentThread(), exception);
        }
    }

    private void sendMessage() {
        String msg = tfMessage.getText();
        if ("".equals(msg)) return;
        tfMessage.setText(null);
        tfMessage.grabFocus();
        socketThread.sendMessage(Library.getTypeBcastClient(msg));
    }

    private void sendNickName() {
        String msgNick = nickNameMessage.getText();
        if ("".equals(msgNick)) return;
        nickNameMessage.setText(null);
        socketThread.sendMessageNick(Library.getEditNickClient(msgNick));
    }

    private void wrtMsgToLogFile(String username, String msg) {
        System.out.println("wrtMsgToLogFile");
        try (BufferedWriter out = new BufferedWriter(new FileWriter("log_" + username+ ".txt", true))) {
            fileName = "log_" + username+ ".txt";
            System.out.println(fileName );
            out.write(username + ": " + msg + "\n");
            out.flush();
        } catch (IOException e) {
            if (!shownIoErrors) {
                shownIoErrors = true;
                showException(Thread.currentThread(), e);
            }
        }
    }

    private void  wrtMsgFromLogFile() {
        if (new File(fileName).exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String str;
                int count = 0;
                while ((str = reader.readLine()) != null && count <= 100) {
                    putLog(str);
                    count++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void putLog(String msg) {
        if ("".equals(msg)) return;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append(msg + "\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }

    private void showException(Thread t, Throwable e) {
        String msg;
        StackTraceElement[] ste = e.getStackTrace();
        if (ste.length == 0)
            msg = "Empty Stacktrace";
        else {
            msg = String.format("Exception in \"%s\" %s: %s\n\tat %s",
                    t.getName(), e.getClass().getCanonicalName(), e.getMessage(), ste[0]);
        }
        JOptionPane.showMessageDialog(null, msg, "Exception", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        e.printStackTrace();
        showException(t, e);
        System.exit(1);
    }

    /**
     * Socket Threead Listener Methods
     * */

    @Override
    public void onSocketStart(SocketThread thread, Socket socket) {
        putLog("Start");
        wrtMsgFromLogFile();
    }

    @Override
    public void onSocketStop(SocketThread thread) {
        panelBottom.setVisible(false);
        panelTop.setVisible(true);
        setTitle(WINDOW_TITLE);
        userList.setListData(new String[0]);

    }

    @Override
    public void onSocketReady(SocketThread thread, Socket socket) {
        putLog("Ready");
        panelBottom.setVisible(true);
        panelTop.setVisible(false);
        String login = tfLogin.getText();
        String password = new String(tfPassword.getPassword());
        thread.sendMessage(Library.getAuthRequest(login, password));
    }

    @Override
    public void onReceiveString(SocketThread thread, Socket socket, String msg) {
        System.out.println("onReceiveString");
        System.out.println(msg);
        handleMessage(msg);
    }

    void handleMessage(String msg) {
        System.out.println("handleMessage");
        System.out.println(msg);
        String[] arr = msg.split(Library.DELIMITER);
        String msgType = arr[0];
        switch (msgType) {
            case Library.AUTH_ACCEPT:
                setTitle(WINDOW_TITLE + ": " + arr[1]);
                break;
            case Library.AUTH_DENIED:
                putLog("Wrong login/password");
                break;
            case Library.EDIT_NICK:
                putLog("Edit nick");
                String users = msg.substring(Library.USER_LIST.length() + Library.DELIMITER.length());
                String[] usersArr = users.split(Library.DELIMITER);
                Arrays.sort(usersArr);
                userList.setListData(usersArr);
                break;
            case Library.MSG_FORMAT_ERROR:
                putLog(msg);
                socketThread.close();
                break;
            case Library.TYPE_BROADCAST:
                putLog(DATE_FORMAT.format(Long.parseLong(arr[1])) + ": " + arr[2] + ": " + arr[3] + "\n");
                wrtMsgToLogFile(arr[2], arr[3]);
                break;
            case Library.USER_LIST:
                String usersGet = msg.substring(Library.USER_LIST.length() + Library.DELIMITER.length());
                String[] usersArrGet = usersGet.split(Library.DELIMITER);
                Arrays.sort(usersArrGet);
                userList.setListData(usersArrGet);
                break;
            default:
                throw new RuntimeException("Unknown message type: " + msg);
        }
    }

    @Override
    public void onSocketException(SocketThread thread, Throwable throwable) {
        //showException(thread, throwable);
        thread.close();
    }
}
