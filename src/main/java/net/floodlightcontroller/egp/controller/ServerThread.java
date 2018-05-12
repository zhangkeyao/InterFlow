package net.floodlightcontroller.egp.controller;

import java.lang.Runnable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerThread implements Runnable{

    private static Logger logger = LoggerFactory.getLogger("egp.controller.ServerThread");

    int localPort;
    List<RemoteController> listController;
    ServerSocket serverSocket;


    ServerThread(int localPort, List<RemoteController> listController) {
        this.localPort = localPort;
        this.listController = listController;
    }

    public void run() {
        logger.info("Server thread running...");
        try {
            serverSocket = new ServerSocket(localPort);
            Socket socket = null;
            while (true) {
                try {
                    socket = serverSocket.accept();
                    logger.info("Accept from " + socket.getInetAddress().getHostAddress());
                    new Thread(new OpenThread(socket, listController)).start();
                } catch (Exception e) {
                    logger.error(e.toString());
                }
            }

        }  catch (Exception e){
            logger.error(e.toString());
            return ;
        }
    }

}
