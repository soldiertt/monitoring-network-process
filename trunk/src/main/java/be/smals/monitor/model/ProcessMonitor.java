package be.smals.monitor.model;

import java.io.Serializable;

/**
 * Created by soldiertt on 23-06-14.
 */
public class ProcessMonitor implements Serializable {

    private String hostname;

    private boolean running;

    private String name;

    public ProcessMonitor(String hostname, String name) {
        this.hostname = hostname;
        this.name = name;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
