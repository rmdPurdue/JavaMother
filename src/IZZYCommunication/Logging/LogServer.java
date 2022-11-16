package IZZYCommunication.Logging;

import lineFollow.LineFollowController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class LogServer extends Thread {

    private LineFollowController controller;
    private ServerSocket ss;
    private LinkedList<LogServerThread> threads;

    public void setLineFollowController(LineFollowController controller) {
        this.controller = controller;
    }

    @Override
    public void run() {
        try {
            ss = new ServerSocket(3125);
            threads = new LinkedList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        while (true) {
            Socket socket;
            try {
                socket = ss.accept();
                if (!threads.isEmpty()) {
                    threads.getFirst().terminate();
                }
                LogServerThread logServerThread = new LogServerThread(socket, controller);
                threads.add(logServerThread);
                logServerThread.start();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }


        }

    }
}
