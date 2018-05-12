package net.floodlightcontroller.egp.event;

/**
 * Created by wangxuan on 15/5/11.
 */
public class LinkUpEvent extends ControllerEvent{

    public LinkUpEvent() {
        this.setType(ControllerEvent.LINKUP);
        this.setInfo("LINKUP");
    }

}
