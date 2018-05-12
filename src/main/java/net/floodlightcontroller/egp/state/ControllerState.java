package net.floodlightcontroller.egp.state;

public class ControllerState {

    public static final int IDLE = 1;
    public static final int OPENSENT = 2;
    public static final int OPENCONFIRM = 3;
    public static final int ESTABLISHED = 4;
    public static final int CONNECT = 5;

    private int state;

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public ControllerState() {
        state = IDLE;
    }

    public ControllerState(int state) {
        this.state = state;
    }

    public String toString() {
        if (state == IDLE) return "IDLE";
        if (state == OPENSENT) return "OPENSENT";
        if (state == OPENCONFIRM) return "OPENCONFIRM";
        if (state == ESTABLISHED) return "ESTABLISHED";
        if (state == CONNECT) return "CONNECT";
        return "UNKNOWN";
    }

}
