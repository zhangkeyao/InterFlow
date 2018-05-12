package net.floodlightcontroller.egp.controller;

public class ConnectState {

    private boolean sendState;
    private boolean receiveState;

    ConnectState() {
        sendState = false;
        receiveState = false;
    }

    public boolean isSendState() {
        return sendState;
    }

    public boolean isReceiveState() {
        return receiveState;
    }

    public void setSendState(boolean sendState) {
        this.sendState = sendState;
    }

    public void setReceiveState(boolean receiveState) {
        this.receiveState = receiveState;
    }
}
