package net.floodlightcontroller.egp.controller;

import net.floodlightcontroller.egp.event.ControllerEventList;
import net.floodlightcontroller.egp.event.KeepAliveEvent;
import net.floodlightcontroller.egp.event.OpenEvent;
import net.floodlightcontroller.egp.event.UpdateEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wangxuan on 15/5/4.
 */
public class ReceiveMessageHandler implements Runnable{

    private static Logger logger = LoggerFactory.getLogger("egp.controller.SendMessageHandler");

    private Socket socket;
    private ControllerEventList receiveEvent;

    public ReceiveMessageHandler(Socket socket, ControllerEventList receiveEvent) {
        this.socket = socket;
        this.receiveEvent = receiveEvent;
    }

    public void parseEvent(String line) throws Exception{
        String sarray[] = line.split(" ");
        if (sarray.length == 0) {
            return ;
        }
        if (sarray[0].equals("OPEN")) {
            receiveEvent.addEvent(new OpenEvent(sarray[1])); // attention!!! not consistent
            return ;
        }
        if (sarray[0].equals("KEEPALIVE")) {
            receiveEvent.addEvent(new KeepAliveEvent());
            return ;
        }
        if (sarray[0].equals("UPDATE")) {
            receiveEvent.addEvent(new UpdateEvent(line)); // attention!!!
            return ;
        }
    }

    public void run() {
        try {
            BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                String line = is.readLine();
                logger.debug("Receive:" + line + "   from:" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
                parseEvent(line);
            }
        }  catch (Exception e) {
            logger.error(e.toString());
        }
    }
}
