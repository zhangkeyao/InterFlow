package net.floodlightcontroller.egp.event;


public class OpenEvent extends ControllerEvent{
    public OpenEvent(String id) {
        this.setType(ControllerEvent.OPEN);
        this.setInfo(new String("OPEN " + id));
    }
}
