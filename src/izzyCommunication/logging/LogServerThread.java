package izzyCommunication.logging;

import lineFollow.LineFollowController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicBoolean;

public class LogServerThread extends Thread {

    private Socket s;
    private LineFollowController controller;
    private AtomicBoolean stop;

    public LogServerThread(Socket s, LineFollowController controller) {
        this.s = s;
        this.controller = controller;
        stop = new AtomicBoolean(false);
    }

    @Override
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            while (!stop.get()) {
                try {
                    String message = br.readLine();
                    if (message != null) {
                        controller.appendLogMessage(message);
                    } else {
                        Thread.sleep(100);
                    }
                } catch (SocketException e) {
                    if (!stop.get()) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void terminate() {
        stop.set(true);
        if (s != null && !s.isClosed()) {
            try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
