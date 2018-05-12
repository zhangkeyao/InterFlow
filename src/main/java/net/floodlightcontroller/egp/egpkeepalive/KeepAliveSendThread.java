package net.floodlightcontroller.egp.egpkeepalive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class KeepAliveSendThread extends Thread{

	private String switchid;
	private int outport;
	private static Logger logger = LoggerFactory.getLogger("egp.egpkeepalive.KeepAliveSendThread"); 
	
	
	public KeepAliveSendThread(String switchid, int outport) {
		this.switchid = switchid;
		this.outport = outport;
	}
	
	
	@Override
	public void run() {
		while (true){
			EGPKeepAlive.SendPacketOut(switchid, outport);
			logger.info("Send KeepAlive from {}: {}", switchid, String.valueOf(outport));
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
