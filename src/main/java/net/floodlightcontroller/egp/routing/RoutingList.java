package net.floodlightcontroller.egp.routing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RoutingList {

    Map<RoutingIndex, RoutingTableEntry> map = new HashMap<RoutingIndex, RoutingTableEntry>();

    public synchronized boolean add(RoutingTableEntry entry) {
        RoutingTableEntry tmp = map.get(entry.getIndex());
        if (tmp == null) {
            map.put(entry.getIndex(), entry);
            return true;
        }  else {
            if (tmp.isEmpty() || tmp.getTimestamp() < entry.getTimestamp()) {
                map.put(entry.getIndex(), entry);
                return true;
            }  else {
                return false;
            }
        }
    }


}
