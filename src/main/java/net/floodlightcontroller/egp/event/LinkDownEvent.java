package net.floodlightcontroller.egp.event;

/**
 * Created by wangxuan on 15/5/11.
 */
public class LinkDownEvent extends ControllerEvent{

    public LinkDownEvent() {
        this.setType(ControllerEvent.LINKDOWN);
        this.setInfo("LINKDOWN");
    }

}
