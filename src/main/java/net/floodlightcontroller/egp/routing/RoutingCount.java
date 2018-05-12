package net.floodlightcontroller.egp.routing;

/**
 * Created by wangxuan on 15/5/10.
 */
public class RoutingCount {

    int count;

    public RoutingCount() {
        count = 0;
    }

    public synchronized int getCount() {
        return ++count;
    }

}
