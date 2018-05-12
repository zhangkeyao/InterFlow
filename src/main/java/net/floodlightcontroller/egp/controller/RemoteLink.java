package net.floodlightcontroller.egp.controller;

import net.floodlightcontroller.egp.config.RemoteControllerLinkConfig;
import net.floodlightcontroller.egp.routing.HopSwitch;

/**
 * Created by wangxuan on 15-4-30.
 */
public class RemoteLink {

    private String localSwitchId;
    private String localSwitchPort;
    private String remoteSwitchId;
    private String remoteSwitchPort;
    private LinkState state;

    public RemoteLink(RemoteControllerLinkConfig config) {
        this.localSwitchId = config.getLocalSwitchId();
        this.localSwitchPort = config.getLocalSwitchPort();
        this.remoteSwitchId = config.getRemoteSwitchId();
        this.remoteSwitchPort = config.getRemoteSwitchPort();
        this.state = new LinkState();
    }

    public String getLocalSwitchId() {
        return localSwitchId;
    }

    public HopSwitch getLocalSwitch() {
        return new HopSwitch(this.localSwitchId, this.localSwitchPort);
    }

    public HopSwitch getRemoteSwitch() {
        return new HopSwitch(this.remoteSwitchId, this.remoteSwitchPort);
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

    public LinkState getState() {
        return state;
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

    public void setState(LinkState state) {
        this.state = state;
    }
}
