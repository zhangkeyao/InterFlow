package net.floodlightcontroller.egp.controller;

import net.floodlightcontroller.egp.event.OpenEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;

/**
 * Created by wangxuan on 15-5-1.
 */
public class OpenThread implements Runnable {

    private static Logger logger = LoggerFactory.getLogger("egp.controller.OpenThread");


    private List<RemoteController> listController;
    private Socket socket;

    public OpenThread(Socket socket, List<RemoteController> listController) {
        this.socket = socket;
        this.listController = listController;
    }

    private boolean parseOpen(String line) {
        String sArray[] = line.split(" ");
        if (sArray.length != 2) return false;
        if (!sArray[0].equals("OPEN")) return false;
        for (RemoteController controller : listController) {
            if (controller.getId().equals(sArray[1]) && controller.getCs().equals("s")) {
                controller.setSocket(socket);
                controller.getReceiveEvent().addEvent(new OpenEvent(sArray[1]));
                return true;
            }
        }
        return false;
    }

    public void run() {
        try {
            BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = is.readLine();
            logger.debug("Receive:" + line + "   from:" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
            if (!parseOpen(line)) {
                socket.close();
            }
        }  catch (Exception e) {
            logger.error(e.toString());
        }
    }
}
