package net.floodlightcontroller.egp.routing;

import net.floodlightcontroller.egp.config.LocalAsConfig;

/**
 * Created by wangxuan on 5/20/15.
 */
public class RoutingIndex {

    private String srcIp;
    private String dstIp;
    private String srcPort;
    private String dstPort;
    private String protocol;

    public RoutingIndex() {
    }

    public RoutingIndex(LocalAsConfig config) {
        this.srcIp = config.getSrcIp();
        this.dstIp = config.getDstIp();
        this.srcPort = config.getSrcPort();
        this.dstPort = config.getDstPort();
        this.protocol = config.getProtocol();
    }

    private int hash(String s) {
        int ret = 0;
        for (int i = 0; i < s.length(); i++) {
            ret = ret * 17 + (int)s.charAt(i);
        }
        return ret;
    }

    @Override
    public int hashCode() {
        int ret = 0;
        if (srcIp != null) ret += hash(srcIp) * 3;
        if (dstIp != null) ret += hash(dstIp) * 5;
        if (srcPort != null) ret += hash(srcPort) * 7;
        if (dstPort != null) ret += hash(dstPort) * 11;
        if (protocol != null) ret += hash(protocol) * 13;
        return ret;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RoutingIndex))
            return false;
        return this.hashCode() == obj.hashCode();

    }



    public String getSrcIp() {
        return srcIp;
    }

    public String getDstIp() {
        return dstIp;
    }

    public String getSrcPort() {
        return srcPort;
    }

    public String getDstPort() {
        return dstPort;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setSrcIp(String srcIp) {
        this.srcIp = srcIp;
    }

    public void setDstIp(String dstIp) {
        this.dstIp = dstIp;
    }

    public void setSrcPort(String srcPort) {
        this.srcPort = srcPort;
    }

    public void setDstPort(String dstPort) {
        this.dstPort = dstPort;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void print() {
        if (this.getSrcIp() != null) System.out.println("--- srcIp:" + this.getSrcIp());
        if (this.getSrcPort() != null) System.out.println("--- srcPort:" + this.getSrcPort());
        if (this.getDstIp() != null) System.out.println("--- dstIp:" + this.getDstIp());
        if (this.getDstPort() != null) System.out.println("--- dstPort:" + this.getDstPort());
        if (this.getProtocol() != null) System.out.println("--- protocol:" + this.getProtocol());
    }
}
