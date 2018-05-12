package net.floodlightcontroller.egp.routing;

import java.util.Iterator;
import java.util.PriorityQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wangxuan on 15/5/10.
 */
public class RoutingPriorityQueue {

    private static Logger logger = LoggerFactory.getLogger("egp.routing.RoutingPriorityQueue");


    PriorityQueue<RoutingTableEntry> queue = new PriorityQueue<RoutingTableEntry>(50, new RoutingEntryComparator());


    // Attention!!



    public synchronized boolean update(RoutingTableEntry entry) {
        this.remove(entry.getNextHop());
        if (entry.isEmpty()) return false;
        queue.add(entry);
        return false;
    }

    public synchronized boolean remove(HopSwitch hopSwitch) {
        Iterator<RoutingTableEntry> iterator = queue.iterator();
        if (queue.isEmpty()) return false;
        boolean ret = false;
        while (iterator.hasNext()) {
            RoutingTableEntry e = iterator.next();
            if (e.getNextHop().equals(hopSwitch)) {

                RoutingTableEntry e2 = this.getTop();
                if (e.getNextHop().equals(e2.getNextHop())) ret = true;

                queue.remove(e);
                break;
            }
        }
        return ret;
    }

    public synchronized RoutingTableEntry getTop() { // return null if queue is empty
        if (queue.isEmpty()) return null;
        RoutingTableEntry entry = queue.poll();
        //logger.error("GetTop: " + entry);
        if (entry != null) {
            queue.add(entry);
        }
        return entry;
    }

    public void printAll() {
        System.out.println("size:" + queue.size());
        Iterator<RoutingTableEntry> iterator = queue.iterator();
        while (iterator.hasNext()) {
            RoutingTableEntry e = iterator.next();
            System.out.println(e.toString());
        }
    }

}
