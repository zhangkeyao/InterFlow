package net.floodlightcontroller.egp.controller;

import net.floodlightcontroller.egp.controller.ControllerMain;
import net.floodlightcontroller.egp.controller.RemoteController;
import net.floodlightcontroller.egp.event.LinkDownEvent;
import net.floodlightcontroller.egp.event.LinkUpEvent;
import net.floodlightcontroller.egp.event.UpdateEvent;
import net.floodlightcontroller.egp.event.UpdateInfo;
import net.floodlightcontroller.egp.routing.RoutingPriorityQueue;
import net.floodlightcontroller.egp.routing.RoutingTableEntry;

import java.util.Map;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wangxuan on 15-5-1.
 */
public class CommandLineInterfaceThread implements Runnable {

    private static Logger logger = LoggerFactory.getLogger("egp.controller.CommandLineInterfaceThread");

    ControllerMain main;

    public CommandLineInterfaceThread(ControllerMain main) {
        this.main = main;
    }

    private void seeLinks() {
        System.out.println("------------------------------------------------------------");
        for (RemoteController controller:main.getControllerList()) {
            System.out.println(controller.getIp() + ":" + controller.getPort() + "   " + controller.getStateMachine().getControllerState().toString());
        }
        System.out.println("------------------------------------------------------------");
    }

    private void seeTables() {

        System.out.println("------------------------------------------------------------");
        main.getTable().printTable();
        System.out.println("------------------------------------------------------------");
    }

    private void linkUpDown(String line) {
        String sArray[] = line.split(" ");
        for (RemoteController controller:main.getControllerList()) {
            if (controller.getId().equals(sArray[1])) {
                if (sArray[0].equals("linkup"))
                    controller.getReceiveEvent().addEvent(new LinkUpEvent());
                else
                    controller.getReceiveEvent().addEvent(new LinkDownEvent());
            }
        }
    }


    public void run() {
        logger.info("Command line interface start...");
        System.out.println("CLI Start...  Local Port: " + main.getLocalPort());
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String line = scanner.nextLine();
            line = line.toLowerCase();
            String sarray[] = line.split(" ");
            if (line.equals("exit"))
                break;
            if (line.equals("links"))
                seeLinks();
            if (line.equals("table"))
                seeTables();
            if (sarray[0].equals("linkup") || sarray[0].equals("linkdown"))
                linkUpDown(line);

        }
        logger.info("Command line interface stop...");
    }
}
