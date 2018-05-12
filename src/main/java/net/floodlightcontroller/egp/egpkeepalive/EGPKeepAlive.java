package net.floodlightcontroller.egp.egpkeepalive;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.projectfloodlight.openflow.protocol.OFFactory;
import org.projectfloodlight.openflow.protocol.OFFlowAdd;
import org.projectfloodlight.openflow.protocol.OFFlowDelete;
import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.protocol.OFPacketIn;
import org.projectfloodlight.openflow.protocol.OFPacketOut;
import org.projectfloodlight.openflow.protocol.OFPortDesc;
import org.projectfloodlight.openflow.protocol.OFType;
import org.projectfloodlight.openflow.protocol.OFVersion;
import org.projectfloodlight.openflow.protocol.action.OFAction;
import org.projectfloodlight.openflow.protocol.action.OFActionOutput;
import org.projectfloodlight.openflow.protocol.action.OFActionSetDlDst;
import org.projectfloodlight.openflow.protocol.action.OFActionSetField;
import org.projectfloodlight.openflow.protocol.action.OFActionSetNwDst;
import org.projectfloodlight.openflow.protocol.action.OFActions;
import org.projectfloodlight.openflow.protocol.instruction.OFInstruction;
import org.projectfloodlight.openflow.protocol.instruction.OFInstructionApplyActions;
import org.projectfloodlight.openflow.protocol.instruction.OFInstructions;
import org.projectfloodlight.openflow.protocol.match.Match;
import org.projectfloodlight.openflow.protocol.match.MatchField;
import org.projectfloodlight.openflow.protocol.oxm.OFOxms;
import org.projectfloodlight.openflow.types.DatapathId;
import org.projectfloodlight.openflow.types.EthType;
import org.projectfloodlight.openflow.types.IPv4Address;
import org.projectfloodlight.openflow.types.IPv4AddressWithMask;
import org.projectfloodlight.openflow.types.IpProtocol;
import org.projectfloodlight.openflow.types.MacAddress;
import org.projectfloodlight.openflow.types.OFBufferId;
import org.projectfloodlight.openflow.types.OFPort;
import org.projectfloodlight.openflow.types.TransportPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.floodlightcontroller.core.FloodlightContext;
import net.floodlightcontroller.core.IFloodlightProviderService;
import net.floodlightcontroller.core.IOFMessageListener;
import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.IOFSwitchListener;
import net.floodlightcontroller.core.PortChangeType;
import net.floodlightcontroller.core.internal.IOFSwitchService;
import net.floodlightcontroller.core.module.FloodlightModuleContext;
import net.floodlightcontroller.core.module.FloodlightModuleException;
import net.floodlightcontroller.core.module.IFloodlightModule;
import net.floodlightcontroller.core.module.IFloodlightService;
import net.floodlightcontroller.egp.controller.ControllerMain;
import net.floodlightcontroller.packet.Data;
import net.floodlightcontroller.packet.Ethernet;
import net.floodlightcontroller.packet.IPv4;
import net.floodlightcontroller.packet.UDP;

