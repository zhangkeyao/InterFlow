package net.floodlightcontroller.egp.config;

import net.floodlightcontroller.egp.routing.HopSwitch;

public class LocalAsPortConfig {
    private String switchId;
    private String switchPort;

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

    public HopSwitch getHopSwitch() {
        return new HopSwitch(switchId, switchPort);
    }
}
