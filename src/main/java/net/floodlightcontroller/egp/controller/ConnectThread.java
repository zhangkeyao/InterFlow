package net.floodlightcontroller.egp.controller;

import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectThread implements Runnable{

    private static Logger logger = LoggerFactory.getLogger("egp.controller.ConnectThread");

    RemoteController controller;

    ConnectThread(RemoteController controller) {
        this.controller = controller;
    }

    public void run() {
        Socket socket = null;
        logger.info("Connect thread running... Try to connect:" + controller.getIp());
        while (true) {
            boolean flag = true;
            try {
                socket = new Socket(controller.getIp(), controller.getPort());
            } catch (UnknownHostException e) {
                flag = false;
            } catch (NullPointerException e) {
                flag = false;
            } catch (ConnectException e) {
                flag = false;
            } catch (Exception e) {
                logger.error(e.toString());
                return ;
            }
            if (flag) break;
            try {
                Thread.sleep(Constant.CONNECT_TIME_INTERVAL);
            }  catch (Exception e) {
                logger.error(e.toString());
                return ;
            }
        }
        logger.info("Connected to " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
        controller.setSocket(socket);
    }
}
