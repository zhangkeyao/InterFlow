package net.floodlightcontroller.egp.event;


public class ControllerEvent {
    public static final int UNDEFINED = 0;
    public static final int OPEN = 1;
    public static final int KEEPALIVE = 2;
    public static final int UPDATE = 3;
    public static final int NOTIFICATION = 4;
    public static final int TIMEOUT = 5;
    public static final int LINKUP = 6;
    public static final int LINKDOWN = 7;

    private int type;
    private String info;

    public ControllerEvent() {
        this.type = UNDEFINED;
    }

    public ControllerEvent(int type) {
        this.type = UNDEFINED;
        this.info = "";
    }

    public ControllerEvent(int type, String info) {
        this.type = UNDEFINED;
        this.info = info;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

}
