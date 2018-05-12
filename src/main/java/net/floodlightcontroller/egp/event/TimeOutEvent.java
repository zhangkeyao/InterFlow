package net.floodlightcontroller.egp.event;

/**
 * Created by wangxuan on 15-4-30.
 */
public class TimeOutEvent extends ControllerEvent {

    public TimeOutEvent() {
        this.setType(ControllerEvent.TIMEOUT);
        this.setInfo("TIMEOUT");
    }

}
