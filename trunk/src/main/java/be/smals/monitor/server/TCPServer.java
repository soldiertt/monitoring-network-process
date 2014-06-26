package be.smals.monitor.server;

import be.smals.monitor.model.ProcessMonitor;

import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
import java.util.Timer;

/**
 * Created by soldiertt on 19-06-14.
 */
public class TCPServer {

    private static ImageIcon imgGreen = new ImageIcon(TCPServer.class.getResource("green.png"));
    private static ImageIcon imgRed = new ImageIcon(TCPServer.class.getResource("red.png"));

    public static void main(String argv[]) throws Exception {
        final String[] clients = {"pc-win8", "pc-win7"};
        final Map<String, Calendar> schedules = new HashMap<String, Calendar>();
        String clientSentence;
        String capitalizedSentence;
        ServerSocket welcomeSocket = new ServerSocket(6789);
        final MonFrame f = new MonFrame(clients);
        f.setVisible(true);
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Calendar ago = Calendar.getInstance();
                ago.add(Calendar.SECOND, -30);
                for (Map.Entry<String, Calendar> entry : schedules.entrySet()) {
                    int index = Arrays.asList(clients).indexOf(entry.getKey());
                    if (entry.getValue().getTime().compareTo(ago.getTime()) < 0 ) {
                        f.getLabels()[index][1].setIcon(imgRed);
                    } else {
                        f.getLabels()[index][1].setIcon(imgGreen);
                    }
                }
            }
        };
        timer.schedule(task,0,20000);

        while(true) {
            Socket connectionSocket = welcomeSocket.accept();
            ObjectInputStream inFromClient = new ObjectInputStream(connectionSocket.getInputStream());
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            ProcessMonitor processMonitor = (ProcessMonitor) inFromClient.readObject();
            int index = Arrays.asList(clients).indexOf(processMonitor.getHostname());
            schedules.put(processMonitor.getHostname(), Calendar.getInstance());
            if (processMonitor.isRunning()) {
                System.out.println("BULLET VERTE pour " + processMonitor.getHostname());
                f.getLabels()[index][2].setIcon(imgGreen);
            } else {
                System.out.println("BULLET ROUGE pour " + processMonitor.getHostname());
                f.getLabels()[index][2].setIcon(imgRed);
            }
        }
    }

}
