package net.floodlightcontroller.egp.routing;

/**
 * Created by wangxuan on 15/5/4.
 */
public class HopSwitch {

    private String switchId;
    private String switchPort;

    public HopSwitch() {
    }

    @Override
    public int hashCode() {
        int ret = 0;
        for (int i = 0; i < switchId.length(); i++)
            ret = ret * 11 + 13 * switchId.charAt(i);
        for (int i = 0; i < switchPort.length(); i++)
            ret = ret * 11 + 13 * switchPort.charAt(i);
        return ret;
    }

    public HopSwitch(String switchId, String switchPort) {
        this.switchId = switchId;
        this.switchPort = switchPort;
    }

    public boolean equals(HopSwitch hopSwitch) {
        if (this.switchId == null || hopSwitch.getSwitchId() == null) return false;
        return this.switchId.equals(hopSwitch.getSwitchId()) && this.switchPort.equals(hopSwitch.getSwitchPort());
    }

    public String getSwitchId() {
        return switchId;
    }

    public String getSwitchPort() {
        return switchPort;
    }

    public void setSwitchId(String switchId) {
        this.switchId = switchId;
    }

    public void setSwitchPort(String switchPort) {
        this.switchPort = switchPort;
    }
}
