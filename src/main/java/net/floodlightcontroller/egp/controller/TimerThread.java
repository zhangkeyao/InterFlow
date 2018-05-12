package net.floodlightcontroller.egp.controller;

import net.floodlightcontroller.egp.event.ControllerEvent;
import net.floodlightcontroller.egp.event.TimeOutEvent;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wangxuan on 15-4-30.
 */
public class TimerThread implements Runnable{

    private int timeSlotsRemain = 0;

    private int msTimePerSlot = 1000;

    private int timeSlotsMaximum = 10;

    private List<ControllerEvent> listEvent;

    private static Logger logger = LoggerFactory.getLogger("egp.controller.TimerThread");

    public TimerThread(List<ControllerEvent> listEvent) {
        this.listEvent = listEvent;
    }

    public TimerThread(int timeSlotsMaximum, List<ControllerEvent> listEvent) {
        this.timeSlotsMaximum = timeSlotsMaximum;
        this.timeSlotsRemain = timeSlotsMaximum;
        this.listEvent = listEvent;
    }

    public TimerThread(int timeSlotsMaximum, int msTimePerSlot, List<ControllerEvent> listEvent) {
        this.timeSlotsMaximum = timeSlotsMaximum;
        this.timeSlotsRemain = timeSlotsMaximum;
        this.msTimePerSlot = msTimePerSlot;
        this.listEvent = listEvent;
    }

    public synchronized void reset() {
        timeSlotsRemain = timeSlotsMaximum;
    }

    private synchronized void timeGo() {
        timeSlotsRemain --;
    }

    public void run() {
        while (timeSlotsRemain > 0) {
            try {
                Thread.sleep(msTimePerSlot);
            }  catch (Exception e) {
                e.printStackTrace();
                logger.error(e.toString());
            }
            timeGo();
        }
        listEvent.add(new TimeOutEvent());
    }



}