public class EGPKeepAlive implements IFloodlightModule, IOFMessageListener,
		IOFSwitchListener {

	private static IFloodlightProviderService floodlightProvider;
	private static IOFSwitchService switchService;
	private static Logger logger;
	private static final String configFileName = "target/config.txt";
	protected static ControllerMain controllerMain;
	protected static HashMap<String, Long> timermap = new HashMap<String, Long>();
	protected static HashMap<String, Boolean> statusmap = new HashMap<String, Boolean>();
	
	
	public static HashMap<String, Long> getTimermap() {
		return timermap;
	}

	public static void setTimermap(HashMap<String, Long> timermap) {
		EGPKeepAlive.timermap = timermap;
	}

	public static HashMap<String, Boolean> getStatusmap() {
		return statusmap;
	}

	public static void setStatusmap(HashMap<String, Boolean> statusmap) {
		EGPKeepAlive.statusmap = statusmap;
	}

	@Override
	public String getName() {
		return EGPKeepAlive.class.getSimpleName();
	}

	@Override
	public boolean isCallbackOrderingPrereq(OFType type, String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCallbackOrderingPostreq(OFType type, String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void switchAdded(DatapathId switchId) {
		logger.info("Switch {} connected; processing its static entries",switchId.toString());
		//createFlowMods(switchId.toString(), "10.0.1.0/24","10.0.2.0/24", null, null, null, 1);
		//createFlowMods(switchId.toString(), "10.0.2.0/24","10.0.1.0/24", null, null, null, 2);
		//deleteFlowMods(switchId.toString(), "10.0.1.0/24","10.0.2.0/24", null, null, null, 1);
	}

	@Override
	public void switchRemoved(DatapathId switchId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void switchActivated(DatapathId switchId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void switchPortChanged(DatapathId switchId, OFPortDesc port,
			PortChangeType type) {
		// TODO Auto-generated method stub

	}

	@Override
	public void switchChanged(DatapathId switchId) {
		// TODO Auto-generated method stub

	}

	@Override
	public net.floodlightcontroller.core.IListener.Command receive(
			IOFSwitch sw, OFMessage msg, FloodlightContext cntx) {
		switch (msg.getType()) {
        	case PACKET_IN:
        		OFPacketIn myPacketIn = (OFPacketIn) msg;
        		OFPort myInPort = (myPacketIn.getVersion().compareTo(OFVersion.OF_12) < 0) 
        		? myPacketIn.getInPort() : myPacketIn.getMatch().get(MatchField.IN_PORT);
        		Ethernet eth = IFloodlightProviderService.bcStore.get(cntx,
        				IFloodlightProviderService.CONTEXT_PI_PAYLOAD);
        		//logger.info("Packet in: inport: {} seen on switch: {}", myInPort.toString(), sw.getId().toString());
        		//logger.info("Packet in: srcmac: {}, dstmac: {}",
        		//		eth.getSourceMACAddress().toString(), eth.getDestinationMACAddress().toString());
        		if (eth.getEtherType() == Ethernet.TYPE_IPv4){
        			IPv4 ipv4 = (IPv4) eth.getPayload();
        			if (ipv4.getProtocol() == IpProtocol.UDP){
        				UDP udp = (UDP) ipv4.getPayload();
        				TransportPort srcPort = udp.getSourcePort();
        				TransportPort dstPort = udp.getDestinationPort();
        				if (srcPort.equals(TransportPort.of(30001)) && dstPort.equals(TransportPort.of(30002))){
        					Data data = (Data) udp.getPayload();
            				byte[] databyte = data.getData();
            				String datastring = databyte.toString();
            				String infostr = String.format("Packet in: inport: %s seen on switch: %s, Payload: %s", 
            						myInPort.toString(), sw.getId().toString(), datastring);
            				logger.info(infostr);
            				String switchport = sw.getId().toString() + ": " + myInPort.toString();
            				long currentTime = System.currentTimeMillis();
            				timermap.put(switchport, Long.valueOf(currentTime));
        				}
        			}
        		}
        		
        break;
            default:
                break;
		}
		return Command.CONTINUE;
	}

	@Override
	public Collection<Class<? extends IFloodlightService>> getModuleServices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Class<? extends IFloodlightService>, IFloodlightService> getServiceImpls() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Class<? extends IFloodlightService>> getModuleDependencies() {
		Collection<Class<? extends IFloodlightService>> l =
		        new ArrayList<Class<? extends IFloodlightService>>();
		    l.add(IFloodlightProviderService.class);
		    l.add(IOFSwitchService.class);
		    return l;
	}

	@Override
	public void init(FloodlightModuleContext context)
			throws FloodlightModuleException {
		floodlightProvider = context.getServiceImpl(IFloodlightProviderService.class);
		switchService = context.getServiceImpl(IOFSwitchService.class);
		logger = LoggerFactory.getLogger("egpnew ControllerMain(configFileName).work();.egpkeepalive.EGPKeepAlive");
		controllerMain = new ControllerMain(configFileName);
	}

	@Override
	public void startUp(FloodlightModuleContext context)
			throws FloodlightModuleException {
		floodlightProvider.addOFMessageListener(OFType.PACKET_IN, this);
		switchService.addOFSwitchListener(this);
		/*
		int port = 1;
		String switchId = "00:00:00:00:00:00:00:01";
		String switchport = switchId + ":" + String.valueOf(port);
		timermap.put(switchport, Long.valueOf(0));
		statusmap.put(switchport, Boolean.valueOf(false));
		KeepAliveSendThread thread1 = new KeepAliveSendThread(switchId, port);
		thread1.start();*/
		controllerMain.work();
	}
	
	public static void SendPacketOut(String switchid, int outport) {
		IOFSwitch mySwitch = switchService.getSwitch(DatapathId.of(switchid));
		if (mySwitch == null)
			return;
		OFFactory myFactory = mySwitch.getOFFactory();		
		/* Compose L2 packet. */
		Ethernet eth = new Ethernet();
		eth.setSourceMACAddress(MacAddress.of("10:00:00:00:00:00"));
		eth.setDestinationMACAddress(MacAddress.of("11:00:00:00:00:00"));
		eth.setEtherType(Ethernet.TYPE_IPv4);
	 
		/* Compose L3 packet. */
		IPv4 ipv4 = new IPv4();
		ipv4.setSourceAddress(IPv4Address.of("127.0.0.1"));
		ipv4.setDestinationAddress(IPv4Address.of("127.0.0.1"));
		ipv4.setProtocol(IpProtocol.UDP);
		
		/* Compose L4 packet. */
		UDP udp = new UDP();
		udp.setSourcePort(TransportPort.of(30001));
		udp.setDestinationPort(TransportPort.of(30002));
		
		/* Compose L5 packet. */
		Data data = new Data();
		String Keepalivedata = "Keep Alive!";
		byte[] Keepalivebyte = Keepalivedata.getBytes();
		data.setData(Keepalivebyte);
				 
		/* Set L2 L3 L4's payload */
		eth.setPayload(ipv4);
		ipv4.setPayload(udp);
		udp.setPayload(data);

		 
		/* Specify the switch port(s) which the packet should be sent out. */
		OFActionOutput output = myFactory.actions().buildOutput()
		    .setPort(OFPort.of(outport))
		    .build();

		/* 
		 * Compose the OFPacketOut with the above Ethernet packet as the 
		 * payload/data, and the specified output port(s) as actions.
		 */
		OFPacketOut myPacketOut = myFactory.buildPacketOut()
		    .setData(eth.serialize())
		    .setBufferId(OFBufferId.NO_BUFFER)
		    .setActions(Collections.singletonList((OFAction) output))
		    .build();
		 
		/* Write the packet to the switch via an IOFSwitch instance. */
		mySwitch.write(myPacketOut);
	}
	
	public static void createFlowMods(String switchid, String srcipv4, String dstipv4, 
			String protocol, String srcport, String dstport, int outport){
		IOFSwitch mySwitch = switchService.getSwitch(DatapathId.of(switchid));
		OFFactory myFactory = mySwitch.getOFFactory();
		OFVersion myVersion = myFactory.getVersion();
		
		Match.Builder myMatchBuilder = myFactory.buildMatch();
		//myMatchBuilder.setExact(MatchField.IN_PORT, OFPort.of(1))
		myMatchBuilder.setExact(MatchField.ETH_TYPE, EthType.IPv4);
		if (srcipv4 != null)
			myMatchBuilder.setMasked(MatchField.IPV4_SRC, IPv4AddressWithMask.of(srcipv4));
		if (dstipv4 != null)
			myMatchBuilder.setMasked(MatchField.IPV4_DST, IPv4AddressWithMask.of(dstipv4));
		if (protocol != null){
			if (protocol.equalsIgnoreCase("tcp")){
				myMatchBuilder.setExact(MatchField.IP_PROTO, IpProtocol.TCP);
				if (srcport != null)
					myMatchBuilder.setExact(MatchField.TCP_SRC, TransportPort.of(Integer.parseInt(srcport)));
				if (dstport != null)
					myMatchBuilder.setExact(MatchField.TCP_DST, TransportPort.of(Integer.parseInt(dstport)));
			}		
			if (protocol.equalsIgnoreCase("udp")){
				myMatchBuilder.setExact(MatchField.IP_PROTO, IpProtocol.UDP);
				if (srcport != null)
					myMatchBuilder.setExact(MatchField.UDP_SRC, TransportPort.of(Integer.parseInt(srcport)));
				if (dstport != null)
					myMatchBuilder.setExact(MatchField.UDP_DST, TransportPort.of(Integer.parseInt(dstport)));
			}
		}
		
		
		Match myMatch = myMatchBuilder.build();
		
		switch (myVersion){
			case OF_10:
				ArrayList<OFAction> actionList10 = new ArrayList<OFAction>();
				OFActions actions10 = myFactory.actions();
				
				/*
				// Use builder to create OFAction.
				OFActionSetDlDst setDlDst10 = actions10.buildSetDlDst()
						.setDlAddr(MacAddress.of("ff:ff:ff:ff:ff:ff"))
						.build();
				actionList10.add(setDlDst10);
				
				// Create OFAction directly w/o use of builder. 
				OFActionSetNwDst setNwDst10 = actions10.buildSetNwDst()
						.setNwAddr(IPv4Address.of("255.255.255.255"))
						.build();
				actionList10.add(setNwDst10);
				
				*/
				 
				// Use builder again.
				OFActionOutput output = actions10.buildOutput()
				    .setMaxLen(0xFFffFFff)
				    .setPort(OFPort.of(1))
				    .build();
				actionList10.add(output);
				
				
				OFFlowAdd flowAdd10 = myFactory.buildFlowAdd()
					    .setBufferId(OFBufferId.NO_BUFFER)
					    .setHardTimeout(3600)
					    .setIdleTimeout(3600)
					    .setPriority(32768)
					    .setMatch(myMatch)
					    .setActions(actionList10)
					    .setOutPort(OFPort.of(outport))
					    .build();
				
				mySwitch.write(flowAdd10);
				break;
			case OF_13:
				ArrayList<OFAction> actionList13 = new ArrayList<OFAction>();
				OFInstructions instructions13 = myFactory.instructions();
				OFActions actions13 = myFactory.actions();
				OFOxms oxms13 = myFactory.oxms();
				
				/*
				// Use OXM to modify data layer dest field.
				OFActionSetField setDlDst13 = actions13.buildSetField()
				    .setField(
				        oxms13.buildEthDst()
				        .setValue(MacAddress.of("ff:ff:ff:ff:ff:ff"))
				        .build()
				    )
				    .build();
				actionList13.add(setDlDst13);
				 
				// Use OXM to modify network layer dest field.
				OFActionSetField setNwDst13 = actions13.buildSetField()
				    .setField(
				        oxms13.buildIpv4Dst()
				        .setValue(IPv4Address.of("255.255.255.255"))
				        .build()
				    )
				    .build();
				actionList13.add(setNwDst13);
				
				*/
				 
				 
				// Output to a port is also an OFAction, not an OXM.
				OFActionOutput output13 = actions13.buildOutput()
				    .setMaxLen(0xFFffFFff)
				    .setPort(OFPort.of(outport))
				    .build();
				actionList13.add(output13);
				
				OFInstructionApplyActions applyActions13 = instructions13.buildApplyActions()
					    .setActions(actionList13)
					    .build();
				
				ArrayList<OFInstruction> instructionList13 = new ArrayList<OFInstruction>();
				instructionList13.add(applyActions13);
				
				OFFlowAdd flowAdd13 = myFactory.buildFlowAdd()
					    .setBufferId(OFBufferId.NO_BUFFER)
					    .setHardTimeout(3600)
					    .setIdleTimeout(3600)
					    .setPriority(32768)
					    .setMatch(myMatch)
					    .setInstructions(instructionList13)
					    .setOutPort(OFPort.of(outport))
					    .build();
				
				mySwitch.write(flowAdd13);
				break;
			default:
				logger.error("Unsupported OFVersion: {}", myVersion.toString());
				break;
		}
		
	}
	
	public static void deleteFlowMods(String switchid, String srcipv4, String dstipv4, 
			String protocol, String srcport, String dstport, int outport){
		IOFSwitch mySwitch = switchService.getSwitch(DatapathId.of(switchid));
		OFFactory myFactory = mySwitch.getOFFactory();
		OFVersion myVersion = myFactory.getVersion();
		
		Match.Builder myMatchBuilder = myFactory.buildMatch();
		//myMatchBuilder.setExact(MatchField.IN_PORT, OFPort.of(1))
		myMatchBuilder.setExact(MatchField.ETH_TYPE, EthType.IPv4);
		if (srcipv4 != null)
			myMatchBuilder.setMasked(MatchField.IPV4_SRC, IPv4AddressWithMask.of(srcipv4));
		if (dstipv4 != null)
			myMatchBuilder.setMasked(MatchField.IPV4_DST, IPv4AddressWithMask.of(dstipv4));
		if (protocol != null){
			if (protocol.equalsIgnoreCase("tcp")){
				myMatchBuilder.setExact(MatchField.IP_PROTO, IpProtocol.TCP);
				if (srcport != null)
					myMatchBuilder.setExact(MatchField.TCP_SRC, TransportPort.of(Integer.parseInt(srcport)));
				if (dstport != null)
					myMatchBuilder.setExact(MatchField.TCP_DST, TransportPort.of(Integer.parseInt(dstport)));
			}		
			if (protocol.equalsIgnoreCase("udp")){
				myMatchBuilder.setExact(MatchField.IP_PROTO, IpProtocol.UDP);
				if (srcport != null)
					myMatchBuilder.setExact(MatchField.UDP_SRC, TransportPort.of(Integer.parseInt(srcport)));
				if (dstport != null)
					myMatchBuilder.setExact(MatchField.UDP_DST, TransportPort.of(Integer.parseInt(dstport)));
			}
		}
		
		Match myMatch = myMatchBuilder.build();
		
		OFFlowDelete flowDelete = myFactory.buildFlowDelete()
				.setBufferId(OFBufferId.NO_BUFFER)
				.setHardTimeout(3600)
				.setIdleTimeout(3600)
				.setPriority(32768)
				.setMatch(myMatch)
				.build();
				
		mySwitch.write(flowDelete);
	}

}
