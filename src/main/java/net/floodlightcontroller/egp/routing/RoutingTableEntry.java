package net.floodlightcontroller.egp.routing;

import java.util.ArrayList;
import java.util.List;


public class RoutingTableEntry {

    private RoutingIndex index;
    private HopSwitch nextHop;
    private List<String> path;
    private Integer timestamp;

    public RoutingTableEntry(RoutingIndex index, HopSwitch nextHop, List<String> path, Integer timestamp) {
        this.index = index;
        this.nextHop = nextHop;
        this.path = path;
        this.timestamp = timestamp;
    }

    public RoutingTableEntry(RoutingIndex index, HopSwitch nextHop, Integer timestamp) {
        this.timestamp = timestamp;
        this.nextHop = nextHop;
        this.index = index;
        this.path = null;
    }

    public boolean isEmpty() {
        return path == null || path.isEmpty();
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("");
        builder.append("nexthop: ");
        builder.append(this.nextHop.getSwitchId());
        builder.append("   path: ");
        boolean flagFirst = true;
        for (String s:this.getPath()) {
            if (!flagFirst) builder.append(",");
            flagFirst = false;
            builder.append(s);
        }
        return builder.toString();
    }

    public RoutingIndex getIndex() {
        return index;
    }

    public HopSwitch getNextHop() {
        return nextHop;
    }

    public void setIndex(RoutingIndex index) {
        this.index = index;
    }

    public void setNextHop(HopSwitch nextHop) {
        this.nextHop = nextHop;
    }

    public List<String> getPath() {
        return path;
    }

    public void setPath(List<String> path) {
        this.path = path;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public RoutingTableEntry clone() {
        RoutingTableEntry ret = new RoutingTableEntry(this.index, this.nextHop, this.timestamp);
        ret.path = null;
        if (this.path != null) {
            ret.path = new ArrayList<String> ();
            for (String s : path) ret.path.add(s);
        }
        return ret;
    }
}
