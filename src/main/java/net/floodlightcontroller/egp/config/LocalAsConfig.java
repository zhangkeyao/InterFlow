package net.floodlightcontroller.egp.config;

import net.floodlightcontroller.egp.routing.HopSwitch;
import net.floodlightcontroller.egp.routing.RoutingIndex;

import java.util.ArrayList;
import java.util.List;


public class LocalAsConfig {

    private String dstIp;
    private String srcIp;
    private String protocol;
    private String srcPort;
    private String dstPort;

    private List<LocalAsPortConfig> outPort = new ArrayList<LocalAsPortConfig>();

    public String getDstIp() {
        return dstIp;
    }

    public List<LocalAsPortConfig> getOutPort() {
        return outPort;
    }

    public String getSrcIp() {
        return srcIp;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getSrcPort() {
        return srcPort;
    }

    public String getDstPort() {
        return dstPort;
    }

    public void setDstIp(String dstIp) {
        this.dstIp = dstIp;
    }

    public void setOutPort(List<LocalAsPortConfig> outPort) {
        this.outPort = outPort;
    }

    public void setSrcIp(String srcIp) {
        this.srcIp = srcIp;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setSrcPort(String srcPort) {
        this.srcPort = srcPort;
    }

    public void setDstPort(String dstPort) {
        this.dstPort = dstPort;
    }

    public boolean equalIndex(RoutingIndex index) {
        return new RoutingIndex(this).equals(index);
    }

    public void print() {
        System.out.println("---dstIp:" + dstIp);
        System.out.println("---srcIp:" + srcIp);
        System.out.println("---dstPort:" + dstPort);
        System.out.println("---srcPort:" + srcPort);
        System.out.println("---protocol:" + protocol);
        for (LocalAsPortConfig config:outPort) {
            System.out.println("------" + config.getSwitchId() + "," + config.getSwitchPort());
        }

    }

}
