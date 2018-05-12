package net.floodlightcontroller.egp.config;

/**
 * Created by wangxuan on 15-4-30.
 */
public class RemoteControllerLinkConfig {
    private String localSwitchId;
    private String localSwitchPort;
    private String remoteSwitchId;
    private String remoteSwitchPort;

    public String getLocalSwitchId() {
        return localSwitchId;
    }

    public String getLocalSwitchPort() {
        return localSwitchPort;
    }

    public String getRemoteSwitchId() {
        return remoteSwitchId;
    }

    public String getRemoteSwitchPort() {
        return remoteSwitchPort;
    }

    public void setLocalSwitchId(String localSwitchId) {
        this.localSwitchId = localSwitchId;
    }

    public void setLocalSwitchPort(String localSwitchPort) {
        this.localSwitchPort = localSwitchPort;
    }

    public void setRemoteSwitchId(String remoteSwitchId) {
        this.remoteSwitchId = remoteSwitchId;
    }

    public void setRemoteSwitchPort(String remoteSwitchPort) {
        this.remoteSwitchPort = remoteSwitchPort;
    }
}
