package net.floodlightcontroller.egp.controller;

import net.floodlightcontroller.egp.event.ControllerEvent;
import net.floodlightcontroller.egp.event.ControllerEventList;

import java.io.PrintWriter;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wangxuan on 15/5/1.
 */
public class SendMessageHandler implements Runnable{

    private static Logger logger = LoggerFactory.getLogger("egp.controller.SendMessageHandler");

    private Socket socket;
    private ControllerEventList sendEvent;

    public SendMessageHandler(Socket socket, ControllerEventList sendEvent) {
        this.socket = socket;
        this.sendEvent = sendEvent;
    }

    public void run() {
        while (true) {
            ControllerEvent event = sendEvent.popEvent();
            if (event == null) {
                try {
                    Thread.sleep(Constant.SEND_TIME_INTERVAL);
                }  catch (Exception e) {
                    logger.error(e.toString());
                    return ;
                }
                continue;
            }
            try {
                PrintWriter os = new PrintWriter(socket.getOutputStream());
                os.println(event.getInfo());
                logger.debug("To:" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + "  Message:" + event.getInfo());
                os.flush();
            }  catch (Exception e) {
                logger.error(e.toString());
            }
        }
    }

}
