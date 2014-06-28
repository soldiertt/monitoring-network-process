package be.smals.monitor.client;

import be.smals.monitor.model.ProcessMonitor;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * My Client
 *
 * Created by soldiertt on 19-06-14.
 */
public class TCPClient {

    public static void main(String argv[]) throws Exception  {

        final String server;
        if (argv.length != 1) {
            throw new IllegalArgumentException("Must be one argument with server name !");
        } else {
            server = argv[0];
        }
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Socket clientSocket;
                final String processName = "notepad++.exe";
                final String dosCommand = "cmd /c tasklist /FI \"IMAGENAME eq " + processName + "\" /FO CSV /NH";
                ProcessMonitor processMonitor = new ProcessMonitor("UNKNOWN", processName);

                try {
                    processMonitor.setHostname(InetAddress.getLocalHost().getHostName());
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }

                try {
                    clientSocket = new Socket(server, 6789);
                    ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
                    final Process process = Runtime.getRuntime().exec(dosCommand);
                    final InputStream in = process.getInputStream();
                    String cmdResult = convertStreamToString(in);
                    if (cmdResult.contains(processName)) {
                        processMonitor.setRunning(true);
                    }
                    outToServer.writeObject(processMonitor);
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };
        timer.schedule(task,0,20000);
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
