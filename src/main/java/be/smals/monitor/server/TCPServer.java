package be.smals.monitor.server;

import be.smals.monitor.model.ProcessMonitor;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.Timer;

/**
 * Created by soldiertt on 19-06-14.
 */
public class TCPServer {

    public static ImageIcon imgGreen = new ImageIcon(TCPServer.class.getResource("green.png"));
    public static ImageIcon imgRed = new ImageIcon(TCPServer.class.getResource("red.png"));

    public TCPServer() {
        //Empty constructor
    }

    public static void main(String argv[]) throws Exception {
        final TCPServer tcpServer = new TCPServer();
        File jarFile = new File(tcpServer.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        String propertiesPath = jarFile.getParentFile().getAbsolutePath();
        Properties mainProperties = new Properties();
        FileInputStream propsFile;
        propsFile = new FileInputStream(propertiesPath + "/server.properties");
        mainProperties.load(propsFile);
        propsFile.close();
        String clientsStr = mainProperties.getProperty("clients");
        final String[] clients = clientsStr.split(",");
        final Map<String, Calendar> lastIncomingRequests = new HashMap<String, Calendar>();
        ServerSocket welcomeSocket = new ServerSocket(6789);
        final MonFrame guiFrame = new MonFrame(clients);
        guiFrame.setVisible(true);
        Timer timer = new Timer();

        //Check connected agents
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Calendar ago = Calendar.getInstance();
                ago.add(Calendar.SECOND, -30);
                for (Map.Entry<String, Calendar> entry : lastIncomingRequests.entrySet()) {
                    int index = tcpServer.findHostIndex(clients, entry.getKey());
                    if (entry.getValue().getTime().compareTo(ago.getTime()) < 0) {
                        guiFrame.getIcons()[index][0].setIcon(imgRed);
                    } else {
                        guiFrame.getIcons()[index][0].setIcon(imgGreen);
                    }
                }
            }
        };
        timer.schedule(task,0,20000);

        while (true) {
            Socket connectionSocket = welcomeSocket.accept();
            ObjectInputStream inFromClient = new ObjectInputStream(connectionSocket.getInputStream());
            ProcessMonitor processMonitor = (ProcessMonitor) inFromClient.readObject();
            System.out.println("client hostname = " + processMonitor.getHostname());
            int index = tcpServer.findHostIndex(clients, processMonitor.getHostname());
            if (index != -1) {
                lastIncomingRequests.put(processMonitor.getHostname(), Calendar.getInstance());
                if (processMonitor.isRunning()) {
                    System.out.println("GREEN for " + processMonitor.getHostname());
                    guiFrame.getIcons()[index][1].setIcon(imgGreen);
                } else {
                    System.out.println("RED for " + processMonitor.getHostname());
                    guiFrame.getIcons()[index][1].setIcon(imgRed);
                    try {
                        InputStream in = new FileInputStream(new File(TCPServer.class.getResource("alarm.au").getPath()));
                        AudioStream audioStream = new AudioStream(in);
                        AudioPlayer.player.start(audioStream);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
                System.out.println("Connected client host '" +  processMonitor.getHostname() + "' is not registered in server.");
            }
        }
    }

    private int findHostIndex(String[] clients, String hostname) {
        for (int i = 0; i < clients.length; i++) {
            if (clients[i].toLowerCase().contains(hostname.toLowerCase())) {
                return i;
            }
        }
        return -1;
    }
}
