package net.floodlightcontroller.egp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.floodlightcontroller.egp.config.AllConfig;
import net.floodlightcontroller.egp.event.*;
import net.floodlightcontroller.egp.routing.HopSwitch;
import net.floodlightcontroller.egp.routing.RoutingTableEntry;
import net.floodlightcontroller.egp.state.ControllerState;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StateMachineHandler implements Runnable{

    private static Logger logger = LoggerFactory.getLogger("egp.controller.StateMachineHandler");

    private RemoteController controller;
    private ControllerState state;

    StateMachineHandler(RemoteController controller) {
        this.controller = controller;
        state = new ControllerState(ControllerState.IDLE);
    }

    private void moveToState(int newState) {
        state.setState(newState);
    }

    public ControllerState getControllerState() {
        return this.state;
    }

    public void handleUpdateEvent(UpdateEvent updateEvent) {
        String info = updateEvent.getInfo().split(" ")[1];
        UpdateInfo updateInfo;
        try {
            ObjectMapper mapper = new ObjectMapper();
            updateInfo = mapper.readValue(info, UpdateInfo.class);
        }  catch (Exception e){
            e.printStackTrace();
            return ;
        }
        RoutingTableEntry entry = new RoutingTableEntry(updateInfo.getIndex(), updateInfo.getNextHop(), updateInfo.getPath(), updateInfo.getTimestamp());
        controller.getTable().addRoute(entry.getNextHop(), entry);
    }


    private void handleEvent(ControllerEvent event) {
        logger.debug("Handle event " + event.getInfo());
        if (event.getType() == ControllerEvent.OPEN) {
            moveToState(ControllerState.OPENCONFIRM);
            controller.getSendEvent().addEvent(new KeepAliveEvent());
        }
        if (event.getType() == ControllerEvent.NOTIFICATION) {
        }
        if (event.getType() == ControllerEvent.KEEPALIVE) {
            moveToState(ControllerState.ESTABLISHED);
        }
        if (event.getType() == ControllerEvent.UPDATE) {
            UpdateEvent updateEvent = (UpdateEvent) event;
            handleUpdateEvent(updateEvent);
        }
        if (event.getType() == ControllerEvent.LINKUP) {
            controller.getListLink().get(0).getState().setLink(true);
            HopSwitch hopSwitch = new HopSwitch(controller.getListLink().get(0).getRemoteSwitchId(), controller.getListLink().get(0).getRemoteSwitchPort());
            controller.getTable().sendAllEntry(controller.getSendEvent(), hopSwitch);
        }
        if (event.getType() == ControllerEvent.LINKDOWN) {
            controller.getListLink().get(0).getState().setLink(false);
            HopSwitch hopSwitch = new HopSwitch(controller.getListLink().get(0).getLocalSwitchId(), controller.getListLink().get(0).getLocalSwitchPort());
            controller.getTable().linkDown(hopSwitch);
        }
    }

    public void run() {
        while (true) {
            if (state.getState() == ControllerState.IDLE) {
                while (controller.getSocket() == null) {
                    try {
                        Thread.sleep(Constant.STATEMACHINE_SLEEP_INTERVAL);
                    }  catch (Exception e) {
                        logger.error(e.toString());
                        return ;
                    }
                }
                moveToState(ControllerState.CONNECT);
                controller.startReceive();
                controller.startSend();
                controller.getSendEvent().addEvent(new OpenEvent(controller.getLocalId()));
            }  else {
                    ControllerEvent event = controller.getReceiveEvent().popEvent();
                    if (event == null) {
                        try {
                            Thread.sleep(Constant.RECEIVE_TIME_INTERVAL);
                        }  catch (Exception e) {
                            logger.error(e.toString());
                            return ;
                        }
                    }  else {
                        try {
                            handleEvent(event);
                        }  catch (Exception e) {
                            logger.error(e.toString());
                            e.printStackTrace();
                            return ;
                        }
                    }

            }
        }

    }
}
