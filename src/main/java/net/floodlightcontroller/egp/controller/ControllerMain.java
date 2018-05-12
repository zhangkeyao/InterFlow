package net.floodlightcontroller.egp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.floodlightcontroller.egp.config.AllConfig;
import net.floodlightcontroller.egp.config.LocalAsConfig;
import net.floodlightcontroller.egp.config.RemoteControllerConfig;
import net.floodlightcontroller.egp.config.RemoteControllerLinkConfig;
import net.floodlightcontroller.egp.egpkeepalive.EGPKeepAlive;
import net.floodlightcontroller.egp.egpkeepalive.KeepAliveSendThread;
import net.floodlightcontroller.egp.egpkeepalive.KeepAliveTimerThread;
import net.floodlightcontroller.egp.routing.RoutingTable;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ControllerMain {

    private static Logger logger = LoggerFactory.getLogger("egp.controller.ControllerMain");

    private String configFileName;
    private List<RemoteController> controllerList= new ArrayList<RemoteController>();
    private RoutingTable table;

    private int localPort;
    private String localId;
    private List<LocalAsConfig> localAs = new ArrayList<LocalAsConfig>();
    private Thread serverThread;



    public ControllerMain(String configFileName) {
        this.configFileName = configFileName;
        this.table = new RoutingTable(controllerList);
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public void setLocalAs(List<LocalAsConfig> localAs) {
        this.localAs = localAs;
    }

    public int getLocalPort() {
        return localPort;
    }

    public String getLocalId() {
        return localId;
    }

    public List<LocalAsConfig> getLocalAs() {
        return localAs;
    }

    public List<RemoteController> getControllerList() {
        return controllerList;
    }

    public RoutingTable getTable() {
        return table;
    }

    public void setTable(RoutingTable table) {
        this.table = table;
    }

    public void setControllerList(List<RemoteController> controllerList) {
        this.controllerList = controllerList;
    }

    private boolean addRemoteController(RemoteControllerConfig config) {
        RemoteController controller = new RemoteController(config, table);
        this.controllerList.add(controller);
        return true;
    }

    private void debugConfigFile(AllConfig config) {
        System.out.println("localAs:");
        //if (localAs != null)
        for (LocalAsConfig asConfig:config.getLocalAs()) asConfig.print();
        System.out.println("localID:" + config.getLocalId());
        System.out.println("number of controllers:" + config.getListController().size());
        for (RemoteControllerConfig c:config.getListController()) {
            System.out.println("---" + c.getIp() + ":" + c.getPort());
            for (RemoteControllerLinkConfig l:c.getListLink()) {
                System.out.println("---------" + l.getLocalSwitchId() + "," + l.getLocalSwitchPort() + "," + l.getRemoteSwitchId() + "," + l.getRemoteSwitchPort());
            }
        }
    }

    private boolean getConfigFile() {
        logger.info("Loading config from " + configFileName);

        AllConfig config = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            config = mapper.readValue(new File(configFileName), AllConfig.class);
        }  catch (Exception e) {
            logger.error(e.toString());
        }

        debugConfigFile(config);


        try {
            config.check();
            this.localPort = Integer.parseInt(config.getLocalPort());
            this.localAs = config.getLocalAs();
            this.localId = config.getLocalId();
            for (RemoteControllerConfig c:config.getListController()) {
                addRemoteController(c);
            }
        }  catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString());
        }
        table.setLocalAs(this.localAs);
        table.setLocalId(this.localId);
        table.initLocal();

        logger.info("Load config successfully");
        return true;
    }

    private void createStateMachine() {
        logger.info("Creating state machines...");
        for (RemoteController controller:controllerList) {
            controller.createStateMachine(localAs, localId);
        }
    }

    private void createServer() {
        logger.info("Creating listening sockets...");
        serverThread = new Thread(new ServerThread(localPort, controllerList));
        serverThread.start();
        logger.info("Creating listening sockets successfully");
    }

    private void createConnect() {
        logger.info("Creating sending sockets...");
        for (RemoteController controller:controllerList) {
            controller.createConnect();
        }
        logger.info("Creating sending sockets successfully");
    }



    private void cliStart() {
        new Thread(new CommandLineInterfaceThread(this)).start();

    }
    
    public RemoteController switchPortToRemoteController(String switchid, String port){
    	for (RemoteController controller:controllerList) {
            for (RemoteLink link:controller.getListLink()){
            	if (link.getLocalSwitchId().equals(switchid) && link.getLocalSwitchPort().equals(port)){
            		return controller;
            	}
            }
        }
    	return null;
    }

    public void initKeepAlive(){
    	for (RemoteController controller:controllerList) {
            for (RemoteLink link:controller.getListLink()){
        		int port = Integer.parseInt(link.getLocalSwitchPort());
        		String switchId = link.getLocalSwitchId();
        		String switchport = switchId + ": " + String.valueOf(port);
        		EGPKeepAlive.getTimermap().put(switchport, Long.valueOf(0));
        		EGPKeepAlive.getStatusmap().put(switchport, Boolean.valueOf(false));
        		KeepAliveSendThread sendThread = new KeepAliveSendThread(switchId, port);
        		sendThread.start();
            }
        }
    	KeepAliveTimerThread timerThread = new KeepAliveTimerThread();
		timerThread.start();
    }

    public void work() {
        logger.info("Working...");
        if (!getConfigFile()) {
            logger.error("Cannot read configuration file successfully");
            return ;
        }
        initKeepAlive();
        createStateMachine(); // create state machine for each remote controller
        createServer(); // create listen thread
        createConnect();
        //cliStart();
        logger.info("Exit");
    }
}
