package net.floodlightcontroller.egp.event;


public class KeepAliveEvent extends ControllerEvent {
    public KeepAliveEvent() {
        this.setType(ControllerEvent.KEEPALIVE);
        this.setInfo(new String("KEEPALIVE"));
    }
}
