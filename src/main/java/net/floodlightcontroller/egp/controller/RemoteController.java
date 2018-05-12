package net.floodlightcontroller.egp.controller;

import net.floodlightcontroller.egp.config.LocalAsConfig;
import net.floodlightcontroller.egp.config.RemoteControllerConfig;
import net.floodlightcontroller.egp.config.RemoteControllerLinkConfig;
import net.floodlightcontroller.egp.event.ControllerEventList;
import net.floodlightcontroller.egp.routing.RoutingTable;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoteController {

    private static Logger logger = LoggerFactory.getLogger("egp.controller.RemoteController");


    private String ip;
    private String id;
    private String cs;
    private int port;
    private Socket socket;
    private ControllerEventList receiveEvent = new ControllerEventList();
    private ControllerEventList sendEvent = new ControllerEventList();
    private RoutingTable table;
    private List<RemoteLink> listLink = new ArrayList<RemoteLink>();
    private StateMachineHandler stateMachine;

    private String localId;
    private List<LocalAsConfig> localAs;


    public RemoteController(RemoteControllerConfig config, RoutingTable table) {
        this.ip = config.getIp();
        this.id = config.getId();
        this.cs = config.getCs();
        this.port = config.getPort();
        this.socket = null;
        this.table = table;
        //this.connectState = new controller.ConnectState();
        //this.state = new ControllerState(ControllerState.IDLE);
        for (RemoteControllerLinkConfig c:config.getListLink()) {
            listLink.add(new RemoteLink(c));
        }
        this.stateMachine = null;
    }

    public String getIp() {
        return ip;
    }

    public String getId() {
        return id;
    }

    public String getCs() {
        return cs;
    }

    public int getPort() {
        return port;
    }

    public Socket getSocket() {
        return socket;
    }

    public ControllerEventList getReceiveEvent() {
        return receiveEvent;
    }

    public ControllerEventList getSendEvent() {
        return sendEvent;
    }

    public RoutingTable getTable() {
        return table;
    }


    public List<RemoteLink> getListLink() {
        return listLink;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCs(String cs) {
        this.cs = cs;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setReceiveEvent(ControllerEventList receiveEvent) {
        this.receiveEvent = receiveEvent;
    }

    public void setSendEvent(ControllerEventList sendEvent) {
        this.sendEvent = sendEvent;
    }



    public void setTable(RoutingTable table) {
        this.table = table;
    }


    public void setListLink(List<RemoteLink> listLink) {
        this.listLink = listLink;
    }

    public String getLocalId() {
        return localId;
    }

    public List<LocalAsConfig> getLocalAs() {
        return localAs;
    }

    public StateMachineHandler getStateMachine() {
        return stateMachine;
    }



    public void createStateMachine(List<LocalAsConfig> localAs, String localId) {
        this.localAs = localAs;
        this.localId = localId;
        logger.info(ip + ":" + port + "   Create state machine...");
        stateMachine = new StateMachineHandler(this);
        new Thread(stateMachine).start();
    }

    public void wakeUpListen() {
        //new Thread(new controller.StateMachineHandler(connectState, state, sendEvent, receiveEvent, table)).start();
        //connectState.setReceiveState(true);
        //new Thread(new ListenThread(socket, receiveEvent)).start();
    }

    public void createConnect() {
        logger.info(ip + ":" + port + "   Create connect thread...");
        if (this.getCs().equals("c"))
            new Thread(new ConnectThread(this)).start();
    }

    public void startReceive() {
        logger.info(ip + ":" + port + "   Start receive thread...");
        new Thread(new ReceiveMessageHandler(socket, receiveEvent)).start();
    }

    public void startSend() {
        logger.info(ip + ":" + port + "   Start send thread...");
        new Thread(new SendMessageHandler(socket, sendEvent)).start();
    }


}
