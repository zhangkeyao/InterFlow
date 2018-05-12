package net.floodlightcontroller.egp.event;

import java.util.ArrayList;
import java.util.List;



public class ControllerEventList {
    private List<ControllerEvent> list = null;

    public ControllerEventList() {
        list = new ArrayList<ControllerEvent>();
    }

    public List<ControllerEvent> getList() {
        return list;
    }

    public synchronized void addEvent(ControllerEvent event) {
        list.add(event);
    }

    public synchronized ControllerEvent popEvent() {
        if (list.isEmpty()) return null;
        ControllerEvent ret = list.get(0);
        list.remove(0);
        return ret;
    }

}
