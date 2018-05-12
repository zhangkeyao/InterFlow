package net.floodlightcontroller.egp.event;

import net.floodlightcontroller.egp.routing.HopSwitch;
import net.floodlightcontroller.egp.routing.RoutingIndex;

import java.util.List;

/**
 * Created by wangxuan on 15/5/11.
 */
public class UpdateInfo {

    private RoutingIndex index;
    private HopSwitch nextHop;
    private List<String> path;
    private Integer timestamp;

    public UpdateInfo() {
    }

    public UpdateInfo(RoutingIndex index, HopSwitch nextHop, List<String> path, Integer timestamp) {
        this.index = index;
        this.nextHop = nextHop;
        this.path = path;
        this.timestamp = timestamp;
    }

    public RoutingIndex getIndex() {
        return index;
    }

    public HopSwitch getNextHop() {
        return nextHop;
    }

    public List<String> getPath() {
        return path;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setIndex(RoutingIndex index) {
        this.index = index;
    }

    public void setNextHop(HopSwitch nextHop) {
        this.nextHop = nextHop;
    }

    public void setPath(List<String> path) {
        this.path = path;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }
}
